package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountsTest {

    @Test
    public void checkTotalIsCorrect() {
        com.aevi.sdk.pos.flow.model.Amounts amounts = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 1000L, "GBP");

        assertThat(amounts.getBaseAmountValue()).isEqualTo(1000L);
        assertThat(amounts.getOtherAmountValue()).isEqualTo(1000L);
        assertThat(amounts.getTipAmountValue()).isEqualTo(1000L);
        assertThat(amounts.getTotalAmountValue()).isEqualTo(3000L);
    }

    @Test
    public void checkCurrencyIsCorrect() {
        com.aevi.sdk.pos.flow.model.Amounts amounts = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 1000L, "GBP");

        assertThat(amounts.getCurrency()).isEqualTo("GBP");
    }

    @Test
    public void checkEquals() {
        com.aevi.sdk.pos.flow.model.Amounts amountUK = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 1000L, "GBP");
        com.aevi.sdk.pos.flow.model.Amounts amountUS = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 1000L, "USD");
        com.aevi.sdk.pos.flow.model.Amounts amount2 = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 0, 1000L, "GBP");
        com.aevi.sdk.pos.flow.model.Amounts amount3 = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 0, "GBP");
        com.aevi.sdk.pos.flow.model.Amounts amountUKMatch = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 1000L, 1000L, "GBP");

        assertThat(amountUK.equals(amountUS)).isFalse();
        assertThat(amount2.equals(amountUK)).isFalse();
        assertThat(amount3.equals(amountUK)).isFalse();
        assertThat(amountUKMatch.equals(amountUK)).isTrue();
    }

    @Test
    public void checkTotalOkWithoutAll() {
        com.aevi.sdk.pos.flow.model.Amounts amounts = new com.aevi.sdk.pos.flow.model.Amounts(1000L, "GBP");

        assertThat(amounts.getTotalAmountValue()).isEqualTo(1000L);
    }

    @Test
    public void checkAddAmounts() throws Exception {
        com.aevi.sdk.pos.flow.model.Amounts amount1 = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 0, 100L, "GBP");
        com.aevi.sdk.pos.flow.model.Amounts amount2 = new com.aevi.sdk.pos.flow.model.Amounts(350L, 50L, 0, "GBP");

        com.aevi.sdk.pos.flow.model.Amounts result = com.aevi.sdk.pos.flow.model.Amounts.addAmounts(amount1, amount2);
        assertThat(result.getBaseAmountValue()).isEqualTo(1350L);
        assertThat(result.getOtherAmountValue()).isEqualTo(100L);
        assertThat(result.getTipAmountValue()).isEqualTo(50L);
        assertThat(result.getTotalAmountValue()).isEqualTo(1500L);
    }

    @Test
    public void checkSubtractAmounts() throws Exception {
        com.aevi.sdk.pos.flow.model.Amounts amount1 = new com.aevi.sdk.pos.flow.model.Amounts(1000L, 0, 100L, "GBP");
        com.aevi.sdk.pos.flow.model.Amounts amount2 = new com.aevi.sdk.pos.flow.model.Amounts(350L, 50L, 0, "GBP");

        com.aevi.sdk.pos.flow.model.Amounts result = com.aevi.sdk.pos.flow.model.Amounts.subtractAmounts(amount1, amount2);
        assertThat(result.getBaseAmountValue()).isEqualTo(650L);
        assertThat(result.getOtherAmountValue()).isEqualTo(100L);
        assertThat(result.getTipAmountValue()).isEqualTo(0L);
        assertThat(result.getTotalAmountValue()).isEqualTo(750L);
    }
}
