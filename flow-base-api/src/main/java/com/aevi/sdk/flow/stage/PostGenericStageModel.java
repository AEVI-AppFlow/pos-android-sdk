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
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the post generic stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link com.aevi.sdk.flow.service.BasePostGenericService} or domain implementations for various ways of getting access to this object.
 *
 * Call {@link #sendResponse()} if references have been added, or {@link #skip()} if no references are required.
 */
public class PostGenericStageModel extends BaseStageModel {

    private final Response inputResponse;
    private final Response outputResponse;

    private PostGenericStageModel(Activity activity, Response response) {
        super(activity);
        this.inputResponse = response;
        this.outputResponse = Response.fromJson(response.toJson()); // Same data, different instance
    }

    private PostGenericStageModel(ClientCommunicator clientCommunicator, Response response) {
        super(clientCommunicator);
        this.inputResponse = response;
        this.outputResponse = Response.fromJson(response.toJson()); // Same data, different instance
    }

    public static PostGenericStageModel fromActivity(Activity activity) {
        String response = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new PostGenericStageModel(activity, Response.fromJson(response));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator A communicator that can be used to send messages and/or end the communication stream
     * @param response           The deserialised Payment provided as a string via {@link BaseApiService#processRequest(ClientCommunicator, String, String)} method
     * @return An instance of {@link GenericStageModel}
     */
    public static PostGenericStageModel fromService(ClientCommunicator clientCommunicator, Response response) {
        return new PostGenericStageModel(clientCommunicator, response);
    }

    /**
     * Get the response that the main generic application generated.
     *
     * @return The response from the main generic app
     */
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
    public void skip() {
        doSendResponse("{}");
    }

    @Override
    public String getRequestJson() {
        return inputResponse.toJson();
    }
}
