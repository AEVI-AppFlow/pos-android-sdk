package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

public class BasketItemModifierTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoName() throws Exception {
        new BasketItemModifier("bla", null, "type", 100f, 2.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoType() throws Exception {
        new BasketItemModifier("bla", "name", null, 100f, 2.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoAmountOrPercentage() throws Exception {
        new BasketItemModifier("bla", "name", "type", null, null);
    }

    @Test
    public void shouldAcceptJustAmount() throws Exception {
        new BasketItemModifier("bla", "name", "type", 100f, null);
    }

    @Test
    public void shouldAcceptJustPercentage() throws Exception {
        new BasketItemModifier("bla", "name", "type", null, 2.5f);
    }

    @Test
    public void shouldBuildFullInstance() throws Exception {
        new BasketItemModifier("bla", "name", "type", 100f, 2.5f);
    }
}
