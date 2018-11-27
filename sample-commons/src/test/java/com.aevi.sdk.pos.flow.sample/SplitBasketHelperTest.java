package com.aevi.sdk.pos.flow.sample;

import com.aevi.sdk.flow.constants.FlowTypes;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.pos.flow.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SplitBasketHelperTest {

    private BasketItem ITEM_ONE_QUANTITY_ONE =
            new BasketItemBuilder().generateRandomId().withLabel("SingleQuantity").withCategory("Muppets").withAmount(1000).build();
    private BasketItem ITEM_ONE_QUANTITY_ZERO = new BasketItemBuilder(ITEM_ONE_QUANTITY_ONE).withQuantity(0).build();
    private BasketItem ITEM_TWO_QUANTITY_TWO =
            new BasketItemBuilder().generateRandomId().withLabel("DoubleQuantity").withCategory("Muppets").withAmount(1000).withQuantity(2).build();
    private BasketItem ITEM_TWO_QUANTITY_ONE = new BasketItemBuilder(ITEM_TWO_QUANTITY_TWO).withQuantity(1).build();

    private Basket sourceBasket = new Basket("test");
    private List<Transaction> transactions = new ArrayList<>();
    private PaymentBuilder paymentBuilder = new PaymentBuilder();

    private SplitBasketHelper splitBasketHelper;

    private void createSplitBasketHelper(boolean... retainZeroQuantityRemaining) {
        Payment payment = paymentBuilder.withPaymentFlow(FlowTypes.FLOW_TYPE_SALE)
                .withAmounts(new Amounts(sourceBasket.getTotalBasketValue(), "GBP"))
                .withBasket(sourceBasket).build();
        SplitRequest splitRequest = new SplitRequest(payment, transactions);
        splitBasketHelper =
                SplitBasketHelper.createFromSplitRequest(splitRequest, retainZeroQuantityRemaining.length > 0 && retainZeroQuantityRemaining[0]);
    }

    private void setupPrevTxnPaidOffItemTwoQuantityOne() {
        AdditionalData additionalData = new AdditionalData();
        Basket basket = new Basket("test");
        basket.addItems(ITEM_TWO_QUANTITY_ONE);

        // Add successful txn paying off one of item two
        Transaction transaction =
                new Transaction(new Amounts(ITEM_TWO_QUANTITY_ONE.getIndividualAmount(), "GBP"), Arrays.asList(basket), null, additionalData);
        transaction.addTransactionResponse(new TransactionResponseBuilder("123")
                                                   .approve(new Amounts(ITEM_TWO_QUANTITY_ONE.getIndividualAmount(), "GBP")).build());

        // Add a failed txn to ensure it is not taken into account
        Transaction failedTxn = new Transaction(new Amounts(500, "GBP"), null, null, additionalData);
        failedTxn.addTransactionResponse(new TransactionResponseBuilder("456").decline("Because").build());

        transactions.add(transaction);
        transactions.add(failedTxn);
    }

    @Test
    public void remainingBasketShouldBeSameAsSourceBasketForFirstSplit() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        createSplitBasketHelper();
        assertThat(splitBasketHelper.getRemainingItems().getTotalNumberOfItems())
                .isEqualTo(ITEM_ONE_QUANTITY_ONE.getQuantity() + ITEM_TWO_QUANTITY_TWO.getQuantity());
        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).isEqualTo(sourceBasket.getBasketItems());
    }

    @Test
    public void canSplitViaBasketShouldReturnTrueIfHasBasket() throws Exception {
        createSplitBasketHelper();
        assertThat(SplitBasketHelper.canSplitViaBasket(new SplitRequest(paymentBuilder.build(), transactions))).isTrue();
    }

    @Test
    public void canSplitViaBasketShouldReturnFalseIfNoBasket() throws Exception {
        createSplitBasketHelper();
        paymentBuilder.withBasket(null);
        assertThat(SplitBasketHelper.canSplitViaBasket(new SplitRequest(paymentBuilder.build(), transactions))).isFalse();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedExceptionIfNoBasket() throws Exception {
        SplitBasketHelper.createFromSplitRequest(new SplitRequest(paymentBuilder.withPaymentFlow(FlowTypes.FLOW_TYPE_SALE)
                                                                          .withAmounts(new Amounts(1000, "GBP")).build(), transactions), false);
    }

    @Test
    public void shouldTakeApprovedTransactionsIntoAccount() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        setupPrevTxnPaidOffItemTwoQuantityOne();
        createSplitBasketHelper();

        Basket remainingItems = splitBasketHelper.getRemainingItems();
        assertThat(remainingItems.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(remainingItems.getTotalNumberOfItems()).isEqualTo(2);
        assertThat(remainingItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_ONE);

        Basket paidItems = splitBasketHelper.getAllPaidItems();
        assertThat(paidItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(paidItems.getNumberOfUniqueItems()).isEqualTo(1);
        assertThat(paidItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_TWO_QUANTITY_ONE);
    }

    @Test
    public void shouldRetainZeroQuantityRemainingItemsIfFlagSet() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_ONE);
        setupPrevTxnPaidOffItemTwoQuantityOne();
        createSplitBasketHelper(true);

        Basket remainingItems = splitBasketHelper.getRemainingItems();
        assertThat(remainingItems.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(remainingItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(remainingItems.getBasketItems())
                .containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ONE, new BasketItemBuilder(ITEM_TWO_QUANTITY_ONE).withQuantity(0).build());

        Basket paidItems = splitBasketHelper.getAllPaidItems();
        assertThat(paidItems.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(paidItems.getNumberOfUniqueItems()).isEqualTo(1);
        assertThat(paidItems.getBasketItems()).containsExactlyInAnyOrder(ITEM_TWO_QUANTITY_ONE);
    }

    @Test
    public void shouldBeAbleToMoveFromRemainingToNextSplit() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_QUANTITY_ONE);

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ZERO, ITEM_TWO_QUANTITY_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ONE);
    }

    @Test
    public void shouldBeAbleToMoveFromNextSplitBackToRemaining() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_QUANTITY_ONE);
        splitBasketHelper.transferItemsFromNextSplitToRemaining(ITEM_ONE_QUANTITY_ONE);

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).isEmpty();
    }

    @Test
    public void shouldBeAbleToMoveAllBackToRemaining() throws Exception {
        sourceBasket.addItems(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        createSplitBasketHelper();

        splitBasketHelper.transferItemsFromRemainingToNextSplit(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        splitBasketHelper.transferAllNextSplitBackToRemaining();

        assertThat(splitBasketHelper.getRemainingItems().getBasketItems()).containsExactlyInAnyOrder(ITEM_ONE_QUANTITY_ONE, ITEM_TWO_QUANTITY_TWO);
        assertThat(splitBasketHelper.getNextSplitItems().getBasketItems()).isEmpty();
    }
}
