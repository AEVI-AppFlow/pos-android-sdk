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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.aevi.sdk.flow.model.FlowException;
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
import io.reactivex.disposables.Disposable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

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
    private Disposable initiateDisposable;

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
                .subscribe(paymentSettings -> {
                    List<String> flowTypes = paymentSettings.getFlowConfigurations().getFlowTypes(FlowConfig.REQUEST_CLASS_PAYMENT);
                    if (paymentSettings.getPaymentFlowServices().getNumberOfFlowServices() == 0 || flowTypes.isEmpty()) {
                        handleNoPaymentServices();
                        return;
                    }
                    this.paymentSettings = paymentSettings;
                    allFieldsReady = true;
                    dropDownHelper
                            .setupDropDown(currencySpinner, new ArrayList<>(paymentSettings.getPaymentFlowServices().getAllSupportedCurrencies()),
                                           false);
                    dropDownHelper.setupDropDown(flowSpinner, flowTypes, false);
                }, throwable -> {
                    if (throwable instanceof FlowException) {
                        Intent errorIntent = new Intent(getContext(), PaymentResultActivity.class);
                        errorIntent.putExtra(PaymentResultActivity.ERROR_KEY, ((FlowException) throwable).toJson());
                        startActivity(errorIntent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Unrecoverable error occurred - see logs", Toast.LENGTH_SHORT).show();
                        Log.e(PaymentFragment.class.getSimpleName(), "Error", throwable);
                        getActivity().finish();
                    }
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
            String flowType = ((String) flowSpinner.getSelectedItem());
            String flowName = paymentSettings.getFlowConfigurations().getFlowNamesForType(flowType).get(0);
            PaymentFlowServices servicesForFlow = paymentSettings.getServicesForFlow(flowName);
            if (servicesForFlow.getAllSupportedCurrencies().isEmpty()) {
                servicesForFlow = paymentSettings.getPaymentFlowServices();
            }
            dropDownHelper.setupDropDown(currencySpinner, new ArrayList<>(servicesForFlow.getAllSupportedCurrencies()), false);
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
        String flowType = ((String) flowSpinner.getSelectedItem());
        if (flowType == null) {
            return; // Not ready yet
        }
        paymentBuilder.withPaymentFlow(flowType); // Note, for production, the overloaded method that also takes a flow name should be used!
        Amounts amounts;
        if (!addBasketBox.isChecked()) {
            amountSpinner.setEnabled(true);
            amounts = getManualAmounts();
            paymentBuilder.withBasket(null);
        } else {
            amountSpinner.setEnabled(false);
            Basket basket = createBasket();
            paymentBuilder.withBasket(basket);
            amounts = new Amounts(basket.getTotalBasketValue(), (String) currencySpinner.getSelectedItem());
        }
        paymentBuilder.withAmounts(amounts);

        paymentBuilder.withSplitEnabled(splitBox.isChecked());
        addCardTokenBox.setEnabled(!splitBox.isChecked());

        if (addCustomerBox.isChecked()) {
            paymentBuilder.withCustomer(CustomerProducer.getDefaultCustomer("Payment Initiation Sample"));
        } else {
            paymentBuilder.withCustomer(null);
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
        String currency = (String) currencySpinner.getSelectedItem();
        long baseAmount = formattedAmountToLong(amountValues[0].trim(), currency);
        Amounts amounts = new Amounts(baseAmount, currency);
        if (amountValues.length > 1) {
            for (int i = 1; i < amountValues.length; i++) {
                String[] additionalAmount = amountValues[i].split(":");
                amounts.addAdditionalAmount(additionalAmount[0].trim(), formattedAmountToLong(additionalAmount[1].trim(), currency));
            }
        }
        return amounts;
    }

    // Different currencies have different unit fractions - see docs for more details
    public static long formattedAmountToLong(String formattedAmount, String currencyCode) {
        BigDecimal bd = new BigDecimal(formattedAmount);
        int subUnitFraction = 2;
        try {
            Currency currency = Currency.getInstance(currencyCode);
            subUnitFraction = currency.getDefaultFractionDigits();
        } catch (Exception e) {
            // Ignore
        }
        return bd.movePointRight(subUnitFraction).longValue();
    }

    private Basket createBasket() {
        return new Basket("sampleBasket",
                          // You can add single count items, with label, category and amount value
                          new BasketItemBuilder().withLabel("Flat White").withCategory("coffee").withAmount(250).build(),
                          new BasketItemBuilder().withLabel("Extra shot").withCategory("coffee").withAmount(50).build(),
                          new BasketItemBuilder().withLabel("Water").withCategory("drinks").withAmount(150).build(),
                          // You can also specify the initial count of the item and provide your own id
                          new BasketItemBuilder().withId("1234-abcd").withLabel("Chocolate Cake").withCategory("cake").withAmount(250).withQuantity(2)
                                  .build());
    }

    @OnClick(R.id.send)
    public void onSend() {
        PaymentClient paymentClient = PaymentApi.getPaymentClient(getContext());
        // Responses come in via PaymentResponseListenerService, and will launch PaymentResultActivity from there
        paymentClient.initiatePayment(paymentBuilder.build())
                .subscribe(() -> {
                               Log.i(PaymentFragment.class.getSimpleName(), "FPS accepted Payment request");
                               getActivity().finish();
                           },
                           throwable -> {
                               final Intent intent = new Intent(getContext(), PaymentResultActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                       Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                               if (throwable instanceof FlowException) {
                                   intent.putExtra(PaymentResultActivity.ERROR_KEY, ((FlowException) throwable).toJson());
                               } else {
                                   intent.putExtra(PaymentResultActivity.ERROR_KEY, new FlowException("Error", throwable.getMessage()).toJson());
                               }
                               if (isAdded()) {
                                   startActivity(intent);
                               }
                           });
    }

    public Payment getPayment() {
        return paymentBuilder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (initiateDisposable != null) {
            initiateDisposable.dispose();
            initiateDisposable = null;
        }
    }
}
