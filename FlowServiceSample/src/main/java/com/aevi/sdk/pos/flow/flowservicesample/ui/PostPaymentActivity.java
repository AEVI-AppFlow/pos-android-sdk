package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostPaymentActivity extends AppCompatActivity {

    private FlowResponse flowResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_payment);
        ButterKnife.bind(this);
        flowResponse = new FlowResponse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        modelDisplay.showTransactionSummary(TransactionSummary.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY)));
    }

    @OnClick(R.id.add_payment_references)
    public void onAddPaymentReferences() {
        AdditionalData paymentRefs = new AdditionalData();
        paymentRefs.addData("someReference", "addedByPostPaymentSample");
        flowResponse.setPaymentReferences(paymentRefs);
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(flowResponse);
    }

    private void sendResponseAndFinish(FlowResponse flowResponse) {
        try {
            ObservableActivityHelper<FlowResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(flowResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
