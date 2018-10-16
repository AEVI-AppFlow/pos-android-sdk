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

package com.aevi.sdk.pos.flow.sample;


import android.util.Log;

import com.aevi.sdk.pos.flow.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for split samples to deal with basket splitting scenarios.
 *
 * Can also be used as a base for split implementations when it comes to basket management.
 *
 * This class will track four different baskets.
 *
 * 1. "sourceItems" - the basket as created by the POS application provided in the Payment request
 * 2. "paidItemsPerSplit" - a list of baskets previously paid for
 * 3. "remainingItems" - the basket containing all the items that are yet to be paid for
 * 4. "nextSplitItems" - the basket containing the items for the next split transaction (as added by the split app)
 */
public class SplitBasketHelper {

    private static final String TAG = SplitBasketHelper.class.getSimpleName();

    private final SplitRequest splitRequest;
    private final Basket sourceItems;
    private final List<Basket> paidItemsPerSplit;
    private final Basket remainingItems;
    private final Basket nextSplitItems;

    /**
     * Check whether this payment contains a basket that allows us to split via basket items.
     *
     * @param splitRequest The split request received
     * @return True if split via basket items is possible, false otherwise
     */
    public static boolean canSplitViaBasket(SplitRequest splitRequest) {
        return splitRequest.getSourcePayment().getBasket() != null;
    }

    /**
     * Create a SplitBasketHelper instance from the given {@link SplitRequest}.
     *
     * This will set up the remaining and paid items baskets according to the information in the request.
     *
     * @param splitRequest                     The split request
     * @param retainZeroQuantityRemainingItems If true, items with zero quantity will be added to remaining items. If false, zero quantity items will not be added.
     * @return An instance of SplitBasketHelper
     * @throws UnsupportedOperationException If there is no basket in the source payment
     */
    public static SplitBasketHelper createFromSplitRequest(SplitRequest splitRequest, boolean retainZeroQuantityRemainingItems) throws UnsupportedOperationException {
        Basket sourceBasket = splitRequest.getSourcePayment().getBasket();
        if (sourceBasket == null) {
            throw new UnsupportedOperationException("The source payment does not have any associated basket");
        }

        List<Basket> paidItemsPerSplit = new ArrayList<>();
        Basket remainingItems = new Basket("remainingItems", sourceBasket.getBasketItems());

        for (Transaction transaction : splitRequest.getTransactions()) {
            // TODO What if this is false ? How do we handle partially fulfilled!?
            if (transaction.hasProcessedRequestedAmounts()) {
                for (Basket basket : transaction.getBaskets()) {
                    paidItemsPerSplit.add(basket);
                    removeItems(remainingItems, basket, retainZeroQuantityRemainingItems);
                }
            }
        }

        return new SplitBasketHelper(splitRequest, sourceBasket, paidItemsPerSplit, remainingItems);
    }

    private SplitBasketHelper(SplitRequest splitRequest, Basket sourceItems, List<Basket> paidItemsPerSplit, Basket remainingItems) {
        this.splitRequest = splitRequest;
        this.sourceItems = sourceItems;
        this.paidItemsPerSplit = paidItemsPerSplit;
        this.remainingItems = remainingItems;
        this.nextSplitItems = new Basket("splitItems");
    }

    /**
     * Get the split request used to track the splits.
     *
     * @return The split request
     */
    public SplitRequest getSplitRequest() {
        return splitRequest;
    }

    /**
     * Get the basket of items as provided via the {@link Payment} request from the POS app.
     *
     * @return The basket of items from the Payment request
     */
    public Basket getSourceItems() {
        return sourceItems;
    }

    /**
     * Get the list of baskets that have already been paid for in previous split transactions.
     *
     * This list can be used to show what each payee paid for in previous transactions.
     *
     * @return The list of baskets that have already been paid in previous split transactions
     */
    public List<Basket> getPaidItemsList() {
        return paidItemsPerSplit;
    }

    /**
     * Get an aggregate basket with all the paid items from the list of transactions.
     *
     * @return An aggregate basket with all the paid items from the list of transactions
     */
    public Basket getAllPaidItems() {
        Basket basket = new Basket("paidItems");
        for (Basket txnBasket : paidItemsPerSplit) {
            for (BasketItem basketItem : txnBasket.getBasketItems()) {
                basket.addItems(basketItem);
            }
        }
        return basket;
    }

