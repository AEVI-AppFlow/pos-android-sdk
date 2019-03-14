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
import android.content.Context;
import android.support.annotation.NonNull;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseGenericService;
import com.aevi.sdk.flow.service.ClientCommunicator;

import static com.aevi.sdk.flow.stage.ServiceComponentDelegate.getActivityRequestJson;

/**
 * Model for the generic stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BaseGenericService} or domain implementations for various ways of getting access to this object.
 *
 * Call {@link #sendResponse(Response)} to send back a response.
 */
public class GenericStageModel extends BaseStageModel {

    private final Request request;

    private GenericStageModel(Activity activity, Request request) {
        super(activity);
        this.request = request;
    }

    private GenericStageModel(ClientCommunicator clientCommunicator, Request request, InternalData senderInternalData) {
        super(clientCommunicator, senderInternalData);
        this.request = request;
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link GenericStageModel}
     */
    @NonNull
    public static GenericStageModel fromActivity(Activity activity) {
        return new GenericStageModel(activity, Request.fromJson(getActivityRequestJson(activity)));
    }

    /**
     * Create an instance from a channel server.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Request provided as a string
     * @param senderInternalData  The InternalData of the app that started this flow
     * @return An instance of {@link GenericStageModel}
     */
    @NonNull
    public static GenericStageModel fromService(ClientCommunicator clientCommunicator, Request request, InternalData senderInternalData) {
        return new GenericStageModel(clientCommunicator, request, senderInternalData);
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
    @NonNull
    public String getRequestJson() {
        return request.toJson();
    }
}
