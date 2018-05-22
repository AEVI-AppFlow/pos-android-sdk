package com.aevi.sdk.pos.flow.sample;

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.TransactionTypes;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.pos.flow.model.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SplitBasketHelperTest {

    private BasketItem ITEM_ONE_COUNT_ONE = new BasketItemBuilder().generateRandomId().withLabel("SingleCount").withCategory("Muppets").withAmount(1000).build();
    private BasketItem ITEM_ONE_COUNT_ZERO = new BasketItemBuilder(ITEM_ONE_COUNT_ONE).withCount(0).build();
    private BasketItem ITEM_TWO_COUNT_TWO = new BasketItemBuilder().generateRandomId().withLabel("DoubleCount").withCategory("Muppets").withAmount(1000).withCount(2).build();
    private BasketItem ITEM_TWO_COUNT_ONE = new BasketItemBuilder(ITEM_TWO_COUNT_TWO).withCount(1).build();

    private Basket sourceBasket = new Basket();
    private List<Transaction> transactions = new ArrayList<>();
    private PaymentBuilder paymentBuilder = new PaymentBuilder();

    private SplitBasketHelper splitBasketHelper;

    private void createSplitBasketHelper(boolean... retainZeroCountRemaining) {
        Payment payment = paymentBuilder.withTransactionType(TransactionTypes.SALE)
                .withAmounts(new Amounts(sourceBasket.getTotalBasketValue(), "GBP"))
                .addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, sourceBasket).build();
        SplitRequest splitRequest = new SplitRequest(payment, transactions);
        splitBasketHelper = SplitBasketHelper.createFromSplitRequest(splitRequest, retainZeroCountRemaining.length > 0 && retainZeroCountRemaining[0]);
    }

    private void setupPrevTxnPaidOffItemTwoCountOne() {
        AdditionalData additionalData = new AdditionalData();
        Basket basket = new Basket();
        basket.addItems(ITEM_TWO_COUNT_ONE);
        additionalData.addData(AdditionalDataKeys.DATA_KEY_BASKET, basket);

        // Add successful txn paying off one of item two
        Transaction transaction = new Transaction(new Amounts(ITEM_TWO_COUNT_ONE.getIndividualAmount(), "GBP"), additionalData);
        transaction.addTransactionResponse(new TransactionResponseBuilder("123")
                .approve(new Amounts(ITEM_TWO_COUNT_ONE.getIndividualAmount(), "GBP")).build());

        // Add a failed txn to ensure it is not taken into account
        Transaction failedTxn = new Transaction(new Amounts(500, "GBP"), additionalData);
        failedTxn.addTransactionResponse(new TransactionResponseBuilder("456").decline("Because").build());

        transactions.add(transaction);
        transactions.add(failedTxn);
    }

    @Test
    public void remainingBasketShouldBeSameAsSourceBasketForFirstSplit() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        createSplitBasketHelper();
        assertThat(splitBasketHelper.getRemainingItems().getTotalNumberOfItems()).isEqualTo(ITEM_ONE_COUNT_ONE.getCount() + ITEM_TWO_COUNT_TWO.getCount());
        assertThat(splitBasketHelper.getRemainingItems()).isEqualTo(sourceBasket);
    }

    @Test
    public void canSplitViaBasketShouldReturnTrueIfHasBasket() throws Exception {
        createSplitBasketHelper();
        assertThat(SplitBasketHelper.canSplitViaBasket(new SplitRequest(paymentBuilder.build(), transactions))).isTrue();
    }

    @Test
    public void canSplitViaBasketShouldReturnFalseIfNoBasket() throws Exception {
        createSplitBasketHelper();
        paymentBuilder.getCurrentAdditionalData().removeData(AdditionalDataKeys.DATA_KEY_BASKET);
        assertThat(SplitBasketHelper.canSplitViaBasket(new SplitRequest(paymentBuilder.build(), transactions))).isFalse();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedExceptionIfNoBasket() throws Exception {
        SplitBasketHelper.createFromSplitRequest(new SplitRequest(paymentBuilder.withTransactionType(TransactionTypes.SALE)
                .withAmounts(new Amounts(1000, "GBP")).build(), transactions), false);
    }

    @Test
    public void shouldTakeApprovedTransactionsIntoAccount() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        setupPrevTxnPaidOffItemTwoCountOne();
        createSplitBasketHelper();

        Basket remainingItems = splitBasketHelper.getRemainingItems();
        assertThat(remainingItems.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(remainingItems.getTotalNumberOfItems()).isEqualTo(2);
        assertThat(remainingItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_ONE);

        Basket paidItems = splitBasketHelper.getAllPaidItems();
        assertThat(paidItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(paidItems.getNumberOfUniqueItems()).isEqualTo(1);
        assertThat(paidItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_TWO_COUNT_ONE);
    }

    @Test
    public void shouldRetainZeroCountRemainingItemsIfFlagSet() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_ONE);
        setupPrevTxnPaidOffItemTwoCountOne();
        createSplitBasketHelper(true);

        Basket remainingItems = splitBasketHelper.getRemainingItems();
        assertThat(remainingItems.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(remainingItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(remainingItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ONE, new BasketItemBuilder(ITEM_TWO_COUNT_ONE).withCount(0).build());

        Basket paidItems = splitBasketHelper.getAllPaidItems();
        assertThat(paidItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(paidItems.getNumberOfUniqueItems()).isEqualTo(1);
        assertThat(paidItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_TWO_COUNT_ONE);
    }

    @Test
    public void shouldBeAbleToMoveFromRemainingToNextSplit() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_COUNT_ONE);

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ZERO, ITEM_TWO_COUNT_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ONE);
    }

    @Test
    public void shouldBeAbleToMoveFromNextSplitBackToRemaining() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_COUNT_ONE);
        splitBasketHelper.transferItemsFromNextSplitToRemaining(ITEM_ONE_COUNT_ONE);

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).isEmpty();
    }

    @Test
    public void shouldBeAbleToMoveAllBackToRemaining() throws Exception {
        sourceBasket.addItems(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        splitBasketHelper.transferAllNextSplitBackToRemaining();

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_COUNT_ONE, ITEM_TWO_COUNT_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).isEmpty();
    }
}
