package com.aevi.sdk.pos.flow.sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.R;

public class ModelDetailsActivity extends AppCompatActivity {

    public static final String KEY_MODEL_DATA = "modelData";
    public static final String KEY_MODEL_TYPE = "modelType";

    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_model_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);

        String modelType = getIntent().getStringExtra(KEY_MODEL_TYPE);
        String data = getIntent().getStringExtra(KEY_MODEL_DATA);

        if (modelType.equals(Payment.class.getName())) {
            modelDisplay.showPayment(Payment.fromJson(data));
        } else if (modelType.equals(TransactionRequest.class.getName())) {
            modelDisplay.showTransactionRequest(TransactionRequest.fromJson(data));
        } else if (modelType.equals(TransactionSummary.class.getName())) {
            modelDisplay.showTransactionSummary(TransactionSummary.fromJson(data));
        } else if (modelType.equals(SplitRequest.class.getName())) {
            modelDisplay.showSplitRequest(SplitRequest.fromJson(data));
        } else if (modelType.equals(PaymentResponse.class.getName())) {
            modelDisplay.showPaymentResponse(PaymentResponse.fromJson(data));
        } else if (modelType.equals(Response.class.getName())) {
            modelDisplay.showResponse(Response.fromJson(data));
        } else if (modelType.equals(Request.class.getName())) {
            modelDisplay.showRequest(Request.fromJson(data));
        } else {
            throw new IllegalArgumentException("Unknown type: " + modelType);
        }
    }
}
