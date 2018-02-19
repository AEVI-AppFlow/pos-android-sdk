package com.aevi.payment.model;


import com.aevi.sdk.pos.flow.model.Amount;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.BasketItem;
import com.aevi.sdk.pos.flow.model.Request;

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
        com.aevi.sdk.pos.flow.model.BasketItem item1 = new com.aevi.sdk.pos.flow.model.BasketItem("Walls Bangers", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP"));
        com.aevi.sdk.pos.flow.model.BasketItem item2 = new com.aevi.sdk.pos.flow.model.BasketItem("Golden Delicious Apples", new com.aevi.sdk.pos.flow.model.Amount(400, "GBP"));
        com.aevi.sdk.pos.flow.model.BasketItem item3 = new com.aevi.sdk.pos.flow.model.BasketItem("VAT @20%", new com.aevi.sdk.pos.flow.model.Amount(280, "GBP"));
        setupBasket(defaultRequest, item1, item2, item3);

        String json = defaultRequest.toJson();
        Request result = com.aevi.sdk.pos.flow.model.Request.fromJson(json);

        assertBasket(result, item1, item2, item3);
        String json2 = result.toJson();
        assertThat(json2).isEqualTo(json);
    }

    @Test
    public void canAddBasketItems() {
        com.aevi.sdk.pos.flow.model.BasketItem item1 = new BasketItem("Walls Bangers", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP"));
        com.aevi.sdk.pos.flow.model.BasketItem item2 = new com.aevi.sdk.pos.flow.model.BasketItem("Golden Delicious Apples", new com.aevi.sdk.pos.flow.model.Amount(400, "GBP"));
        setupBasket(defaultRequest, item1, item2);

        String json = defaultRequest.toJson();

        assertThat(json).isNotNull();
        assertThat(json).containsSequence(
                "{\"options\":{\"basket\":{\"value\":{\"displayItems\":[" + "{\"count\":1,\"label\":\"Walls Bangers\",\"amount\":{\"value\":1000,\"currency\":\"GBP\"}}," + "{\"count\":1,\"label\":\"Golden Delicious Apples\",\"amount\":{\"value\":400,\"currency\":\"GBP\"}}" + "]},\"type\":\"com.aevi.sdk.pos.flow.model.Basket\"}");
    }

    @Test
    public void canFindBasketItemByLabel() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        com.aevi.sdk.pos.flow.model.BasketItem result = basket.getItem("LabelOne");
        assertThat(result).isNotNull();
        assertThat(result.getIndividualAmount()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP"));
    }

    @Test
    public void canIncrementBasketItemCount() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        com.aevi.sdk.pos.flow.model.BasketItem result = basket.getItem("LabelOne");
        result.addOne();
        com.aevi.sdk.pos.flow.model.BasketItem result2 = basket.getItem("LabelOne");
        assertThat(result2.getCount()).isEqualTo(2);
    }

    @Test
    public void canSetAndDecrementBasketItemCount() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        com.aevi.sdk.pos.flow.model.BasketItem result = basket.getItem("LabelOne");
        result.setCount(4);
        result.removeOne();
        com.aevi.sdk.pos.flow.model.BasketItem result2 = basket.getItem("LabelOne");
        assertThat(result2.getCount()).isEqualTo(3);
    }

    @Test
    public void canAddOneOfTypeToBasket() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        basket.addOneOf(new com.aevi.sdk.pos.flow.model.BasketItem("LabelOne", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")));

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithNoRetain() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        basket.removeOneOf(new com.aevi.sdk.pos.flow.model.BasketItem("LabelOne", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")), false);

        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    @Test
    public void canRemoveOneOfTypeFromBasketWithWithRetain() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        basket.removeOneOf(new com.aevi.sdk.pos.flow.model.BasketItem("LabelOne", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")), true);

        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void canAddItemsAndMerge() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();

        basket.addItemMerge(new com.aevi.sdk.pos.flow.model.BasketItem("LabelOne", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")));
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(2);
    }

    @Test
    public void canRemoveMultipleItems() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new com.aevi.sdk.pos.flow.model.BasketItem(5, "LabelOne", null, new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(5);
    }

    @Test
    public void cannotRemoveMoreThanAllItems() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new com.aevi.sdk.pos.flow.model.BasketItem(25, "LabelOne", null, new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")), true);
        assertThat(basket.getDisplayItems().size()).isEqualTo(2);
        assertThat(basket.getItem("LabelOne")).isNotNull();
        assertThat(basket.getItem("LabelOne").getCount()).isEqualTo(0);
    }

    @Test
    public void willRemoveIfZeroLeftAndRetainSetToFalse() {
        com.aevi.sdk.pos.flow.model.Basket basket = setupDefaultBasket();
        basket.getItem("LabelOne").setCount(10);

        basket.removeItems(new com.aevi.sdk.pos.flow.model.BasketItem(25, "LabelOne", null, new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP")), false);
        assertThat(basket.getDisplayItems().size()).isEqualTo(1);
        assertThat(basket.getItem("LabelOne")).isNull();
    }

    private com.aevi.sdk.pos.flow.model.Basket setupDefaultBasket() {
        com.aevi.sdk.pos.flow.model.BasketItem item1 = new com.aevi.sdk.pos.flow.model.BasketItem("LabelOne", new com.aevi.sdk.pos.flow.model.Amount(1000, "GBP"));
        com.aevi.sdk.pos.flow.model.BasketItem item2 = new com.aevi.sdk.pos.flow.model.BasketItem("Golden Delicious Apples", new Amount(400, "GBP"));
        com.aevi.sdk.pos.flow.model.Basket basket = new com.aevi.sdk.pos.flow.model.Basket();
        basket.addItems(item1, item2);
        return basket;
    }

    private void assertBasket(com.aevi.sdk.pos.flow.model.Request result, com.aevi.sdk.pos.flow.model.BasketItem... items) {
        com.aevi.sdk.pos.flow.model.Basket basket = (com.aevi.sdk.pos.flow.model.Basket) result.getAdditionalData().getValue(DATA_KEY_BASKET);
        assertThat(basket).isNotNull();
        List<com.aevi.sdk.pos.flow.model.BasketItem> resultItems = basket.getDisplayItems();
        assertThat(resultItems).isNotNull();
        assertThat(resultItems.size()).isEqualTo(items.length);
        assertThat(resultItems).containsExactly(items);
    }

    private void setupBasket(com.aevi.sdk.pos.flow.model.Request request, com.aevi.sdk.pos.flow.model.BasketItem... items) {
        com.aevi.sdk.pos.flow.model.Basket basket = new com.aevi.sdk.pos.flow.model.Basket();
        basket.addItems(items);
        request.getAdditionalData().addData(DATA_KEY_BASKET, basket);
    }


}
