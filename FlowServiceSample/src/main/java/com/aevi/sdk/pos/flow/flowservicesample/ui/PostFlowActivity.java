package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostFlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_flow);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        modelDisplay.showPaymentResponse(PaymentResponse.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY)));
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish();
    }

    private void sendResponseAndFinish() {
        try {
            ObservableActivityHelper<NoOpModel> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(new NoOpModel());
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
