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
import android.view.View;
import android.widget.CheckBox;

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.AmountIdentifiers;
import com.aevi.sdk.flow.constants.CustomerDataKeys;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.PreFlowModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.aevi.sdk.pos.flow.model.AmountsModifier.percentageToFraction;

public class PreFlowActivity extends BaseSampleAppCompatActivity {

    private PreFlowModel preFlowModel;
    private ModelDisplay modelDisplay;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_flow);
        ButterKnife.bind(this);

        preFlowModel = PreFlowModel.fromActivity(this);
        setupToolbar(toolbar, R.string.fss_pre_flow);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateModel();
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showFlowResponse(preFlowModel.getFlowResponse());
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
        return preFlowModel.getFlowResponse().toJson();
    }

    @Override
    protected String getRequestJson() {
        return preFlowModel.getPayment().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.pre_flow_help);
    }

    @OnClick(R.id.add_tax)
    public void onAddTax(View v) {
        int taxPercentage = getResources().getInteger(R.integer.tax_percentage);
        preFlowModel.setAdditionalAmountAsBaseFraction(AmountIdentifiers.AMOUNT_TAX, percentageToFraction(taxPercentage));
        updateModel();
        v.setEnabled(false);
    }

    @OnCheckedChanged(R.id.enable_split)
    public void onEnableSplit(CheckBox split) {
        preFlowModel.setSplitEnabled(split.isChecked());
        updateModel();
    }

    @OnClick(R.id.add_customer_data)
    public void addCustomerData(View v) {
        Customer customer;
        Payment payment = preFlowModel.getPayment();
        if (payment.getAdditionalData().hasData(AdditionalDataKeys.DATA_KEY_CUSTOMER)) {
            customer = payment.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_CUSTOMER, Customer.class);
            customer.addCustomerDetails(CustomerDataKeys.CITY, "New York");
            customer.addCustomerDetails("updatedBy", "PreFlow Sample");
        } else {
            customer = CustomerProducer.getDefaultCustomer("PreFlow Sample");
        }
        preFlowModel.addRequestData(AdditionalDataKeys.DATA_KEY_CUSTOMER, customer);
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        preFlowModel.sendResponse();
        finish();
    }
}
