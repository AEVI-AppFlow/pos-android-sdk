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

package com.aevi.sdk.pos.flow.stage;

import android.app.Activity;
import android.content.Context;

import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the post-flow stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 */
public class PostFlowModel extends BaseStageModel {

    private final PaymentResponse paymentResponse;

    private PostFlowModel(Activity activity, PaymentResponse paymentResponse) {
        super(activity);
        this.paymentResponse = paymentResponse;
    }

    private PostFlowModel(ClientCommunicator clientCommunicator, PaymentResponse paymentResponse) {
        super(clientCommunicator);
        this.paymentResponse = paymentResponse;
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PostFlowModel}
     */
    public static PostFlowModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new PostFlowModel(activity, PaymentResponse.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request         The deserialised Payment provided as a string via {@link BaseApiService#processRequest(ClientCommunicator, String, String)}
     * @return An instance of {@link PostFlowModel}
     */
    public static PostFlowModel fromService(ClientCommunicator clientCommunicator, PaymentResponse request) {
        return new PostFlowModel(clientCommunicator, request);
    }

    /**
     * Get the payment response.
     *
     * @return The payment response.
     */
    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }

    /**
     * Call this from a service as soon as possible to indicate that the service will handle the request asynchronously / in the background.
     *
     * Typical use cases for this is analytics, reporting, etc that do not launch any UI and hence do not need to block the flow from finishing.
     */
    public void processInBackground() {
        clientCommunicator.notifyBackgroundProcessing();
    }

    /**
     * Call when finished processing.
     */
    public void sendResponse() {
        doSendResponse(new NoOpModel().toJson());
    }

    @Override
    public String getRequestJson() {
        return paymentResponse.toJson();
    }

}
