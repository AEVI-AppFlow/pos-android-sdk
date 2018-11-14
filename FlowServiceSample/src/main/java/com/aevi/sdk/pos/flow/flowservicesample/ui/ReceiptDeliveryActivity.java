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

package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.constants.FlowTypes;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.Basket;
import com.aevi.sdk.pos.flow.model.Transaction;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_TRANSACTION;
import static com.aevi.sdk.flow.constants.ReceiptKeys.*;

public class ReceiptDeliveryActivity extends BaseSampleAppCompatActivity {

    private GenericStageModel genericStageModel;
    private Request request;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_delivery);
        ButterKnife.bind(this);
        genericStageModel = GenericStageModel.fromActivity(this);
        request = genericStageModel.getRequest();
        setupToolbar(toolbar, R.string.receipt_delivery);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        AdditionalData requestData = request.getRequestData();
        if (requestData.hasData(DATA_KEY_TRANSACTION)) {
            Transaction transaction = requestData.getValue(DATA_KEY_TRANSACTION, Transaction.class);
            if (transaction != null) {
                modelDisplay.showTransactionSummary(new TransactionSummary(transaction, FlowTypes.FLOW_TYPE_RECEIPT_DELIVERY, DeviceAudience.CUSTOMER,
                                                                           transaction.getLastResponse().getCard()));
            } else {
                sendResponseAndFinish(new Response(request, false, "Transaction data missing"));
            }
        } else {

            AdditionalData receiptData = new AdditionalData();
            Amounts amounts = requestData.getValue(RECEIPT_AMOUNTS, Amounts.class);
            String paymentMethod = requestData.getStringValue(RECEIPT_PAYMENT_METHOD);
            String outcome = requestData.getStringValue(RECEIPT_OUTCOME);
            Basket basket = receiptData.getValue(RECEIPT_BASKET, Basket.class);

            receiptData.addData(RECEIPT_AMOUNTS, amounts);
            receiptData.addData(RECEIPT_PAYMENT_METHOD, paymentMethod);
            receiptData.addData(RECEIPT_OUTCOME, outcome);
            if (basket != null) {
                receiptData.addData(RECEIPT_BASKET, basket);
            }
            modelDisplay.showCustomData(receiptData);
            modelDisplay.showTitle(false);
        }
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(new Response(request, true, "Receipt data presented"));
    }

    private void sendResponseAndFinish(Response response) {
        genericStageModel.sendResponse(response);
        finish();
    }

    @Override
    protected boolean showFlowStagesOption() {
        return false;
    }

    @Override
    protected boolean showViewModelOption() {
        return false;
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return "showLoyaltyPointsBalance";
    }

    @Override
    protected Class<?> getRequestClass() {
        return Request.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return Response.class;
    }

    @Override
    protected String getModelJson() {
        return null;
    }

    @Override
    protected String getRequestJson() {
        return request.toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.loyalty_balance_help);
    }
}
