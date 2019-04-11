package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemModifierBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoName() throws Exception {
        new BasketItemModifierBuilder(null, "bananas")
                .withAmount(100)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoType() throws Exception {
        new BasketItemModifierBuilder("type", null)
                .withAmount(100)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoAmountOrPercentage() throws Exception {
        new BasketItemModifierBuilder("bananas", "type")
                .build();
    }

    @Test
    public void shouldAllowJustAmount() throws Exception {
        BasketItemModifier bim = new BasketItemModifierBuilder("bananas", "type")
                .withAmount(100)
                .build();
        assertBasketItemModifier(bim, null, "bananas", "type", 100L, 100f, null);
    }

    @Test
    public void shouldAllowJustPercentage() throws Exception {
        BasketItemModifier bim = new BasketItemModifierBuilder("bananas", "type")
                .withPercentage(10.2f)
                .build();
        assertBasketItemModifier(bim, null, "bananas", "type", null, null, 10.2f);
    }

    @Test
    public void shouldAllowAllValues() throws Exception {
        BasketItemModifier bim = new BasketItemModifierBuilder("bananas", "type")
                .withId("idee")
                .withAmount(101)
                .withPercentage(10.2f)
                .build();
        assertBasketItemModifier(bim, "idee", "bananas", "type", 101L, 101f, 10.2f);
    }

    @Test
    public void shouldAllowFractionalAmount() throws Exception {
        BasketItemModifier bim = new BasketItemModifierBuilder("bananas", "type")
                .withFractionalAmount(10.165443f)
                .build();
        assertBasketItemModifier(bim, null, "bananas", "type", 10L, 10.165443f, null);
    }

    private void assertBasketItemModifier(BasketItemModifier bim, String id, String name, String type, Long amount, Float fractionalAmount,
                                          Float percentage) {
        assertThat(bim.getId()).isEqualTo(id);
        assertThat(bim.getName()).isEqualTo(name);
        assertThat(bim.getType()).isEqualTo(type);
        assertThat(bim.getAmount()).isEqualTo(amount);
        assertThat(bim.getFractionalAmount()).isEqualTo(fractionalAmount);
        assertThat(bim.getPercentage()).isEqualTo(percentage);
    }
}
