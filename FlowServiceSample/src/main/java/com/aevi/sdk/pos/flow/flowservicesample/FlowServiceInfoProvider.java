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

package com.aevi.sdk.pos.flow.flowservicesample;


import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.constants.TransactionTypes;
import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.pos.flow.flowservicesample.service.GenericRequestService;
import com.aevi.sdk.pos.flow.flowservicesample.settings.ServiceStateHandler;
import com.aevi.sdk.pos.flow.model.FlowServiceInfoBuilder;
import com.aevi.sdk.pos.flow.model.PaymentStage;
import com.aevi.sdk.pos.flow.service.BaseFlowServiceInfoProvider;

import java.util.ArrayList;
import java.util.List;

import static com.aevi.sdk.pos.flow.model.PaymentStage.*;

public class FlowServiceInfoProvider extends BaseFlowServiceInfoProvider {

    @Override
    protected FlowServiceInfo getFlowServiceInfo() {
        return new FlowServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Flow Service Sample")
                .withCanAdjustAmounts(true)
                .withCanPayAmounts(true, PaymentMethods.LOYALTY_POINTS, PaymentMethods.GIFT_CARD, PaymentMethods.CASH)
                .withSupportedTransactionTypes(TransactionTypes.SALE)
                .withSupportedRequestTypes(FinancialRequestTypes.PAYMENT, GenericRequestService.SHOW_LOYALTY_POINTS_REQUEST)
                .build(getContext());
    }

    private String[] getEnabledStages() {
        List<String> stages = addEnabledStages(PRE_FLOW, SPLIT, PRE_TRANSACTION, POST_CARD_READING, POST_TRANSACTION, POST_FLOW);
        String[] stagesArray = new String[stages.size()];
        return stages.toArray(stagesArray);
    }

    private List<String> addEnabledStages(PaymentStage... paymentStages) {
        List<String> stages = new ArrayList<>();
        for (PaymentStage paymentStage : paymentStages) {
            if (ServiceStateHandler.isStageEnabled(getContext(), paymentStage)) {
                stages.add(paymentStage.name());
            }
        }
        return stages;
    }
}
