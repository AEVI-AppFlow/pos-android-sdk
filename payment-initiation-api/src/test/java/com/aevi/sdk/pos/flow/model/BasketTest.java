package com.aevi.sdk.pos.flow.model;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_BASKET;
import static com.aevi.sdk.flow.constants.TransactionTypes.TYPE_PAY;
import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest {

    private com.aevi.sdk.pos.flow.model.Request defaultRequest;

    @Before
    public void setUp() throws Exception {
        defaultRequest = new com.aevi.sdk.pos.flow.model.Request.Builder().withAmounts(new Amounts(1000, "GBP")).withTransactionType(TYPE_PAY).build();
    }

    @Test
    public void canDeserializeWithBasketOption() {
        BasketItem item1 = new BasketItem("Walls Bangers", new Amount(1000, "GBP"));
        BasketItem item2 = new BasketItem("Golden Delicious Apples", new Amount(400, "GBP"));
        BasketItem item3 = new BasketItem("VAT @20%", new Amount(280, "GBP"));
        setupBasket(defaultRequest, item1, item2, item3);

        String json = defaultRequest.toJson();
        Request result = com.aevi.sdk.pos.flow.model.Request.fromJson(json);

        assertBasket(result, item1, item2, item3);
        String json2 = result.toJson();
        assertThat(json2).isEqualTo(json);
    }

    @Test
    public void canAddBasketItems() {
        BasketItem item1 = new BasketItem("Walls Bangers", new Amount(1000, "GBP"));
        BasketItem item2 = new BasketItem("Golden Delicious Apples", new Amount(400, "GBP"));
        setupBasket(defaultRequest, item1, item2);

        String json = defaultRequest.toJson();

        assertThat(json).isNotNull();
        assertThat(json).containsSequence(
                "{\"data\":{\"basket\":{\"value\":{\"displayItems\":[" + "{\"count\":1,\"label\":\"Walls Bangers\",\"amount\":{\"value\":1000,\"currency\":\"GBP\"}}," + "{\"count\":1,\"label\":\"Golden Delicious Apples\",\"amount\":{\"value\":400,\"currency\":\"GBP\"}}" + "]},\"type\":\"com.aevi.sdk.pos.flow.model.Basket\"}");
    }

    @Test
    public void canFindBasketItemByLabel() {
        Basket basket = setupDefaultBasket();

        BasketItem result = basket.getItem("LabelOne");
        assertThat(result).isNotNull();
        assertThat(result.getIndividualAmount()).isEqualTo(new Amount(1000, "GBP"));
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

        basket.addOneOf(new BasketItem("LabelOne", new Amount(1000, "GBP")));

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithNoRetain() {
        Basket basket = setupDefaultBasket();

        basket.removeOneOf(new BasketItem("LabelOne", new Amount(1000, "GBP")), false);

        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithWithRetain() {
        Basket basket = setupDefaultBasket();

        basket.removeOneOf(new BasketItem("LabelOne", new Amount(1000, "GBP")), true);

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void canAddItemsAndMerge() {
        Basket basket = setupDefaultBasket();

        basket.addItemMerge(new BasketItem("LabelOne", new Amount(1000, "GBP")));
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveMultipleItems() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(5, "LabelOne", null, new Amount(1000, "GBP")), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(5);
    }

    @Test
    public void cannotRemoveMoreThanAllItems() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(25, "LabelOne", null, new Amount(1000, "GBP")), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void willRemoveIfZeroLeftAndRetainSetToFalse() {
        Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new BasketItem(25, "LabelOne", null, new Amount(1000, "GBP")), false);
        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    private Basket setupDefaultBasket() {
        BasketItem item1 = new BasketItem("LabelOne", new Amount(1000, "GBP"));
        BasketItem item2 = new BasketItem("Golden Delicious Apples", new Amount(400, "GBP"));
        Basket basket = new Basket();
        basket.addItems(item1, item2);
        return basket;
    }

    private void assertBasket(com.aevi.sdk.pos.flow.model.Request result, BasketItem... items) {
        Basket basket = (Basket) result.getAdditionalData().getValue(DATA_KEY_BASKET);
        assertThat(basket).isNotNull();
        List<BasketItem> resultItems = basket.getDisplayItems();
        assertThat(resultItems).isNotNull();
        assertThat(resultItems.size()).isEqualTo(items.length);
        assertThat(resultItems).containsExactly(items);
    }

    private void setupBasket(com.aevi.sdk.pos.flow.model.Request request, BasketItem... items) {
        Basket basket = new Basket();
        basket.addItems(items);
        request.getAdditionalData().addData(DATA_KEY_BASKET, basket);
    }


}
