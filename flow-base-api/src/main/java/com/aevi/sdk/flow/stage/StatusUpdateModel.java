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
import android.support.annotation.NonNull;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseStatusUpdateService;
import com.aevi.sdk.flow.service.ClientCommunicator;

/**
 * Model for the status update stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * Note that it is NOT allowed to launch any form of user interface (activities, etc) from the status update stage.
 *
 * See {@link BaseStatusUpdateService} or domain implementations for various ways of getting access to this object.
 *
 * Call {@link #finish()} or {@link #finishWithReferences(AdditionalData)} to finish processing.
 */
public class StatusUpdateModel extends BaseStageModel {

    private final Request request;

    private StatusUpdateModel(ClientCommunicator clientCommunicator, Request request) {
        super(clientCommunicator);
        this.request = request;
    }

    /**
     * Create an instance from a channel server.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Request provided as a string
     * @return An instance of {@link StatusUpdateModel}
     */
    @NonNull
    public static StatusUpdateModel fromService(ClientCommunicator clientCommunicator, Request request) {
        return new StatusUpdateModel(clientCommunicator, request);
    }

    /**
     * Get the request.
     *
     * @return The request
     */
    @NonNull
    public Request getRequest() {
        return request;
    }

    /**
     * Call to finish processing without sending any data back to initiating client.
     */
    public void finish() {
        sendEmptyResponse();
    }

    /**
     * Call to finish processing and pass back a set of references to the initiating client.
     *
     * @param references The references to send before finishing
     */
    public void finishWithReferences(AdditionalData references) {
        doSendResponse(new Response(request, true, "", references).toJson());
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return request.toJson();
    }

    // Status update handlers are not allowed to launch into foreground
    @Override
    @NonNull
    public ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson) {
        throw new IllegalStateException("Starting activities is not allowed for status updates");
    }
}
