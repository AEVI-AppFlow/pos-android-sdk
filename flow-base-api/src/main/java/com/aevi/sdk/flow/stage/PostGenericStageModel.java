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
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BasePostGenericService;
import com.aevi.sdk.flow.service.ClientCommunicator;

import static com.aevi.sdk.flow.stage.ServiceComponentDelegate.ACTIVITY_REQUEST_KEY;
import static com.aevi.sdk.flow.stage.ServiceComponentDelegate.EXTRAS_FLOW_INITIATOR;

/**
 * Model for the post generic stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePostGenericService} or domain implementations for various ways of getting access to this object.
 *
 * Call {@link #sendResponse()} if references have been added, or {@link #finish()} if no references are required.
 */
public class PostGenericStageModel extends BaseStageModel {

    private final Response inputResponse;
    private final Response outputResponse;

    private PostGenericStageModel(Activity activity, Response response, String flowInitiator) {
        super(activity, flowInitiator);
        this.inputResponse = response;
        this.outputResponse = Response.fromJson(response.toJson()); // Same data, different instance
    }

    private PostGenericStageModel(ClientCommunicator clientCommunicator, Response response, String flowInitiator) {
        super(clientCommunicator, flowInitiator);
        this.inputResponse = response;
        this.outputResponse = Response.fromJson(response.toJson()); // Same data, different instance
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PostGenericStageModel}
     */
    @NonNull
    public static PostGenericStageModel fromActivity(Activity activity) {
        String response = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        String flowInitiator = activity.getIntent().getStringExtra(EXTRAS_FLOW_INITIATOR);
        return new PostGenericStageModel(activity, Response.fromJson(response), flowInitiator);
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator A communicator that can be used to send messages and/or end the communication stream
     * @param response           The deserialised Response
     * @param flowInitiator The packageName of the app that started this flow
     * @return An instance of {@link PostGenericStageModel}
     */
    @NonNull
    public static PostGenericStageModel fromService(ClientCommunicator clientCommunicator, Response response, String flowInitiator) {
        return new PostGenericStageModel(clientCommunicator, response, flowInitiator);
    }

    /**
     * Get the response that the main generic application generated.
     *
     * @return The response from the main generic app
     */
    @NonNull
    public Response getResponse() {
        return inputResponse;
    }

    /**
     * Add references to the response.
     *
     * The value of this can be any arbitrary data or collection of data.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values A var-args input of values associated with the key
     * @param <T>    The type of object this data is an array of
     */
    @SafeVarargs
    public final <T> void addReferences(String key, T... values) {
        outputResponse.getResponseData().addData(key, values);
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        doSendResponse(outputResponse.toJson());
    }

    /**
     * Call when finished processing and no references have been added.
     */
    public void finish() {
        sendEmptyResponse();
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return inputResponse.toJson();
    }
}
