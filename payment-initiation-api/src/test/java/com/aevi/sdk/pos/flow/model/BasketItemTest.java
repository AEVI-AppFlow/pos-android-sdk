package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.pos.flow.model.BasketItem;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemTest {

    @Test
    public void checkLabels() {
        com.aevi.sdk.pos.flow.model.BasketItem item = getBasketItem();
        assertThat(item.getLabel()).isEqualTo("Pandoras Box");
        assertThat(item.getCategory()).isEqualTo("Greek Myths");
        assertThat(item.getCount()).isEqualTo(1);
    }

    @Test
    public void checkEquals() {
        com.aevi.sdk.pos.flow.model.BasketItem item1 = getBasketItem();
        com.aevi.sdk.pos.flow.model.BasketItem item2 = getBasketItem();
        com.aevi.sdk.pos.flow.model.BasketItem item3 = getBasketItem2();
        assertThat(item1.equals(item2)).isTrue();
        assertThat(item1.equals(item3)).isFalse();
        assertThat(item3.equals(item1)).isFalse();
    }

    @Test
    public void canAddOneToItem() {
        com.aevi.sdk.pos.flow.model.BasketItem item = getBasketItem();

        item.addOne();
        assertThat(item.getCount()).isEqualTo(2);

        item.addOne();
        item.addOne();
        assertThat(item.getCount()).isEqualTo(4);
    }

    @Test
    public void totalsAreCorrect() {
        BasketItem item = getBasketItem();

        assertValues(item, 100000, 100000);
        item.addOne();
        assertValues(item, 100000, 200000);
        item.addOne();
        assertValues(item, 100000, 300000);
    }

    @Test
    public void totalsAreCorrectForZero() {
        com.aevi.sdk.pos.flow.model.BasketItem item = getBasketItem();

        assertThat(item.getIndividualAmount()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amount(100000, "GBP"));
        item.removeOne();
        assertValues(item, 100000, 0);
        item.removeOne();
        item.removeOne();
        item.removeOne();
        item.removeOne();
        assertValues(item, 100000, 0);
    }

    @Test
    public void canSubtractOneFromItem() {
        com.aevi.sdk.pos.flow.model.BasketItem item = getBasketItem();

        item.removeOne();
        assertThat(item.getCount()).isEqualTo(0);
    }

    @Test
    public void cannotGoBelowZeroCount() {
        com.aevi.sdk.pos.flow.model.BasketItem item = getBasketItem();

        item.removeOne();
        item.removeOne();
        item.removeOne();
        item.removeOne();
        item.removeOne();
        assertThat(item.getCount()).isEqualTo(0);
    }

    private void assertValues(com.aevi.sdk.pos.flow.model.BasketItem item, long amtInd, long total) {
        assertThat(item.getIndividualAmount()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amount(amtInd, "GBP"));
        assertThat(item.getTotalAmount()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amount(total, "GBP"));
    }

    private com.aevi.sdk.pos.flow.model.BasketItem getBasketItem() {
        return new com.aevi.sdk.pos.flow.model.BasketItem(1, "Pandoras Box", "Greek Myths", new com.aevi.sdk.pos.flow.model.Amount(100000, "GBP"));
    }

    private com.aevi.sdk.pos.flow.model.BasketItem getBasketItem2() {
        return new com.aevi.sdk.pos.flow.model.BasketItem(1, "Trojan Horse", "Greek Myths", new com.aevi.sdk.pos.flow.model.Amount(10000, "GBP"));
    }
}
