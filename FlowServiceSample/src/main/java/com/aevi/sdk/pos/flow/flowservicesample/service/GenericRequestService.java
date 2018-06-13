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

import com.aevi.sdk.pos.flow.flowservicesample.ui.LoyaltyBalanceActivity;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseRequestService;

public class GenericRequestService extends BaseRequestService {

    public static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";

    @Override
    protected void processRequest(String clientMessageId, Request request) {
        if (request.getRequestType().equals(SHOW_LOYALTY_POINTS_REQUEST)) {
            launchActivity(LoyaltyBalanceActivity.class, clientMessageId, request);
        } else {
            finishWithResponse(clientMessageId, new Response(request, false, "Unsupported request type"));
        }
    }

    @Override
    protected void finish(String clientMessageId) {

    }
}
