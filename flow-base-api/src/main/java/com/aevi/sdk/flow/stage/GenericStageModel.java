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

import android.app.Activity;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseGenericService;
import com.aevi.sdk.flow.service.ClientCommunicator;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the generic stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BaseGenericService}or domain implementations for various ways of getting access to this object.
 */
public class GenericStageModel extends BaseStageModel {

    private final Request request;

    private GenericStageModel(Activity activity, Request request) {
        super(activity);
        this.request = request;
    }

    private GenericStageModel(ClientCommunicator clientCommunicator, Request request) {
        super(clientCommunicator);
        this.request = request;
    }

    public static GenericStageModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new GenericStageModel(activity, Request.fromJson(request));
    }

    /**
     * Create an instance from a channel server.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Payment provided as a string
     * @return An instance of {@link GenericStageModel}
     */
    public static GenericStageModel fromService(ClientCommunicator clientCommunicator, Request request) {
        return new GenericStageModel(clientCommunicator, request);
    }

    /**
     * Get the request.
     *
     * @return The request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     *
     * @param response The response
     */
    public void sendResponse(Response response) {
        doSendResponse(response.toJson());
    }

    @Override
    public void sendResponse() {
        doSendResponse(new Response(request, false, "No response set").toJson());
    }

    @Override
    public String getRequestJson() {
        return request.toJson();
    }
}
