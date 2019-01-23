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

package com.aevi.sdk.flow.stage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.util.Preconditions;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;
import static com.aevi.sdk.flow.constants.ActivityEvents.FINISH;
import static com.aevi.sdk.flow.constants.AppMessageTypes.*;
import static com.aevi.sdk.flow.constants.ErrorConstants.FLOW_SERVICE_ERROR;
import static com.aevi.sdk.flow.constants.FlowServiceEvents.FINISH_IMMEDIATELY;
import static com.aevi.sdk.flow.constants.FlowServiceEvents.RESUME_USER_INTERFACE;

/**
 * Provides service-based implementation for stage models.
 *
 * This can also be used directly from a service to start and communicate with activities, etc.
 */
public class ServiceComponentDelegate extends AndroidComponentDelegate {

    private static final String TAG = ServiceComponentDelegate.class.getSimpleName();
    public static final String ACTIVITY_REQUEST_KEY = "request";
    public static final String EXTRAS_INTERNAL_DATA_KEY = "internalData";

    private final ClientCommunicator clientCommunicator;
    private final PublishSubject<String> flowServiceMessageSubject;
    private Disposable messageDisposable;
    private String activityId;

    public ServiceComponentDelegate(ClientCommunicator clientCommunicator) {
        Preconditions.checkNotNull(clientCommunicator, "clientCommunicator can not be null");
        this.clientCommunicator = clientCommunicator;
        this.flowServiceMessageSubject = PublishSubject.create();
        listenForMessages();
    }

    private void listenForMessages() {
        messageDisposable = clientCommunicator.subscribeToMessages().subscribe(appMessage -> {
            switch (appMessage.getMessageType()) {
                case FORCE_FINISH_MESSAGE:
                    sendMessageToActivity(FINISH);
                    publishFlowServiceMessage(FINISH_IMMEDIATELY);
                    completeMessageStream();
                    break;
                case RESPONSE_OUTCOME:
                    publishFlowServiceMessage(appMessage.getMessageData());
                    completeMessageStream();
                    break;
                case RESTART_UI:
                    publishFlowServiceMessage(RESUME_USER_INTERFACE);
                    break;
                case REQUEST_MESSAGE:
                    // no-op
                    break;
                default:
                    Log.w(TAG, "Unknown/unexpected message type: " + appMessage.getMessageType());
                    break;
            }
        }, throwable -> Log.e(TAG, "Exception whilst listening for message", throwable));
    }

    private void publishFlowServiceMessage(String message) {
        Log.i(TAG, "Received message from FPS for flow service: " + message);
        flowServiceMessageSubject.onNext(message);
    }

    private void completeMessageStream() {
        flowServiceMessageSubject.onComplete();
        if (messageDisposable != null) {
            messageDisposable.dispose();
            messageDisposable = null;
        }
    }

    @NonNull
    public ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson) {
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_INTERNAL_DATA_KEY, clientCommunicator.getResponseInternalData().toJson());
        activityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (requestJson != null) {
            activityIntent.putExtra(ACTIVITY_REQUEST_KEY, requestJson);
        }
        this.activityId = UUID.randomUUID().toString();
        activityIntent.putExtra(ObservableActivityHelper.INTENT_ID, activityId);
        activityIntent.putExtras(extras);
        ObservableActivityHelper<AppMessage> helper = ObservableActivityHelper.createInstance(context, activityIntent);
        helper.startObservableActivity().subscribe(clientCommunicator::sendMessage,
                                                   throwable -> handleActivityException(throwable, clientCommunicator));
        return helper;
    }

    private void handleActivityException(Throwable throwable, ClientCommunicator clientCommunicator) {
        if (throwable instanceof FlowException) {
            FlowException me = (FlowException) throwable;
            clientCommunicator.sendResponseAsErrorAndEnd(me.getErrorCode(), me.getErrorMessage());
        } else {
            clientCommunicator.sendResponseAsErrorAndEnd(FLOW_SERVICE_ERROR,
                                                         String.format("Flow service failed during activity: %s", throwable.getMessage()));
        }
    }

    /**
     * Send message to activity previously started via {@link #processInActivity(Context, Intent, String)}.
     *
     * @param message The message
     */
    public void sendMessageToActivity(String message) {
        try {
            ObservableActivityHelper<AppMessage> helper = ObservableActivityHelper.getInstance(activityId);
            helper.sendEventToActivity(message);
        } catch (NoSuchInstanceException e) {
            Log.w(TAG, "Failed to find OAH for sending event to activity");
        }
    }

    @Override
    public void sendMessage(AppMessage appMessage) {
        appMessage.updateInternalData(clientCommunicator.getResponseInternalData());
        clientCommunicator.sendMessage(appMessage);
    }

    @Override
    public Observable<String> getFlowServiceMessages() {
        return flowServiceMessageSubject;
    }
}
