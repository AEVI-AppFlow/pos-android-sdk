package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.AmountIdentifiers;
import com.aevi.sdk.flow.constants.CustomerDataKeys;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.AmountsModifier;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDetailsActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.aevi.sdk.pos.flow.model.AmountsModifier.percentageToFraction;

public class PreFlowActivity extends AppCompatActivity {

    private Payment payment;
    private FlowResponse flowResponse;
    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_flow);
        ButterKnife.bind(this);

        payment = Payment.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        flowResponse = new FlowResponse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        updateModel();
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showFlowResponse(flowResponse);
        }
    }

    @OnClick(R.id.show_request)
    public void onShowRequest() {
        Intent intent = new Intent(this, ModelDetailsActivity.class);
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_TYPE, Payment.class.getName());
        intent.putExtra(ModelDetailsActivity.KEY_TITLE, "Payment data");
        intent.putExtra(ModelDetailsActivity.KEY_TITLE_BG, getResources().getColor(R.color.colorPrimary));
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_DATA, payment.toJson());
        startActivity(intent);
    }

    @OnClick(R.id.add_tax)
    public void onAddTax(View v) {
        int taxPercentage = getResources().getInteger(R.integer.tax_percentage);
        AmountsModifier amountsModifier = new AmountsModifier(payment.getAmounts());
        amountsModifier.setAdditionalAmountAsBaseFraction(AmountIdentifiers.AMOUNT_TAX, percentageToFraction(taxPercentage));
        flowResponse.updateRequestAmounts(amountsModifier.build());
        updateModel();
        v.setEnabled(false);
    }

    @OnCheckedChanged(R.id.enable_split)
    public void onEnableSplit(CheckBox split) {
        flowResponse.setEnableSplit(split.isChecked());
        updateModel();
    }

    @OnClick(R.id.add_customer_data)
    public void addCustomerData(View v) {
        Customer customer;
        if (payment.getAdditionalData().hasData(AdditionalDataKeys.DATA_KEY_CUSTOMER)) {
            customer = payment.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_CUSTOMER, Customer.class);
            customer.addCustomerDetails(CustomerDataKeys.CITY, "New York");
            customer.addCustomerDetails("updatedBy", "PreFlow Sample");
        } else {
            customer = CustomerProducer.getDefaultCustomer("PreFlow Sample");
        }
        flowResponse.addAdditionalRequestData(AdditionalDataKeys.DATA_KEY_CUSTOMER, customer);
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        // Lastly, as we were started via launchActivity() in the API, we pass back the response to the service in the manner below
        try {
            ObservableActivityHelper<FlowResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(flowResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }

        // Always remember to finish the activity after sending the response!
        finish();
    }
}
