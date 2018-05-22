package com.aevi.sdk.pos.flow.model;


import com.aevi.util.json.JsonConverter;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest {

    private static final BasketItem defaultItemOne = new BasketItem(UUID.randomUUID().toString(), "LabelOne", null, 1000, 2);
    private static final BasketItem defaultItemTwo = new BasketItem(UUID.randomUUID().toString(), "LabelTwo", null, 400, 1);
    private Basket sourceBasket;

    @Before
    public void setUp() throws Exception {
        sourceBasket = new Basket();
    }

    @Test
    public void canAddItems() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(sourceBasket.hasItemWithId(defaultItemOne.getId())).isTrue();
        assertThat(sourceBasket.hasItemWithId(defaultItemTwo.getId())).isTrue();
    }

    @Test
    public void canMergeItemsWithSameId() throws Exception {
        int singleItemCount = defaultItemOne.getCount();
        sourceBasket.addItems(defaultItemOne);

        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getCount()).isEqualTo(singleItemCount);

        sourceBasket.addItems(defaultItemOne);

        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getCount()).isEqualTo(singleItemCount * 2);
    }

    @Test
    public void returnsListInRecentFirstOrder() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.getBasketItems()).containsExactly(defaultItemTwo, defaultItemOne);
    }

    @Test
    public void listOrderIsRetainedAfterUpdatingCount() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.incrementItemCount(defaultItemOne.getId(), 1);
        sourceBasket.decrementItemCount(defaultItemOne.getId(), 1);

        assertThat(sourceBasket.getBasketItems()).containsExactly(defaultItemTwo, defaultItemOne);
    }

    @Test
    public void canHaveMultipleItemsSameLabel() throws Exception {
        sourceBasket.addItems(defaultItemOne);
        BasketItem anotherItem = new BasketItem("123", defaultItemOne.getLabel(), null, 500, 1);
        sourceBasket.addItems(anotherItem);

        assertThat(sourceBasket.getNumberOfUniqueItems()).isEqualTo(2);
        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getLabel()).isEqualTo(sourceBasket.getItemById(anotherItem.getId()).getLabel());
    }

    @Test
    public void canSerialiseAndDeserialise() {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        String json = sourceBasket.toJson();
        Basket deserialised = JsonConverter.deserialize(json, Basket.class);

        assertThat(deserialised).isEqualTo(sourceBasket);
        String json2 = deserialised.toJson();
        assertThat(json2).isEqualTo(json);
    }

    @Test
    public void canSpecifyMinCountForHasItemWithId() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.hasItemWithId(defaultItemOne.getId())).isTrue();
        assertThat(sourceBasket.hasItemWithId(defaultItemTwo.getId())).isTrue();
        assertThat(sourceBasket.hasItemWithId(defaultItemOne.getId(), defaultItemOne.getCount())).isTrue();
        assertThat(sourceBasket.hasItemWithId(defaultItemOne.getId(), defaultItemOne.getCount() + 1)).isFalse();
    }

    @Test
    public void canFindBasketItemById() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        BasketItem item = sourceBasket.getItemById(defaultItemOne.getId());
        assertThat(item).isEqualTo(defaultItemOne);
    }

    @Test
    public void canFindBasketItemByLabel() {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        BasketItem item = sourceBasket.getItemByLabel(defaultItemOne.getLabel());
        assertThat(item).isEqualTo(defaultItemOne);
    }

    @Test
    public void canIncrementBasketItemCount() {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        int beforeCount = defaultItemOne.getCount();
        sourceBasket.incrementItemCount(defaultItemOne.getId(), 1);

        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getCount()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void canDecrementBasketItemCount() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        int beforeCount = defaultItemOne.getCount();
        sourceBasket.decrementItemCount(defaultItemOne.getId(), 1);

        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getCount()).isEqualTo(beforeCount - 1);
    }

    @Test
    public void cantDecrementPastZeroAndItemRetainedAsZeroCount() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.decrementItemCount(defaultItemTwo.getId(), 1, true);

        assertThat(sourceBasket.getItemById(defaultItemTwo.getId()).getCount()).isEqualTo(0);
    }

    @Test
    public void itemIsRemovedAfterDecrementingToZeroIfRetainNotSet() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.decrementItemCount(defaultItemTwo.getId(), 1);

        assertThat(sourceBasket.hasItemWithId(defaultItemTwo.getId())).isFalse();
    }

    @Test
    public void canSetBasketItemCount() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.setItemCount(defaultItemOne.getId(), 10);

        assertThat(sourceBasket.getItemById(defaultItemOne.getId()).getCount()).isEqualTo(10);
    }

    @Test
    public void canGetTotalNumberOfItems() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.getTotalNumberOfItems()).isEqualTo(defaultItemOne.getCount() + defaultItemTwo.getCount());
    }

    @Test
    public void canGetNumberOfUniqueItems() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.getNumberOfUniqueItems()).isEqualTo(2);
    }

    @Test
    public void canClearItems() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.clearItems();

        assertThat(sourceBasket.getTotalNumberOfItems()).isZero();
    }

    @Test
    public void canRemoveItem() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        sourceBasket.removeItem(defaultItemOne.getId());

        assertThat(sourceBasket.getTotalNumberOfItems()).isEqualTo(1);
        assertThat(sourceBasket.hasItemWithId(defaultItemOne.getId())).isFalse();
    }

    @Test
    public void canGetTotalBasketValue() throws Exception {
        sourceBasket.addItems(defaultItemOne, defaultItemTwo);

        assertThat(sourceBasket.getTotalBasketValue()).isEqualTo(defaultItemOne.getTotalAmount() + defaultItemTwo.getTotalAmount());
    }

    @Test
    public void canGetItemsByCategory() throws Exception {
        sourceBasket.addItems(new BasketItem("123", "Coke", "Drinks", 1000, 1),
                new BasketItem("456", "Fanta", "Drinks", 1000, 1),
                new BasketItem("789", "Pork", "Meat", 1000, 1));

        List<BasketItem> drinks = sourceBasket.getBasketItemsByCategory("Drinks");
        assertThat(drinks).hasSize(2).containsExactlyInAnyOrder(sourceBasket.getItemById("123"), sourceBasket.getItemById("456"));
    }


}
