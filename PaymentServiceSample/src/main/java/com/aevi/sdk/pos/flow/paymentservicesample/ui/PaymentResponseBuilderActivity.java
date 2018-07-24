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
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.paymentservicesample.util.IdProvider;
import com.aevi.sdk.pos.flow.paymentservicesample.util.InMemoryStore;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.CardProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.*;

import static com.aevi.sdk.flow.constants.ReferenceKeys.*;

public class PaymentResponseBuilderActivity extends BaseSampleAppCompatActivity<TransactionResponse> {

    private static final String APPROVED_RESP_CODE = "00";
    private static final String DECLINED_RESP_CODE = "XX";

    private static final String INTERNAL_ID_KEY = "sampleTransactionReference";

    @BindView(R.id.processed_amounts_spinner)
    DropDownSpinner processedAmountsSpinner;

    @BindView(R.id.payment_methods_spinner)
    DropDownSpinner paymentMethodsSpinner;

    @BindView(R.id.include_card)
    CheckBox includeCardView;

    @BindView(R.id.approve_switch)
    Switch approveSwitch;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<Long> processedAmountsOptions = new ArrayList<>();
    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;
    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_response_builder);
        ButterKnife.bind(this);
        transactionRequest = TransactionRequest.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));

        final DropDownHelper dropDownHelper = new DropDownHelper(this);
        long totalAmount = transactionRequest.getAmounts().getTotalAmountValue();
        processedAmountsOptions.addAll(Arrays.asList(0L, totalAmount / 2L, totalAmount, (long) (totalAmount * 1.5)));
        List<String> formattedAmountOptions = new ArrayList<>();
        for (Long processedAmountsOption : processedAmountsOptions) {
            formattedAmountOptions
                    .add(formatAmount(processedAmountsOption) + " (" + (int) ((processedAmountsOption / (double) totalAmount) * 100) + "%)");
        }
        dropDownHelper.setupDropDown(processedAmountsSpinner, formattedAmountOptions, false);
        processedAmountsSpinner.setSelection(2);

        dropDownHelper.setupDropDown(paymentMethodsSpinner, R.array.payment_methods);
        registerForActivityEvents();
        setupToolbar(toolbar, R.string.pss_txn_processing);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildTransactionResponse();
    }

    public String formatAmount(long amount) {
        return AmountFormatter.formatAmount(transactionRequest.getAmounts().getCurrency(), amount);
    }

    @OnCheckedChanged({R.id.approve_switch, R.id.include_card})
    public void onCheckboxChange() {
        buildTransactionResponse();
        processedAmountsSpinner.setEnabled(approveSwitch.isChecked());
        paymentMethodsSpinner.setEnabled(approveSwitch.isChecked());
        includeCardView.setEnabled(approveSwitch.isChecked());
    }

    @OnItemSelected({R.id.processed_amounts_spinner, R.id.payment_methods_spinner})
    public void onSpinnerChange() {
        buildTransactionResponse();
    }

    private void buildTransactionResponse() {
        if (approveSwitch.isChecked()) {
            transactionResponse = new TransactionResponseBuilder(transactionRequest.getId())
                    .approve(getProcessedAmounts(), (String) paymentMethodsSpinner.getSelectedItem())
                    .withOutcomeMessage("User approved manually")
                    .withResponseCode(APPROVED_RESP_CODE)
                    .withCard(getCard())
                    .withReference(INTERNAL_ID_KEY, UUID.randomUUID().toString())
                    .withReference(MERCHANT_ID, IdProvider.getMerchantId())
                    .withReference(MERCHANT_NAME, IdProvider.getMerchantName())
                    .withReference(TERMINAL_ID, IdProvider.getTerminalId())
                    .withReference(TRANSACTION_DATE_TIME, String.valueOf(System.currentTimeMillis()))
                    .build();
        } else {
            transactionResponse = new TransactionResponseBuilder(transactionRequest.getId())
                    .decline("Used declined manually")
                    .withResponseCode(DECLINED_RESP_CODE)
                    .build();
        }
        if (modelDisplay != null) {
            modelDisplay.showTransactionResponse(transactionResponse);
        }
    }

    @OnClick(R.id.send_response)
    public void onApprove() {
        sendResponseAndFinish(transactionResponse);
    }

    private Amounts getProcessedAmounts() {
        int chosenSelection = processedAmountsSpinner.getSelectedItemPosition();
        long amounts = processedAmountsOptions.get(chosenSelection);
        return new Amounts(amounts, transactionRequest.getAmounts().getCurrency());
    }

    private Card getCard() {
        if (!includeCardView.isChecked()) {
            return null;
        }

        // If the card details passed on via the request is from our app in the card reading step, we use that again
        if (CardProducer.cardWasProducedHere(transactionRequest.getCard())) {
            return transactionRequest.getCard();
        }
        // If not, let's just return the default card
        return CardProducer.getDefaultCard();
    }

    @Override
    protected void sendResponseAndFinish(TransactionResponse response) {
        super.sendResponseAndFinish(response);
        InMemoryStore.getInstance().setLastTransactionResponseGenerated(transactionResponse);
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return PaymentStage.TRANSACTION_PROCESSING.name();
    }

    @Override
    protected Class<?> getRequestClass() {
        return TransactionRequest.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return TransactionResponse.class;
    }

    @Override
    protected String getModelJson() {
        return transactionResponse.toJson();
    }

    @Override
    protected String getRequestJson() {
        return transactionRequest.toJson();
    }
}
