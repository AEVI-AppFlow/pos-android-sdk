package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.constants.AdditionalDataKeys;

/**
 * Basic helper for split applications to deal with basket splitting scenarios.
 *
 * TODO Can be much more sophisticated - review PartPay demo app
 */
public class SplitBasketHelper {

    private final SplitRequest splitRequest;
    private Basket paidItems;
    private Basket remainingItems;
    private Basket nextSplitItems;

    public SplitBasketHelper(SplitRequest splitRequest) {
        this.splitRequest = splitRequest;
        this.paidItems = new Basket();
        this.remainingItems = new Basket();
        this.nextSplitItems = new Basket();
        remainingItems.addItems(splitRequest.getSourcePayment().getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_BASKET, Basket.class));

        for (Transaction transaction : splitRequest.getTransactions()) {
            if (transaction.hasProcessedRequestedAmounts()) {
                Basket basket = transaction.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_BASKET, Basket.class);
                paidItems.addItems(basket);
                remainingItems.removeItems(basket, false);
            }
        }
    }

    public boolean isFirstSplit() {
        return !splitRequest.hasPreviousTransactions();
    }

    public Basket getPaidItems() {
        return paidItems;
    }

    public Basket getRemainingItems() {
        return remainingItems;
    }

    public void addItemForNextSplit(BasketItem basketItem) {
        nextSplitItems.addItemMerge(basketItem);
    }

    public Basket getNextSplitItems() {
        return nextSplitItems;
    }
}
