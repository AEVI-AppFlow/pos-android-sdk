package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeCount() throws Exception {
        new BasketItemBuilder().withLabel("bla").withCount(-1).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfIdIsNull() throws Exception {
        new BasketItemBuilder().withId(null).withCount(1).withLabel("bla").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLabelNull() throws Exception {
        new BasketItemBuilder().withCount(1).build();
    }

    @Test
    public void shouldInitialiseWithDefaultRandomId() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withLabel("banana").build();
        assertThat(basketItem.getId()).isNotEmpty();
    }

    @Test
    public void shouldCreateCorrectBasketItem() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        assertThat(basketItem.getId()).isEqualTo("123");
        assertThat(basketItem.getLabel()).isEqualTo("banana");
        assertThat(basketItem.getCategory()).isEqualTo("fruit");
        assertThat(basketItem.getCount()).isEqualTo(2);
        assertThat(basketItem.getIndividualAmount()).isEqualTo(200);
        assertThat(basketItem.getTotalAmount()).isEqualTo(400);
    }

    @Test
    public void shouldAllowCreationFromExistingItem() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).withCount(4).build();

        assertThat(newItem.getId()).isEqualTo("123");
        assertThat(newItem.getLabel()).isEqualTo("banana");
        assertThat(newItem.getCategory()).isEqualTo("fruit");
        assertThat(newItem.getCount()).isEqualTo(4);
        assertThat(newItem.getIndividualAmount()).isEqualTo(200);
        assertThat(newItem.getTotalAmount()).isEqualTo(800);
    }

    @Test
    public void shouldAllowIncrementItemCount() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).incrementCount().build();
        assertThat(newItem.getCount()).isEqualTo(3);
    }

    @Test
    public void shouldAllowDecrementItemCount() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).decrementCount().build();
        assertThat(newItem.getCount()).isEqualTo(1);
    }

    @Test
    public void shouldAllowOffsetPositiveItemCount() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).offsetCountBy(2).build();
        assertThat(newItem.getCount()).isEqualTo(4);
    }

    @Test
    public void shouldAllowOffsetNegativeItemCount() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withId("123").withLabel("banana").withCount(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).offsetCountBy(-1).build();
        assertThat(newItem.getCount()).isEqualTo(1);
    }

}
