/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.service;


import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.android.rxmessenger.MessageException;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.android.rxmessenger.service.AbstractMessengerService;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import io.reactivex.functions.Consumer;

import static android.content.Intent.*;
import static com.aevi.sdk.flow.model.ActivityEvents.FINISH;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;
import static com.aevi.sdk.flow.model.AppMessageTypes.*;
import static com.aevi.sdk.flow.model.MessageErrors.*;

public abstract class BaseApiService<REQUEST extends Jsonable, RESPONSE extends Jsonable> extends AbstractMessengerService {

    public static final String ACTIVITY_REQUEST_KEY = "request";
    public static final String BACKGROUND_PROCESSING = "backgroundProcessing";

    private final Class<REQUEST> requestClass;
    private final String TAG = getClass().getSimpleName(); // Use class name of implementing service

    private final InternalData internalData;

    protected BaseApiService(Class<REQUEST> requestClass, String apiVersion) {
        this.requestClass = requestClass;
        internalData = new InternalData(apiVersion);
    }

    @Override
    protected final void handleRequest(String clientMessageId, String message, String packageName) {
        Log.d(TAG, "Received message: " + message);
        AppMessage appMessage = AppMessage.fromJson(message);
        checkVersions(appMessage);
        switch (appMessage.getMessageType()) {
            case REQUEST_MESSAGE:
                handleRequestMessage(clientMessageId, appMessage.getMessageData(), packageName);
                break;
            case FORCE_FINISH_MESSAGE:
                finish(clientMessageId);
                break;
            default:
                Log.e(TAG, "Unknown message type: " + appMessage.getMessageType() + ". ");
                sendErrorMessageAndFinish(clientMessageId, ERROR_UNKNOWN_MESSAGE_TYPE);
                break;
        }
    }

    private void checkVersions(AppMessage appMessage) {
        // All we do for now is log this - at some point we might want to have specific checks or whatevs
        InternalData senderInternalData = appMessage.getInternalData();
        if (senderInternalData != null) {
            Log.i(TAG, String.format("Our API version is: %s. Sender API version is: %s",
                    internalData.getSenderApiVersion(),
                    senderInternalData.getSenderApiVersion()));
        } else {
            Log.i(TAG, String.format("Our API version is: %s. Sender API version is UNKNOWN!", internalData.getSenderApiVersion()));
        }
    }

    private void handleRequestMessage(String clientMessageId, String messageData, String packageName) {
        try {
            REQUEST request = JsonConverter.deserialize(messageData, requestClass);
            sendAck(clientMessageId);
            processRequest(clientMessageId, request);
        } catch (Throwable t) {
            sendErrorMessageAndFinish(clientMessageId, ERROR_SERVICE_EXCEPTION);
            throw t;
        }
    }

    private void sendAck(String clientMessageId) {
        Log.d(TAG, "Sending ack");
        AppMessage appMessage = new AppMessage(REQUEST_ACK_MESSAGE, internalData);
        sendMessageToClient(clientMessageId, appMessage.toJson());
    }

    private void sendErrorMessageAndFinish(String clientMessageId, String error) {
        Log.d(TAG, "Sending error message: " + error);
        AppMessage errorMessage = new AppMessage(FAILURE_MESSAGE, error, internalData);
        sendMessageToClient(clientMessageId, errorMessage.toJson());
        sendEndStreamMessageToClient(clientMessageId);
    }

    /**
     * Get the API version.
     *
     * The API versioning follows semver rules with major.minor.patch versions.
     *
     * @return The API version
     */
    protected String getApiVersion() {
        return internalData.getSenderApiVersion();
    }

    /**
     * Send notification that this service will process in the background and won't send back any response.
     *
     * Note that you should NOT show any UI after calling this, nor call any of the "finish...Response" methods.
     *
     * This is typically useful for post-transaction / post-flow services that processes the transaction information with no need
     * to show any user interface or augment the transaction.
     *
     * @param clientMessageId The client message id
     */
    protected void notifyBackgroundProcessing(String clientMessageId) {
        Log.d(TAG, "notifyBackgroundProcessing");
        internalData.addAdditionalData(BACKGROUND_PROCESSING, "true");
        sendAppMessageAndEndStream(clientMessageId, EMPTY_DATA);
    }

    /**
     * Finish without passing any response back.
     *
     * This is the preferred approach for any case where no response data was generated.
     *
     * @param clientMessageId The client message id
     */
    protected void finishWithNoResponse(String clientMessageId) {
        Log.d(TAG, "finishWithNoResponse");
        sendAppMessageAndEndStream(clientMessageId, EMPTY_DATA);
    }

