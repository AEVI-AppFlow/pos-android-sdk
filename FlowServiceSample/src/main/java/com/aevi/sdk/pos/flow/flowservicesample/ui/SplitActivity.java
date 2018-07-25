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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.SplitDataKeys;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.SplitBasketHelper;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aevi.sdk.flow.constants.SplitDataKeys.*;

/**
 * Sample for a split application.
 *
 * This sample illustrates both splitting via basket items and amounts.
 *
 * In order to keep complexity down, this sample only allows splitting into two transactions.
 * The API itself supports splitting into any arbitrary number of transactions.
 */
public class SplitActivity extends BaseSampleAppCompatActivity<FlowResponse> {

    private SplitRequest splitRequest;
    private SplitBasketHelper splitBasketHelper;
    private FlowResponse flowResponse;
    private ModelDisplay modelDisplay;

    @BindView(R.id.info_message)
    TextView infoMessage;

    @BindView(R.id.split_amounts)
    Button splitAmountsButton;

    @BindView(R.id.split_basket)
    Button splitBasketButton;

    @BindView(R.id.prev_split_info)
    TextView prevSplitInfo;

    @BindView(R.id.send_response)
    Button sendResponseButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        ButterKnife.bind(this);

        splitRequest = SplitRequest.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        if (SplitBasketHelper.canSplitViaBasket(splitRequest)) {
            splitBasketHelper = SplitBasketHelper.createFromSplitRequest(splitRequest, false);
            splitBasketHelper.logBaskets();
        }
        flowResponse = new FlowResponse();