    /**
     * Get the remaining basket items to be paid.
     *
     * @return The remaining basket items to be paid
     */
    public Basket getRemainingItems() {
        return remainingItems;
    }

    /**
     * Transfer basket items from the remaining basket to the next split basket.
     *
     * The quantity in the provided item determines the outcome quantity of the items in the remaining basket and in the next split basket.
     *
     * Note that this never removes the items from the remaining items, but simply updates the quantity and allows for zero-quantity items.
     *
     * This is intentional to support split apps in showing the full basket with the correct quantity for customer clarity.
     *
     * @param basketItems The basket items to process for the next split transaction
     */
    public void transferItemsFromRemainingToNextSplit(BasketItem... basketItems) {
        for (BasketItem basketItem : basketItems) {
            // Create new instance to hold the separate quantity
            nextSplitItems.addItems(new BasketItemBuilder(basketItem).build());
            removeItem(remainingItems, basketItem, true);
        }
    }

    /**
     * Transfer basket items back to the remaining items from the next split basket.
     *
     * @param basketItems The basket items to move back to remaining items
     */
    public void transferItemsFromNextSplitToRemaining(BasketItem... basketItems) {
        for (BasketItem basketItem : basketItems) {
            remainingItems.addItems(basketItem);
            removeItem(nextSplitItems, basketItem, false);
        }
    }

    /**
     * Transfer all the next split items back to the remaining basket.
     */
    public void transferAllNextSplitBackToRemaining() {
        BasketItem[] array = new BasketItem[nextSplitItems.getBasketItems().size()];
        nextSplitItems.getBasketItems().toArray(array);
        transferItemsFromNextSplitToRemaining(array);
    }

    /**
     * Get the basket for the next split transaction.
     *
     * Note that the client has to add items to this basket from the remaining items.
     *
     * @return The basket for the next split transaction
     */
    public Basket getNextSplitItems() {
        return nextSplitItems;
    }

    private static void copyItems(Basket to, Basket from) {
        for (BasketItem basketItem : from.getBasketItems()) {
            to.addItems(basketItem);
        }
    }

    private static void removeItems(Basket removeFrom, Basket source, boolean retain) {
        for (BasketItem basketItem : source.getBasketItems()) {
            removeItem(removeFrom, basketItem, retain);
        }
    }

    /*
     * Removes items of the given type from the basket up to the item quantity. If the number of items requested to be removed is greater than
     * the current quantity for this line item in the basket then all items are removed and the quantity is set to 0.
     */
    private static void removeItem(Basket basket, BasketItem item, boolean retain) {
        if (basket.hasItemWithId(item.getId())) {
            BasketItem itemLine = basket.getItemById(item.getId());
            replaceItem(basket, itemLine, -item.getQuantity(), retain);
        }
    }

    private static BasketItem replaceItem(Basket basket, BasketItem existingItem, int quantityOffset, boolean retainIfZero) {
        BasketItem newItem = new BasketItemBuilder(existingItem).offsetQuantityBy(quantityOffset).build();
        if (newItem.getQuantity() == 0 && !retainIfZero) {
            basket.getBasketItems().remove(existingItem);
        } else {
            basket.getBasketItems().set(basket.getBasketItems().indexOf(existingItem), newItem);
        }
        return newItem;
    }

    /**
     * Log baskets for debugging purposes
     */
    public void logBaskets() {
        Log.d(TAG, "Source items");
        logBasketItems(sourceItems);
        Log.d(TAG, "----");
        Log.d(TAG, "Previously paid items");
        logBasketItems(getAllPaidItems());
        Log.d(TAG, "----");
        Log.d(TAG, "Remaining items");
        logBasketItems(remainingItems);
        Log.d(TAG, "----");
        Log.d(TAG, "Next split items");
        logBasketItems(nextSplitItems);
    }

    private void logBasketItems(Basket basket) {
        for (BasketItem basketItem : basket.getBasketItems()) {
            Log.d(TAG, basketItem.toString());
        }
    }
}
