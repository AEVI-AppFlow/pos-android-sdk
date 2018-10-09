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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.aevi.android.rxmessenger.MessageException;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentInitiationActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.BaseObservableFragment;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class PaymentFragment extends BaseObservableFragment {

    @BindView(R.id.flow_spinner)
    DropDownSpinner flowSpinner;

    @BindView(R.id.currency_spinner)
    DropDownSpinner currencySpinner;

    @BindView(R.id.amounts_spinner)
    DropDownSpinner amountSpinner;

    @BindView(R.id.enable_split)
    CheckBox splitBox;

    @BindView(R.id.add_basket)
    CheckBox addBasketBox;

    @BindView(R.id.add_customer)
    CheckBox addCustomerBox;

    @BindView(R.id.add_card_token)
    CheckBox addCardTokenBox;

    @BindView(R.id.no_payment_services)
    TextView noPaymentServices;

    @BindView(R.id.send)
    Button send;

    private PaymentBuilder paymentBuilder = new PaymentBuilder();
    private boolean allFieldsReady;
    private ModelDisplay modelDisplay;
    private DropDownHelper dropDownHelper;
    private PaymentSettings paymentSettings;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_payment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        modelDisplay = ((PaymentInitiationActivity) getActivity()).getModelDisplay();

        dropDownHelper = new DropDownHelper(getActivity());
        dropDownHelper.setupDropDown(amountSpinner, R.array.amounts);
        PaymentClient paymentClient = SampleContext.getInstance(getContext()).getPaymentClient();

        paymentClient.getPaymentSettings()
                .flatMap((Function<PaymentSettings, SingleSource<List<String>>>) paymentSettings -> {
                    if (paymentSettings.getPaymentFlowServices().getNumberOfFlowServices() == 0) {
                        throw new Exception("No services available");
                    }
                    this.paymentSettings = paymentSettings;
                    return paymentSettings.getFlowConfigurations().stream()
                            .filter(flowConfig -> flowConfig.getRequestClass().equals(FlowConfig.REQUEST_CLASS_PAYMENT))
                            .map(FlowConfig::getName)
                            .toList();
                })
                .subscribe(paymentFlowNames -> {
                    allFieldsReady = true;
                    dropDownHelper.setupDropDown(currencySpinner, new ArrayList<>(paymentSettings.getPaymentFlowServices().getAllSupportedCurrencies()), false);
                    dropDownHelper.setupDropDown(flowSpinner, paymentFlowNames, false);
                }, throwable -> {
                    if (throwable instanceof IllegalStateException) {
                        Toast.makeText(getContext(), "FPS is not installed on the device", Toast.LENGTH_SHORT).show();
                    }
                    handleNoPaymentServices();
                });
    }

    private void handleNoPaymentServices() {
        noPaymentServices.setVisibility(View.VISIBLE);
        flowSpinner.setEnabled(false);
        currencySpinner.setEnabled(false);
        addBasketBox.setEnabled(false);
        amountSpinner.setEnabled(false);
        addCustomerBox.setEnabled(false);
        addCardTokenBox.setEnabled(false);
        splitBox.setEnabled(false);
        send.setEnabled(false);
    }

    @OnItemSelected({R.id.flow_spinner})
    public void onFlowSpinnerSelection() {
        if (allFieldsReady) {
            // This will trigger an update to all fields as per below
            String flowName = ((String) flowSpinner.getSelectedItem());
            dropDownHelper.setupDropDown(currencySpinner, new ArrayList<>(paymentSettings.getServicesForFlow(flowName).getAllSupportedCurrencies()), false);
        }
    }

    @OnItemSelected({R.id.currency_spinner, R.id.amounts_spinner})
    public void onSpinnerSelection() {
        if (allFieldsReady) {
            readAllFields();
        }
    }

    @OnCheckedChanged({R.id.enable_split, R.id.add_basket, R.id.add_customer, R.id.add_card_token})
    public void onCheckboxValueChanged() {
        readAllFields();
    }

    private void readAllFields() {
        String flowName = ((String) flowSpinner.getSelectedItem());
        paymentBuilder.withPaymentFlow(flowName);
        Amounts amounts;
        if (!addBasketBox.isChecked()) {
            amountSpinner.setEnabled(true);
            amounts = getManualAmounts();
            paymentBuilder.getCurrentAdditionalData().removeData(AdditionalDataKeys.DATA_KEY_BASKET);
        } else {
            amountSpinner.setEnabled(false);
            Basket basket = createBasket();
            paymentBuilder.addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, basket);
            amounts = new Amounts(basket.getTotalBasketValue(), (String) currencySpinner.getSelectedItem());
        }
        paymentBuilder.withAmounts(amounts);

        paymentBuilder.withSplitEnabled(splitBox.isChecked());
        addCardTokenBox.setEnabled(!splitBox.isChecked());

        if (addCustomerBox.isChecked()) {
            paymentBuilder.addAdditionalData(AdditionalDataKeys.DATA_KEY_CUSTOMER, CustomerProducer.getDefaultCustomer("Payment Initiation Sample"));
        } else {
            paymentBuilder.getCurrentAdditionalData().removeData(AdditionalDataKeys.DATA_KEY_CUSTOMER);
        }

        if (addCardTokenBox.isChecked()) {
            paymentBuilder.withCardToken(CustomerProducer.CUSTOMER_TOKEN);
            splitBox.setEnabled(false);
        } else {
            paymentBuilder.withCardToken(null);
            splitBox.setEnabled(true);
        }

        if (modelDisplay != null) {
            modelDisplay.showPayment(paymentBuilder.build());
        }
    }

    private Amounts getManualAmounts() {
        String amountChoice = (String) amountSpinner.getSelectedItem();
        String[] amountValues = amountChoice.split(",");
        long baseAmount = formattedAmountToLong(amountValues[0].trim());
        Amounts amounts = new Amounts(baseAmount, (String) currencySpinner.getSelectedItem());
        if (amountValues.length > 1) {
            for (int i = 1; i < amountValues.length; i++) {
                String[] additionalAmount = amountValues[i].split(":");
                amounts.addAdditionalAmount(additionalAmount[0].trim(), formattedAmountToLong(additionalAmount[1].trim()));
            }
        }
        return amounts;
    }

    public static long formattedAmountToLong(String formattedAmount) {
        return Math.round(new BigDecimal(formattedAmount).doubleValue() * 100);
    }

    private Basket createBasket() {
        return new Basket(
                // You can add single count items, with label, category and amount value
                new BasketItemBuilder().withLabel("Flat White").withCategory("coffee").withAmount(250).build(),
                new BasketItemBuilder().withLabel("Water").withCategory("drinks").withAmount(150).build(),
                // You can also specify the initial count of the item and provide your own id
                new BasketItemBuilder().withId("1234-abcd").withLabel("Chocolate Cake").withCategory("cake").withAmount(250).withQuantity(3).build());
    }

    @OnClick(R.id.send)
    public void onSend() {
        PaymentClient paymentClient = PaymentApi.getPaymentClient(getContext());
        final Intent intent = new Intent(getContext(), PaymentResultActivity.class);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        paymentClient.initiatePayment(paymentBuilder.build()).subscribe(response -> {
            SampleContext.getInstance(getContext()).setLastReceivedPaymentResponse(response);
            if (isAdded()) {
                intent.putExtra(PaymentResultActivity.PAYMENT_RESPONSE_KEY, response.toJson());
                startActivity(intent);
            }
        }, throwable -> {
            if (throwable instanceof MessageException) {
                intent.putExtra(PaymentResultActivity.ERROR_KEY, ((MessageException) throwable).toJson());
            } else if (throwable instanceof IllegalStateException) {
                intent.putExtra(PaymentResultActivity.ERROR_KEY, new MessageException("Error", "FPS not installed").toJson());
            } else {
                intent.putExtra(PaymentResultActivity.ERROR_KEY, new MessageException("Error", throwable.getMessage()).toJson());
            }
            if (isAdded()) {
                startActivity(intent);
            }
        });
    }

    public Payment getPayment() {
        return paymentBuilder.build();
    }

}
