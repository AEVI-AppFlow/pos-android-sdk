package com.aevi.sdk.payment.service.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.Switch;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.payment.service.sample.R;
import com.aevi.sdk.payment.service.sample.util.InMemoryStore;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.CardProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDetailsActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.*;

public class PaymentResponseBuilderActivity extends AppCompatActivity {

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
            formattedAmountOptions.add(formatAmount(processedAmountsOption) + " (" + (int) ((processedAmountsOption / (double) totalAmount) * 100) + "%)");
        }
        dropDownHelper.setupDropDown(processedAmountsSpinner, formattedAmountOptions, false);
        processedAmountsSpinner.setSelection(2);

        dropDownHelper.setupDropDown(paymentMethodsSpinner, R.array.payment_methods);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        buildTransactionResponse();
    }

    @OnClick(R.id.show_request)
    public void onShowRequest() {
        Intent intent = new Intent(this, ModelDetailsActivity.class);
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_TYPE, TransactionRequest.class.getName());
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_DATA, transactionRequest.toJson());
        startActivity(intent);
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
                    .withOutcome(TransactionResponse.Outcome.APPROVED)
                    .withOutcomeMessage("User approved manually")
                    .withResponseCode(APPROVED_RESP_CODE)
                    .withPaymentMethod((String) paymentMethodsSpinner.getSelectedItem())
                    .withCard(getCard())
                    .withAmountsProcessed(getProcessedAmounts())
                    .withReference(INTERNAL_ID_KEY, UUID.randomUUID().toString())
                    .build();
        } else {
            transactionResponse = new TransactionResponseBuilder(transactionRequest.getId())
                    .withOutcome(TransactionResponse.Outcome.DECLINED)
                    .withOutcomeMessage("Used declined manually")
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

    private void sendResponseAndFinish(TransactionResponse transactionResponse) {
        try {
            ObservableActivityHelper<TransactionResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            InMemoryStore.getInstance().setLastTransactionResponseGenerated(transactionResponse);
            activityHelper.publishResponse(transactionResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
