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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentBuilder;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.PreFlowModel;

public class PreFlowActivity extends BaseSampleAppCompatActivity {

    private static final long AMOUNT = 1000;

    private PreFlowModel preFlowModel;
    private PaymentBuilder paymentBuilder;
    private ModelDisplay modelDisplay;
    private String chosenCurrency;

    @BindView(R.id.set_amounts)
    Button setAmounts;

    @BindView(R.id.enable_split)
    CheckBox splitCheckBox;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_flow);
        ButterKnife.bind(this);

        PaymentClient paymentClient = PaymentApi.getPaymentClient(this);
        paymentClient.getPaymentSettings().subscribe(paymentSettings -> {
            chosenCurrency = paymentSettings.getPaymentFlowServices().getAllSupportedCurrencies().iterator().next();
            String amountString = AmountFormatter.formatAmount(chosenCurrency, AMOUNT);
            setAmounts.setText("Set amounts to " + amountString);
        }, throwable -> {
            Toast.makeText(PreFlowActivity.this, "Failed to determine currency", Toast.LENGTH_SHORT).show();
            setAmounts.setEnabled(false);
        });

        preFlowModel = PreFlowModel.fromActivity(this);
        paymentBuilder = preFlowModel.getPaymentBuilder();
        setupToolbar(toolbar, R.string.fss_pre_flow);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        splitCheckBox.setChecked(preFlowModel.getPayment().isSplitEnabled());
        subscribeToFlowServiceEvents(preFlowModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateModel();
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showPayment(paymentBuilder.build());
        }
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return FlowStages.PRE_FLOW;
    }

    @Override
    protected Class<?> getRequestClass() {
        return Payment.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return FlowResponse.class;
    }

    @Override
    protected String getModelJson() {
        return paymentBuilder.build().toJson();
    }

    @Override
    protected String getRequestJson() {
        return preFlowModel.getPayment().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.pre_flow_help);
    }

    @OnClick(R.id.set_amounts)
    public void onSetAmounts() {
        paymentBuilder.withAmounts(new Amounts(AMOUNT, chosenCurrency));
        setAmounts.setEnabled(false);
        updateModel();
    }

    @OnCheckedChanged(R.id.enable_split)
    public void onEnableSplit(CheckBox split) {
        paymentBuilder.withSplitEnabled(split.isEnabled());
        updateModel();
    }

    @OnClick(R.id.cancel_transaction)
    public void onCancel() {
        preFlowModel.cancelFlow();
        finish();
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        preFlowModel.sendResponse();
        finish();
    }
}
