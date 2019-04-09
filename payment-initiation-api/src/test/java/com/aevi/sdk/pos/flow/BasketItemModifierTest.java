package com.aevi.sdk.pos.flow;

import com.aevi.sdk.pos.flow.model.BasketItemModifier;
import org.junit.Test;

public class BasketItemModifierTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoName() throws Exception {
        new BasketItemModifier("bla", null, "type", 100L, 2.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoType() throws Exception {
        new BasketItemModifier("bla", "name", null, 100L, 2.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoAmountOrPercentage() throws Exception {
        new BasketItemModifier("bla", "name", "type", null, null);
    }

    @Test
    public void shouldAcceptJustAmount() throws Exception {
        new BasketItemModifier("bla", "name", "type", 100L, null);
    }

    @Test
    public void shouldAcceptJustPercentage() throws Exception {
        new BasketItemModifier("bla", "name", "type", null, 2.5f);
    }

    @Test
    public void shouldBuildFullInstance() throws Exception {
        new BasketItemModifier("bla", "name", "type", 100L, 2.5f);
    }
}
