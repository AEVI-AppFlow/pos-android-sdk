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

package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.constants.AmountIdentifiers;
import com.aevi.sdk.flow.constants.CustomerDataKeys;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.PreTransactionModel;
import com.aevi.sdk.pos.flow.stage.StageModelHelper;

import java.util.Arrays;
import java.util.List;

import static com.aevi.sdk.flow.constants.PaymentMethods.*;
import static com.aevi.sdk.flow.model.AuditEntry.AuditSeverity.INFO;
import static com.aevi.sdk.pos.flow.model.AmountsModifier.percentageToFraction;

abstract class BasePreProcessingActivity extends BaseSampleAppCompatActivity {

    private static final String SAMPLE_POINTS_USED_KEY = "sampleLoyaltyPointsUsed";

    private TransactionRequest transactionRequest;
    private PreTransactionModel preTransactionModel;
    private ModelDisplay modelDisplay;

    @BindViews({R.id.pay_with_points, R.id.pay_with_giftcard, R.id.discount_basket_item})
    List<View> payViews;

    @BindView(R.id.add_surcharge)
    TextView surchargeView;

    @BindView(R.id.pay_with_giftcard)
    TextView giftCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_txn);
        ButterKnife.bind(this);

        preTransactionModel = PreTransactionModel.fromActivity(this);
        transactionRequest = preTransactionModel.getTransactionRequest();

        surchargeView.setText(getString(R.string.add_surcharge_fee,
                                        AmountFormatter.formatAmount(transactionRequest.getAmounts().getCurrency(), getResources()
                                                .getInteger(R.integer.surcharge_fee))));
        long giftCardValue = getResources().getInteger(R.integer.pay_gift_card_value);
        if (transactionRequest.getAmounts().getBaseAmountValue() < giftCardValue) {
            giftCardValue = transactionRequest.getAmounts().getBaseAmountValue();
        }
        giftCardView.setText(getString(R.string.pay_portion_with_gift_card,
                                       AmountFormatter.formatAmount(transactionRequest.getAmounts().getCurrency(), giftCardValue)));
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        if (transactionRequest.getBaskets().isEmpty()) {
            findViewById(R.id.discount_basket_item).setVisibility(View.GONE);
        }
        if (transactionRequest.getAmounts() == null || transactionRequest.getAmounts().getBaseAmountValue() == 0) {
            disablePayViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateModel();
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showFlowResponse(StageModelHelper.getFlowResponse(preTransactionModel));
        }
    }

    @OnClick(R.id.add_surcharge)
    public void onAddSurcharge() {
        int surchargeFree = getResources().getInteger(R.integer.surcharge_fee);
        preTransactionModel.setAdditionalAmount(AmountIdentifiers.AMOUNT_SURCHARGE, surchargeFree);
        updateModel();
        surchargeView.setEnabled(false);
    }

    @OnClick(R.id.add_charity)
    public void onAddCharity(View v) {
        int charityPercentage = getResources().getInteger(R.integer.charity_percentage);
        preTransactionModel.setAdditionalAmountAsBaseFraction(AmountIdentifiers.AMOUNT_CHARITY_DONATION, percentageToFraction(charityPercentage));
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.pay_with_points)
    public void onPayWithPoints() {
        // We pretend 1 point == 1 subunit
        long pointsValue = getRandomPointsValue();

        preTransactionModel
                .setAmountsPaid(new Amounts(pointsValue, transactionRequest.getAmounts().getCurrency()), PAYMENT_METHOD_LOYALTY_POINTS);
        preTransactionModel.addRequestData(SAMPLE_POINTS_USED_KEY, pointsValue);
        disablePayViews();
        updateModel();
    }

    private long getRandomPointsValue() {
        long max = transactionRequest.getAmounts().getBaseAmountValue();
        long min = 1;
        return (long) (Math.random() * ((max - min) + 1)) + min;
    }

    @OnClick(R.id.pay_with_giftcard)
    public void onPayWithGiftCard() {
        long giftCardValue = getResources().getInteger(R.integer.pay_gift_card_value);
        if (transactionRequest.getAmounts().getBaseAmountValue() < giftCardValue) {
            giftCardValue = transactionRequest.getAmounts().getBaseAmountValue();
        }
        preTransactionModel.setAmountsPaid(new Amounts(giftCardValue, transactionRequest.getAmounts().getCurrency()), PAYMENT_METHOD_GIFT_CARD);
        disablePayViews();
        updateModel();
    }

    @OnClick(R.id.add_customer_data)
    public void addCustomerData(View v) {
        Customer customer;
        if (transactionRequest.getCustomer() != null) {
            customer = transactionRequest.getCustomer();
            customer.addCustomerDetails(CustomerDataKeys.CITY, "London");
            customer.addCustomerDetails("updatedBy", "PrePayment Sample");
        } else {
            customer = CustomerProducer.getDefaultCustomer("PrePayment Sample");
        }
        preTransactionModel.addOrUpdateCustomerDetails(customer);
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.add_basket)
    public void addBasketData(View v) {
        Basket basket = new Basket("sampleAdditionalBasket");
        basket.addItems(new BasketItemBuilder().withLabel("flowItem").withAmount(200).build());
        preTransactionModel.addNewBasket(basket);
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.discount_basket_item)
    public void onDiscountBasketItem() {
        if (!transactionRequest.getBaskets().isEmpty()) {
            Basket basket = transactionRequest.getBaskets().get(0);
            if (!basket.getBasketItems().isEmpty()) {
                BasketItem basketItem = basket.getBasketItems().get(0);
                long amountForDiscount = basketItem.getIndividualAmount();
                BasketItem discountItem = new BasketItemBuilder(basketItem).withLabel("Reward - free item: " + basketItem.getLabel())
                        .withAmount(amountForDiscount * -1).build();
                preTransactionModel.applyDiscountsToBasket(basket.getId(), Arrays.asList(discountItem), PAYMENT_METHOD_REWARD);
            }
        }
        updateModel();
        disablePayViews();
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        preTransactionModel.addAuditEntry(INFO, "Just a hello from %s", getClass().getSimpleName());
        preTransactionModel.sendResponse();
        finish();
    }

    private void disablePayViews() {
        for (View payView : payViews) {
            payView.setEnabled(false);
        }
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected Class<?> getRequestClass() {
        return TransactionRequest.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return FlowResponse.class;
    }

    @Override
    protected String getModelJson() {
        return StageModelHelper.getFlowResponse(preTransactionModel).toJson();
    }

    @Override
    protected String getRequestJson() {
        return transactionRequest.toJson();
    }
}
