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

package com.aevi.sdk.pos.flow.flowservicesample.service;

import android.util.Log;
import com.aevi.sdk.flow.constants.FlowTypes;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseGenericService;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.flowservicesample.ui.LoyaltyBalanceActivity;
import com.aevi.sdk.pos.flow.flowservicesample.ui.ReceiptDeliveryActivity;

/**
 * Illustrates how to implement a service to handle a bespoke request type.
 */
public class GenericRequestService extends BaseGenericService {

    private static final String TAG = GenericRequestService.class.getSimpleName();
    public static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";

    @Override
    protected void processRequest(GenericStageModel stageModel) {
        Request request = stageModel.getRequest();
        Log.d(TAG, "Got generic request: " + request.toJson());

        switch (request.getRequestType()) {
            case SHOW_LOYALTY_POINTS_REQUEST:
                stageModel.processInActivity(getBaseContext(), LoyaltyBalanceActivity.class);
                break;
            case FlowTypes.FLOW_TYPE_RECEIPT_DELIVERY:
                stageModel.processInActivity(getBaseContext(), ReceiptDeliveryActivity.class);
                break;
            default:
                stageModel.sendResponse(new Response(request, false, "Unsupported request type"));
                break;

        }
    }
}
