package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountsTest {

    @Test
    public void checkTotalIsCorrect() {
        Amounts amounts = new Amounts(1000L, "GBP");
        amounts.addAdditionalAmount("one", 1500L);
        amounts.addAdditionalAmount("two", 2000L);
        assertThat(amounts.getBaseAmountValue()).isEqualTo(1000L);
        assertThat(amounts.getTotalAmountValue()).isEqualTo(4500L);
    }

    @Test
    public void checkCurrencyIsCorrect() {
        Amounts amounts = new Amounts(1000L, "GBP");

        assertThat(amounts.getCurrency()).isEqualTo("GBP");
    }

    @Test
    public void checkAddAmounts() throws Exception {
        Amounts amount1 = new Amounts(1000L, "GBP");
        amount1.addAdditionalAmount("bob", 150L);
        amount1.addAdditionalAmount("susan", 300L);
        Amounts amount2 = new Amounts(350L, "GBP");
        amount2.addAdditionalAmount("bob", 250L);
        amount2.addAdditionalAmount("harry", 120L);

        Amounts result = Amounts.addAmounts(amount1, amount2);
        assertThat(result.getCurrency()).isEqualTo("GBP");
        assertThat(result.getBaseAmountValue()).isEqualTo(1350L);
        assertThat(result.getTotalAmountValue()).isEqualTo(2170L);
        assertThat(result.getAdditionalAmountValue("bob")).isEqualTo(400L);
        assertThat(result.getAdditionalAmountValue("susan")).isEqualTo(300L);
        assertThat(result.getAdditionalAmountValue("harry")).isEqualTo(120L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkCantAddAmountsDifferentCurrency() throws Exception {
        Amounts.addAmounts(new Amounts(10L, "GBP"), new Amounts(10L, "USD"));
    }

    @Test
    public void checkSubtractAmounts() throws Exception {
        Amounts amount1 = new Amounts(1000L, "GBP");
        amount1.addAdditionalAmount("bob", 450L);
        amount1.addAdditionalAmount("susan", 300L);
        Amounts amount2 = new Amounts(350L, "GBP");
        amount2.addAdditionalAmount("bob", 250L);
        amount2.addAdditionalAmount("harry", 120L);

        Amounts result = Amounts.subtractAmounts(amount1, amount2);
        assertThat(result.getCurrency()).isEqualTo("GBP");
        assertThat(result.getBaseAmountValue()).isEqualTo(650L);
        assertThat(result.getTotalAmountValue()).isEqualTo(1150L);
        assertThat(result.getAdditionalAmountValue("bob")).isEqualTo(200L);
        assertThat(result.getAdditionalAmountValue("susan")).isEqualTo(300L);
        assertThat(result.getAdditionalAmountValue("harry")).isEqualTo(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkCantSubtractAmountsDifferentCurrency() throws Exception {
        Amounts.subtractAmounts(new Amounts(10L, "GBP"), new Amounts(10L, "USD"));
    }
}
