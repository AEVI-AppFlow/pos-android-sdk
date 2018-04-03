package com.aevi.sdk.pos.flow.paymentservicesample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.Switch;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.CardResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.sample.CardProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDetailsActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import butterknife.*;

public class SelectPaymentCardActivity extends AppCompatActivity {

    @BindView(R.id.card_scheme_spinner)
    DropDownSpinner cardSchemeSpinner;

    @BindView(R.id.include_token)
    CheckBox includeTokenView;

    @BindView(R.id.include_cardholder_name)
    CheckBox includeCardholderNameView;

    @BindView(R.id.include_emv_details)
    CheckBox includeEmvDetailsView;

    @BindView(R.id.approve_switch)
    Switch approveSwitch;

    private TransactionRequest transactionRequest;
    private Card card;
    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        ButterKnife.bind(this);
        transactionRequest = TransactionRequest.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        final DropDownHelper dropDownHelper = new DropDownHelper(this);
        dropDownHelper.setupDropDown(cardSchemeSpinner, R.array.card_schemes);
    }

    @OnClick(R.id.show_request)
    public void onShowRequest() {
        Intent intent = new Intent(this, ModelDetailsActivity.class);
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_TYPE, TransactionRequest.class.getName());
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_DATA, transactionRequest.toJson());
        startActivity(intent);
    }

    @OnItemSelected(R.id.card_scheme_spinner)
    public void onCardSchemeChange() {
        updateModel();
    }

    @OnCheckedChanged({R.id.include_token, R.id.include_cardholder_name, R.id.include_emv_details, R.id.approve_switch})
    public void onCheckBoxChange() {
        updateModel();
        cardSchemeSpinner.setEnabled(approveSwitch.isChecked());
        includeTokenView.setEnabled(approveSwitch.isChecked());
        includeCardholderNameView.setEnabled(approveSwitch.isChecked());
        includeEmvDetailsView.setEnabled(approveSwitch.isChecked());
    }

    private void updateModel() {
        buildCard();
        if (modelDisplay != null) {
            if (approveSwitch.isChecked()) {
                modelDisplay.showCardResponse(new CardResponse(card));
            } else {
                modelDisplay.showCardResponse(new CardResponse(CardResponse.Result.DECLINED));
            }
        }
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        if (approveSwitch.isChecked()) {
            sendResponseAndFinish(new CardResponse(card));
        } else {
            sendResponseAndFinish(new CardResponse(CardResponse.Result.DECLINED));
        }
    }

    private void sendResponseAndFinish(CardResponse cardResponse) {
        try {
            ObservableActivityHelper<CardResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(cardResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }

    private void buildCard() {
        String cardNetwork = (String) cardSchemeSpinner.getSelectedItem();
        boolean includeToken = includeTokenView.isChecked();
        boolean includeCardholder = includeCardholderNameView.isChecked();
        boolean includeEmvDetails = includeEmvDetailsView.isChecked();

        this.card = CardProducer.buildCard(cardNetwork, includeToken, includeCardholder, includeEmvDetails);
    }
}
