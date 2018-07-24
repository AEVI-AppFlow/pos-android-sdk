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

package com.aevi.sdk.pos.flow.paymentservicesample.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.Switch;

import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.CardResponse;
import com.aevi.sdk.pos.flow.model.PaymentStage;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.sample.CardProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import butterknife.*;

public class SelectPaymentCardActivity extends BaseSampleAppCompatActivity<CardResponse> {

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        final DropDownHelper dropDownHelper = new DropDownHelper(this);
        dropDownHelper.setupDropDown(cardSchemeSpinner, R.array.card_schemes);
        registerForActivityEvents();
        setupToolbar(toolbar, R.string.pss_card_reading);
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return PaymentStage.PAYMENT_CARD_READING.name();
    }

    @Override
    protected Class<?> getRequestClass() {
        return TransactionRequest.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return CardResponse.class;
    }

    @Override
    protected String getModelJson() {
        return new CardResponse(card).toJson();
    }

    @Override
    protected String getRequestJson() {
        return transactionRequest.toJson();
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

    private void buildCard() {
        String cardNetwork = (String) cardSchemeSpinner.getSelectedItem();
        boolean includeToken = includeTokenView.isChecked();
        boolean includeCardholder = includeCardholderNameView.isChecked();
        boolean includeEmvDetails = includeEmvDetailsView.isChecked();

        this.card = CardProducer.buildCard(cardNetwork, includeToken, includeCardholder, includeEmvDetails);
    }
}
