package com.aevi.sdk.pos.flow.model;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_BASKET;
import static com.aevi.sdk.flow.constants.TransactionTypes.SALE;
import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest {

    private Payment defaultPayment;

    @Before
    public void setUp() throws Exception {
        defaultPayment = new PaymentBuilder().withAmounts(new Amounts(1000, "GBP")).withTransactionType(SALE).build();
    }

    @Test
    public void canDeserializeWithBasketOption() {
        BasketItem item1 = new BasketItem("Walls Bangers", 1000);
        BasketItem item2 = new BasketItem("Golden Delicious Apples", 400);
        BasketItem item3 = new BasketItem("VAT @20%", 280);
        setupBasket(defaultPayment, item1, item2, item3);

        String json = defaultPayment.toJson();
        Payment result = Payment.fromJson(json);

        assertBasket(result, item1, item2, item3);
        String json2 = result.toJson();
        assertThat(json2).isEqualTo(json);
    }

    @Test
    public void canAddBasketItems() {
        BasketItem item1 = new BasketItem("Walls Bangers", 1000);
        BasketItem item2 = new BasketItem("Golden Delicious Apples", 400);
        setupBasket(defaultPayment, item1, item2);

        String json = defaultPayment.toJson();

        assertThat(json).isNotNull();
        assertThat(json).containsSequence(
                "{\"data\":{\"basket\":{\"value\":{\"displayItems\":[" + "{\"count\":1,\"label\":\"Walls Bangers\",\"amount\":1000}," + "{\"count\":1,\"label\":\"Golden Delicious Apples\",\"amount\":400}" + "]},\"type\":\"com.aevi.sdk.pos.flow.model.Basket\"}");
    }

    @Test
    public void canFindBasketItemByLabel() {
        Basket basket = setupDefaultBasket();

        BasketItem result = basket.getItem("LabelOne");
        assertThat(result).isNotNull();
        assertThat(result.getIndividualAmount()).isEqualTo(1000);
    }

    @Test
    public void canIncrementBasketItemCount() {
        Basket basket = setupDefaultBasket();

        BasketItem result = basket.getItem("LabelOne");
        result.addOne();
        BasketItem result2 = basket.getItem("LabelOne");
        assertThat(result2.getCount()).isEqualTo(2);
    }

    @Test
    public void canSetAndDecrementBasketItemCount() {
        Basket basket = setupDefaultBasket();

        BasketItem result = basket.getItem("LabelOne");
        result.setCount(4);
        result.removeOne();
        BasketItem result2 = basket.getItem("LabelOne");
        assertThat(result2.getCount()).isEqualTo(3);
    }

    @Test
    public void canAddOneOfTypeToBasket() {
        Basket basket = setupDefaultBasket();

        basket.addOneOf(new BasketItem("LabelOne", 1000));

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithNoRetain() {
        Basket basket = setupDefaultBasket();

        basket.removeOneOf(new BasketItem("LabelOne", 1000), false);

        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithWithRetain() {
        Basket basket = setupDefaultBasket();

        basket.removeOneOf(new BasketItem("LabelOne", 1000), true);

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void canAddItemsAndMerge() {
        Basket basket = setupDefaultBasket();

        basket.addItemMerge(new BasketItem("LabelOne", 1000));
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveMultipleItems() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(5, "LabelOne", null, 1000), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(5);
    }

    @Test
    public void cannotRemoveMoreThanAllItems() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(25, "LabelOne", null, 1000), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void willRemoveIfZeroLeftAndRetainSetToFalse() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(25, "LabelOne", null, 1000), false);
        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    private Basket setupDefaultBasket() {
        BasketItem item1 = new BasketItem("LabelOne", 1000);
        BasketItem item2 = new BasketItem("Golden Delicious Apples", 400);
        Basket basket = new Basket();
        basket.addItems(item1, item2);
        return basket;
    }

    private void assertBasket(Payment result, BasketItem... items) {
        Basket basket = (Basket) result.getAdditionalData().getValue(DATA_KEY_BASKET);
        assertThat(basket).isNotNull();
        List<BasketItem> resultItems = basket.getDisplayItems();
        assertThat(resultItems).isNotNull();
        assertThat(resultItems.size()).isEqualTo(items.length);
        assertThat(resultItems).containsExactly(items);
    }

    private void setupBasket(Payment payment, BasketItem... items) {
        Basket basket = new Basket();
        basket.addItems(items);
        payment.getAdditionalData().addData(DATA_KEY_BASKET, basket);
    }


}