    /**
     * Finish with a valid response.
     *
     * @param clientMessageId The client message id
     * @param response        The response object
     */
    protected void finishWithResponse(String clientMessageId, RESPONSE response) {
        Log.d(TAG, "finishWithResponse");
        sendAppMessageAndEndStream(clientMessageId, response.toJson());
    }

    private void sendAppMessageAndEndStream(String clientMessageId, String responseData) {
        AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, responseData, internalData);
        sendMessageToClient(clientMessageId, appMessage.toJson());
        sendEndStreamMessageToClient(clientMessageId);
    }

    /**
     * Called when there is a new request to process.
     *
     * @param clientMessageId The client message id
     * @param request         The request to be processed
     */
    protected abstract void processRequest(@NonNull String clientMessageId, @NonNull REQUEST request);

    /**
     * Called when your application needs to abort what it is doing and finish any Activity that is running.
     *
     * As part of this callback, you need to;
     *
     * 1. Finish any Activity. If you launched it via the {@link #launchActivity(Class, String, Jsonable)} method, you can use {@link #finishLaunchedActivity(String)} to ask it to finish itself, provided that the activity has registered with the {@link ObservableActivityHelper} for finish events.
     *
     * 2. Finish the service with {@link #finishWithNoResponse(String)} as any response data would be ignored at this stage.
     *
     * @param clientMessageId The client message id
     */
    protected abstract void finish(@NonNull String clientMessageId);

    /**
     * Helper to launch an activity with the request passed in.
     *
     * The request will be passed in the intent as a string extra with the key "request".
     *
     * @param activityCls     The activity that should handle the request.
     * @param clientMessageId The id that was passed to {@link #processRequest(String, Jsonable)}
     * @param request         The request model.
     * @param extras          Extras to add to the intent
     */
    protected void launchActivity(Class<? extends Activity> activityCls, final String clientMessageId, final REQUEST request, final Bundle extras) {
        Intent intent = new Intent(this, activityCls);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (request != null) {
            intent.putExtra(ACTIVITY_REQUEST_KEY, request.toJson());
        }
        intent.putExtra(ObservableActivityHelper.INTENT_ID, clientMessageId);
        intent.putExtras(extras);
        ObservableActivityHelper<RESPONSE> helper = ObservableActivityHelper.createInstance(this, intent);
        subscribeToLifecycle(helper);
        helper.startObservableActivity().subscribe(new Consumer<RESPONSE>() {
            @Override
            public void accept(@NonNull RESPONSE response) throws Exception {
                finishWithResponse(clientMessageId, response);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                handleActivityException(clientMessageId, throwable);
            }
        });
    }

    /**
     * Can be overriden to handle when the activity responds with an error / exception.
     */
    protected void handleActivityException(String clientMessageId, Throwable throwable) {
        if (throwable instanceof MessageException) {
            MessageException me = (MessageException) throwable;
            sendErrorMessageToClient(clientMessageId, me.getCode(), me.getMessage());
        } else {
            sendErrorMessageAndFinish(clientMessageId, throwable.getMessage());
        }
    }

    protected void launchActivity(Class<? extends Activity> activityCls, final String clientMessageId, final REQUEST request) {
        launchActivity(activityCls, clientMessageId, request, new Bundle());
    }

    private void subscribeToLifecycle(ObservableActivityHelper<RESPONSE> helper) {
        Log.d(TAG, "Subscribing to lifecycle events");
        helper.onLifecycleEvent().subscribe(new Consumer<Lifecycle.Event>() {
            @Override
            public void accept(Lifecycle.Event event) throws Exception {
                Log.d(TAG, "Received lifecycle event: " + event);
                onActivityLifecycleEvent(event);
            }
        });
    }

    /**
     * Can be overridden to receive lifecycle events from the activity started via {@link #launchActivity(Class, String)}.
     *
     * @param event
     */
    protected void onActivityLifecycleEvent(@NonNull Lifecycle.Event event) {
        // Default no-op
    }

    /**
     * Helper to launch an activity with no request but that needs to send a response.
     *
     * @param activityCls     The activity to start
     * @param clientMessageId The id that was passed to {@link #processRequest(String, Jsonable)}
     */
    protected void launchActivity(Class<? extends Activity> activityCls, final String clientMessageId) {
        launchActivity(activityCls, clientMessageId, null);
    }

    /**
     * Finish an activity launched via {@link #launchActivity(Class, String, Jsonable)}.
     *
     * Note that the activity must have subscribed via ObservableActivityHelper.registerForEvents().
     *
     * @param clientMessageId The id that was used to call {@link #launchActivity(Class, String, Jsonable)}.
     */
    protected void finishLaunchedActivity(String clientMessageId) {
        try {
            ObservableActivityHelper<RESPONSE> helper = ObservableActivityHelper.getInstance(clientMessageId);
            helper.sendEventToActivity(FINISH);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
    }

}