        setupSplit();
        registerForActivityEvents();
        setupToolbar(toolbar, R.string.fss_split);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
    }

    private void setupSplit() {
        // As a split app, you must take into account declined transactions
        if (lastTransactionFailed()) {
            prevSplitInfo.setVisibility(View.VISIBLE);
            prevSplitInfo.setText(R.string.prev_txn_declined);
            prevSplitInfo.setTextColor(Color.RED);
        }

        if (!splitRequest.hasPreviousTransactions()) {
            if (SplitBasketHelper.canSplitViaBasket(splitRequest)) {
                infoMessage.setText(R.string.choose_split_type);
            } else {
                infoMessage.setText(R.string.only_amount_split_possible);
                splitBasketButton.setEnabled(false);
            }
        } else {
            String prevInfoText;
            String lastSplitType = splitRequest.getLastTransaction().getAdditionalData().getStringValue(SplitDataKeys.DATA_KEY_SPLIT_TYPE);

            if (lastSplitType.equals(SplitDataKeys.SPLIT_TYPE_BASKET)) {
                splitAmountsButton.setVisibility(View.GONE);
                splitBasketButton.setText(R.string.add_remaining_basket_items);
                prevInfoText = getPaidForBasketItems();
            } else {
                splitBasketButton.setVisibility(View.GONE);
                splitAmountsButton.setText(R.string.add_remaining_amounts);
                prevInfoText = getString(R.string.previously_paid_amount, getPreviousAmountTotalFormatted());
            }

            if (!lastTransactionFailed()) {
                prevSplitInfo.setVisibility(View.VISIBLE);
                prevSplitInfo.setText(prevInfoText);
            }
        }
    }

    private boolean lastTransactionFailed() {
        return splitRequest.hasPreviousTransactions() && !splitRequest.getLastTransaction().hasProcessedRequestedAmounts() &&
                splitRequest.getLastTransaction().hasDeclinedResponses();
    }

    private String getPreviousAmountTotalFormatted() {
        return AmountFormatter.formatAmount(splitRequest.getLastTransaction().getProcessedAmounts().getCurrency(),
                splitRequest.getLastTransaction().getProcessedAmounts().getTotalAmountValue());
    }

    private String getPaidForBasketItems() {
        Basket paidItems = splitBasketHelper.getAllPaidItems();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.prev_paid_for_items)).append(" ").append(getPreviousAmountTotalFormatted()).append(")\n");
        for (BasketItem basketItem : paidItems.getBasketItems()) {
            stringBuilder.append(basketItem.getLabel()).append("  (").append(basketItem.getCount()).append(")").append(" @ ")
                    .append(AmountFormatter.formatAmount(splitRequest.getSourcePayment().getAmounts().getCurrency(), basketItem.getIndividualAmount()))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateModel();
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showFlowResponse(flowResponse);
        }
    }

    private void disableSplitButtons() {
        splitAmountsButton.setEnabled(false);
        splitBasketButton.setEnabled(false);
        sendResponseButton.setText(R.string.process_split);
    }

    @OnClick(R.id.split_basket)
    public void onSplitBasket() {
        disableSplitButtons();

        Basket nextSplitBasket;
        if (!splitRequest.hasPreviousTransactions()) {
            nextSplitBasket = splitBasketInHalf();
        } else {
            nextSplitBasket = splitBasketHelper.getRemainingItems();
        }

        AmountsModifier amountsModifier = new AmountsModifier(splitRequest.getRemainingAmounts());
        amountsModifier.updateBaseAmount(nextSplitBasket.getTotalBasketValue());

        flowResponse.updateRequestAmounts(amountsModifier.build());
        flowResponse.addAdditionalRequestData(AdditionalDataKeys.DATA_KEY_BASKET, nextSplitBasket);
        addCommonSplitData(SPLIT_TYPE_BASKET);
        updateModel();
    }

    private Basket splitBasketInHalf() {
        Basket basket = splitBasketHelper.getSourceItems();
        int totalNumberOfItems = basket.getTotalNumberOfItems();
        int itemsForFirstSplit = totalNumberOfItems / 2;

        // This will simply get half (rounded down) of the items for the first txn
        for (int i = 0, count = 0; i < basket.getBasketItems().size() && count < itemsForFirstSplit; i++) {
            BasketItem item = basket.getBasketItems().get(i);
            if (count + item.getCount() > itemsForFirstSplit) {
                item = new BasketItemBuilder(item).withCount(itemsForFirstSplit - count).build();
            }
            splitBasketHelper.transferItemsFromRemainingToNextSplit(item);
            count += item.getCount();
        }
        return splitBasketHelper.getNextSplitItems();
    }

    @OnClick(R.id.split_amounts)
    public void onSplitAmounts() {
        disableSplitButtons();
        AmountsModifier amountsModifier = new AmountsModifier(splitRequest.getRemainingAmounts());

        // Set up first split to be half the amount, and the second split will simply add the remaining amounts
        if (!splitRequest.hasPreviousTransactions()) {
            amountsModifier.updateBaseAmount(splitRequest.getRemainingAmounts().getBaseAmountValue() / 2);
        }

        flowResponse.updateRequestAmounts(amountsModifier.build());
        addCommonSplitData(SPLIT_TYPE_AMOUNTS);
        updateModel();
    }

    private void addCommonSplitData(String splitType) {
        flowResponse.addAdditionalRequestData(SplitDataKeys.DATA_KEY_SPLIT_TXN, true);
        flowResponse.addAdditionalRequestData(SplitDataKeys.DATA_KEY_NUM_SPLITS, 2);
        flowResponse.addAdditionalRequestData(SplitDataKeys.DATA_KEY_SPLIT_TYPE, splitType);
    }

    @OnClick(R.id.cancel_transaction)
    public void onCancelTransaction() {
        disableSplitButtons();
        flowResponse.setCancelTransaction(true);
        onSendResponse();
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        sendResponseAndFinish(flowResponse);
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return PaymentStage.SPLIT.name();
    }

    @Override
    protected Class<?> getRequestClass() {
        return SplitRequest.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return FlowResponse.class;
    }

    @Override
    protected String getModelJson() {
        return flowResponse.toJson();
    }

    @Override
    protected String getRequestJson() {
        return splitRequest.toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.split_help);
    }
}
