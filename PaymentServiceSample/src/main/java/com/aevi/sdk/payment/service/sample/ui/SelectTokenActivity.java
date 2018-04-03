package com.aevi.sdk.payment.service.sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.payment.service.sample.R;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.ui.library.recycler.DropDownSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTokenActivity extends AppCompatActivity {

    @BindView(R.id.card_scheme_spinner)
    DropDownSpinner cardSchemeSpinner;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_token);
        ButterKnife.bind(this);
        request = Request.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
    }

    @OnClick(R.id.reply_with_token)
    public void onRespondWitToken() {
        sendResponseAndFinish(CustomerProducer.CUSTOMER_TOKEN);
    }

    @OnClick(R.id.reply_no_token)
    public void onRespondWithFailure() {
        sendResponseAndFinish(null);
    }

    private void sendResponseAndFinish(Token token) {
        try {
            ObservableActivityHelper<Response> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            Response response = new Response(request, token != null, token != null ? "Sample token generated" : "Failed to generate sample token");
            response.addAdditionalData(AdditionalDataKeys.DATA_KEY_TOKEN, token);
            activityHelper.publishResponse(response);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
