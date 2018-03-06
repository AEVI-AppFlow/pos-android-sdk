package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemTest {

    @Test
    public void checkLabels() {
        BasketItem item = getBasketItem();
        assertThat(item.getLabel()).isEqualTo("Pandoras Box");
        assertThat(item.getCategory()).isEqualTo("Greek Myths");
        assertThat(item.getCount()).isEqualTo(1);
    }

    @Test
    public void checkEquals() {
        BasketItem item1 = getBasketItem();
        BasketItem item2 = getBasketItem();
        BasketItem item3 = getBasketItem2();
        assertThat(item1.equals(item2)).isTrue();
        assertThat(item1.equals(item3)).isFalse();
        assertThat(item3.equals(item1)).isFalse();
    }

    @Test
    public void canAddOneToItem() {
        BasketItem item = getBasketItem();

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
        BasketItem item = getBasketItem();

        assertThat(item.getIndividualAmount()).isEqualTo(100000);
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
        BasketItem item = getBasketItem();

        item.removeOne();
        assertThat(item.getCount()).isEqualTo(0);
    }

    @Test
    public void cannotGoBelowZeroCount() {
        BasketItem item = getBasketItem();

        item.removeOne();
        item.removeOne();
        item.removeOne();
        item.removeOne();
        item.removeOne();
        assertThat(item.getCount()).isEqualTo(0);
    }

    private void assertValues(BasketItem item, long amtInd, long total) {
        assertThat(item.getIndividualAmount()).isEqualTo(amtInd);
        assertThat(item.getTotalAmount()).isEqualTo(total);
    }

    private BasketItem getBasketItem() {
        return new BasketItem(1, "Pandoras Box", "Greek Myths", 100000);
    }

    private BasketItem getBasketItem2() {
        return new BasketItem(1, "Trojan Horse", "Greek Myths", 10000);
    }
}
