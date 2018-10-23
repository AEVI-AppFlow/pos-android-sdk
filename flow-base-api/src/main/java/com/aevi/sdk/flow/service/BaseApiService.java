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

import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.android.rxmessenger.service.AbstractChannelService;
import com.aevi.sdk.flow.constants.InternalDataKeys;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.InternalData;

import io.reactivex.functions.Consumer;

import static com.aevi.sdk.flow.constants.ActivityEvents.FINISH;
import static com.aevi.sdk.flow.constants.AppMessageTypes.FORCE_FINISH_MESSAGE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.REQUEST_MESSAGE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.RESPONSE_MESSAGE;
import static com.aevi.sdk.flow.constants.MessageErrors.ERROR_SERVICE_EXCEPTION;
import static com.aevi.sdk.flow.constants.MessageErrors.ERROR_UNKNOWN_MESSAGE_TYPE;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;

/**
 * Base service for all API service implementations.
 */
public abstract class BaseApiService extends AbstractChannelService {

    public static final String BACKGROUND_PROCESSING = "backgroundProcessing";

    private final String TAG = getClass().getSimpleName(); // Use class name of implementing service

    protected final InternalData internalData;
    private boolean stopServiceOnEndOfStream;

    protected BaseApiService(String apiVersion) {
        internalData = new InternalData(apiVersion);
    }

    /**
     * Set whether or not this service should be stopped after the stream to the client has ended.
     *
     * The default is false, meaning that the service instance will stay active until Android decides to kill it.
     * This means the same service instance may be re-used for multiple requests.
     *
     * If set to true, the service will be stopped after each request and re-started for new ones. This may have a slight performance impact.
     *
     * @param stopServiceOnEndOfStream True to stop service on end of stream, false to keep it running
     */
    public void setStopServiceOnEndOfStream(boolean stopServiceOnEndOfStream) {
        this.stopServiceOnEndOfStream = stopServiceOnEndOfStream;
    }

    @Override
    protected void onNewClient(ChannelServer channelServer, String packageName) {

        final ClientCommunicator clientCommunicator = new ClientCommunicator(channelServer, internalData);
        clientCommunicator.subscribeToMessages().subscribe(new Consumer<String>() {
            @Override
            public void accept(String message) throws Exception {
                Log.d(TAG, "Received message: " + message);
                AppMessage appMessage = AppMessage.fromJson(message);
                String flowStage = null;
                if (appMessage.getInternalData() != null && appMessage.getInternalData().getAdditionalData() != null) {
                    flowStage = appMessage.getInternalData().getAdditionalData().get(InternalDataKeys.FLOW_STAGE);
                }
                if (flowStage == null) {
                    flowStage = "UNKNOWN";
                }

                checkVersions(appMessage, internalData);
                switch (appMessage.getMessageType()) {
                    case REQUEST_MESSAGE:
                        handleRequestMessage(clientCommunicator, appMessage.getMessageData(), flowStage);
                        break;
                    case FORCE_FINISH_MESSAGE:
                        onFinish();
                        break;
                    default:
                        Log.e(TAG, "Unknown message type: " + appMessage.getMessageType() + ". ");
                        clientCommunicator.sendResponseAsErrorAndEnd(ERROR_UNKNOWN_MESSAGE_TYPE);
                        break;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "Failed while parsing message from client", throwable);
            }
        });
    }

    static void checkVersions(AppMessage appMessage, InternalData checkWith) {
        // All we do for now is log this - at some point we might want to have specific checks or whatevs
        InternalData senderInternalData = appMessage.getInternalData();
        if (senderInternalData != null) {
            Log.i(BaseApiService.class.getSimpleName(), String.format("Our API version is: %s. Sender API version is: %s",
                    checkWith.getSenderApiVersion(),
                    senderInternalData.getSenderApiVersion()));
        } else {
            Log.i(BaseApiService.class.getSimpleName(),
                    String.format("Our API version is: %s. Sender API version is UNKNOWN!", checkWith.getSenderApiVersion()));
        }
    }

    private void handleRequestMessage(ClientCommunicator clientCommunicator, String messageData, String flowStage) {
        try {
            clientCommunicator.sendAck();
            processRequest(clientCommunicator, messageData, flowStage);
        } catch (Throwable t) {
            clientCommunicator.sendResponseAsErrorAndEnd(ERROR_SERVICE_EXCEPTION);
            throw t;
        }
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
    public void finishWithResponse(String clientMessageId, String response) {
        Log.d(TAG, "finishWithResponse");
        sendAppMessageAndEndStream(clientMessageId, response);
    }

    protected void sendAppMessageAndEndStream(String clientMessageId, String responseData) {
        AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, responseData, internalData);
        ChannelServer channelServer = getChannelServerForId(clientMessageId);
        if (channelServer != null) {
            channelServer.send(appMessage.toJson());
            channelServer.sendEndStream();
        }
        if (stopServiceOnEndOfStream) {
            stopSelf();
        }
    }

    /**
     * Called when there is a new request to process.
     *
     * @param clientCommunicator The client communicator for this stage
     * @param request            The request to be processed
     * @param flowStage          The flow stage this request is being called for
     */
    protected abstract void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request, @NonNull String flowStage);

    /**
     * Called when your application needs to abort what it is doing and finish any Activity that is running.
     *
     * As part of this callback, you need to;
     *
     * 1. Finish any Activity. If you launched it via the {@link #launchActivity(Class, String, String)} method, you can use {@link #finishLaunchedActivity(String)} to ask it to finish itself, provided that the activity has registered with the {@link ObservableActivityHelper} for finish events.
     *
     * 2. Finish the service with {@link #finishWithNoResponse(String)} as any response data would be ignored at this stage.
     */
    protected abstract void onFinish();


}
