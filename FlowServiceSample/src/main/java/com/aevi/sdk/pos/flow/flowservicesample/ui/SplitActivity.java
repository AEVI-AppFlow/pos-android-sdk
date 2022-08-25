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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.flow.constants.SplitDataKeys;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.SplitBasketHelper;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.SplitModel;
import com.aevi.sdk.pos.flow.stage.StageModelHelper;

import static com.aevi.sdk.flow.constants.SplitDataKeys.SPLIT_TYPE_AMOUNTS;
import static com.aevi.sdk.flow.constants.SplitDataKeys.SPLIT_TYPE_BASKET;
import static com.aevi.sdk.flow.model.AuditEntry.AuditSeverity.INFO;

import androidx.appcompat.widget.Toolbar;

/**
 * Sample for a split application.
 *
 * This sample illustrates both splitting via basket items and amounts.
 *
 * In order to keep complexity down, this sample only allows splitting into two transactions.
 * The API itself supports splitting into any arbitrary number of transactions.
 */
public class SplitActivity extends BaseSampleAppCompatActivity {

    private SplitModel splitModel;
    private SplitRequest splitRequest;
    private SplitBasketHelper splitBasketHelper;
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

        splitModel = SplitModel.fromActivity(this);
        splitRequest = splitModel.getSplitRequest();
        if (SplitBasketHelper.canSplitViaBasket(splitRequest)) {
            splitBasketHelper = SplitBasketHelper.createFromSplitRequest(splitRequest, false);
            splitBasketHelper.logBaskets();
        }

        setupSplit();
        setupToolbar(toolbar, R.string.fss_split);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        subscribeToFlowServiceEvents(splitModel);
    }

    private void setupSplit() {
        // As a split app, you must take into account declined transactions
        if (splitModel.lastTransactionFailed()) {
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

            if (lastSplitType.equals(SPLIT_TYPE_BASKET)) {
                splitAmountsButton.setVisibility(View.GONE);
                splitBasketButton.setText(R.string.add_remaining_basket_items);
                prevInfoText = getPaidForBasketItems();
            } else {
                splitBasketButton.setVisibility(View.GONE);
                splitAmountsButton.setText(R.string.add_remaining_amounts);
                prevInfoText = getString(R.string.previously_paid_amount, getPreviousAmountTotalFormatted());
            }

            if (!splitModel.lastTransactionFailed()) {
                prevSplitInfo.setVisibility(View.VISIBLE);
                prevSplitInfo.setText(prevInfoText);
            }
        }
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
            stringBuilder.append(basketItem.getLabel()).append("  (").append(basketItem.getQuantity()).append(")").append(" @ ")
                    .append(AmountFormatter
                                    .formatAmount(splitRequest.getSourcePayment().getAmounts().getCurrency(), basketItem.getIndividualAmount()))
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
            modelDisplay.showFlowResponse(StageModelHelper.getFlowResponse(splitModel));
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

        splitModel.setBasketForNextTransaction(nextSplitBasket);
        splitModel.addRequestData(SplitDataKeys.DATA_KEY_SPLIT_TYPE, SPLIT_TYPE_BASKET);
        updateModel();
    }

    private Basket splitBasketInHalf() {
        Basket basket = splitBasketHelper.getSourceItems();
        int totalNumberOfItems = basket.getTotalNumberOfItems();
        int itemsForFirstSplit = totalNumberOfItems / 2;

        // This will simply get half (rounded down) of the items for the first txn
        for (int i = 0, count = 0; i < basket.getBasketItems().size() && count < itemsForFirstSplit; i++) {
            BasketItem item = basket.getBasketItems().get(i);
            if (count + item.getQuantity() > itemsForFirstSplit) {
                item = new BasketItemBuilder(item).withQuantity(itemsForFirstSplit - count).build();
            }
            splitBasketHelper.transferItemsFromRemainingToNextSplit(item);
            count += item.getQuantity();
        }
        return splitBasketHelper.getNextSplitItems();
    }

    @OnClick(R.id.split_amounts)
    public void onSplitAmounts() {
        disableSplitButtons();
        long splitBaseAmount = splitRequest.getRemainingAmounts().getBaseAmountValue();
        // Set up first split to be half the amount, and the second split will simply add the remaining amounts
        if (!splitRequest.hasPreviousTransactions()) {
            splitBaseAmount /= 2;
        }

        splitModel.setBaseAmountForNextTransaction(splitBaseAmount);
        splitModel.addRequestData(SplitDataKeys.DATA_KEY_SPLIT_TYPE, SPLIT_TYPE_AMOUNTS);
        updateModel();
    }

    @OnClick(R.id.cancel_transaction)
    public void onCancelTransaction() {
        disableSplitButtons();
        splitModel.cancelFlow();
        finish();
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        splitModel.addAuditEntry(INFO, "Just a hello from %s", getClass().getSimpleName());
        splitModel.sendResponse();
        finish();
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return FlowStages.SPLIT;
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
        return StageModelHelper.getFlowResponse(splitModel).toJson();
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
