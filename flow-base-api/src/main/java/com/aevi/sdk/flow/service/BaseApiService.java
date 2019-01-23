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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.android.rxmessenger.service.AbstractChannelService;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.InternalData;

import static com.aevi.sdk.flow.constants.AppMessageTypes.REQUEST_MESSAGE;
import static com.aevi.sdk.flow.constants.ErrorConstants.FLOW_SERVICE_ERROR;
import static com.aevi.sdk.flow.constants.ErrorConstants.INVALID_MESSAGE_TYPE;

/**
 * Internal base class for all API service implementations.
 *
 * This is an internal class not intended to be used directly by external applications. No guarantees are made of backwards compatibility and the
 * class may be removed without any warning.
 */
public abstract class BaseApiService extends AbstractChannelService {

    private final String TAG = getClass().getSimpleName(); // Use class name of implementing service

    protected final InternalData internalData;

    protected BaseApiService(String apiVersion) {
        internalData = new InternalData(apiVersion);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        internalData.setSenderPackageName(getPackageName());
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
        setStopSelfOnEndOfStream(stopServiceOnEndOfStream);
    }

    @Override
    protected final void onNewClient(ChannelServer channelServer, String packageName) {
        final ClientCommunicator clientCommunicator = new ClientCommunicator(channelServer, internalData);
        // We only listen for the initial message here to set up the relevant models based on the request and after that, it's up to subclasses
        // to manage further comms
        clientCommunicator.subscribeToMessages().take(1).subscribe(appMessage -> {
            Log.d(TAG, "Received message: " + appMessage.getMessageType());
            checkVersions(appMessage, internalData);
            String messageData = appMessage.getMessageData();
            switch (appMessage.getMessageType()) {
                case REQUEST_MESSAGE:
                    handleRequestMessage(clientCommunicator, messageData, appMessage.getInternalData());
                    break;
                default:
                    String msg = String.format("Unknown message type: %s", appMessage.getMessageType());
                    Log.e(TAG, msg);
                    clientCommunicator.sendResponseAsErrorAndEnd(INVALID_MESSAGE_TYPE, msg);
                    break;
            }
        }, throwable -> Log.e(TAG, "Failed while parsing message from client", throwable));
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

    private void handleRequestMessage(ClientCommunicator clientCommunicator, String requestData, InternalData internalData) {
        try {
            clientCommunicator.sendAck();
            processRequest(clientCommunicator, requestData, internalData);
        } catch (Throwable t) {
            clientCommunicator.sendResponseAsErrorAndEnd(FLOW_SERVICE_ERROR, String.format("Flow service failed with exception: %s", t.getMessage()));
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
     * Called when there is a new request to process.
     *
     * @param clientCommunicator The client communicator for this stage
     * @param request            The request to be processed
     * @param senderInternalData Associated internal data from client
     */
    protected abstract void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request,
                                           @Nullable InternalData senderInternalData);
}