package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AmountsModifierTest {

    @Test
    public void shouldUpdateAmountsCorrectly() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount("cashback", 25);

        Amounts updated = new AmountsModifier(original)
                .updateBaseAmount(50)
                .setAdditionalAmount("cashback", 20)
                .setAdditionalAmount("tip", 15)
                .build();

        assertThat(updated.getBaseAmountValue()).isEqualTo(50);
        assertThat(updated.getAdditionalAmountValue("cashback")).isEqualTo(20);
        assertThat(updated.getAdditionalAmountValue("tip")).isEqualTo(15);
    }

    @Test
    public void shouldIgnoreNegativeAmounts() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount("cashback", 25);

        Amounts updated = new AmountsModifier(original)
                .updateBaseAmount(-50)
                .setAdditionalAmount("cashback", -20)
                .build();

        assertThat(updated).isEqualTo(original);
    }

    @Test
    public void shouldConvertAmountsCorrectly() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount("cashback", 25);

        double exchangeRate = 0.5;
        Amounts updated = new AmountsModifier(original).changeCurrency("USD", exchangeRate).build();

        assertThat(updated.getCurrency()).isEqualTo("USD");
        assertThat(updated.getOriginalCurrency()).isEqualTo("GBP");
        assertThat(updated.getCurrencyExchangeRate()).isEqualTo(exchangeRate);
        assertThat(updated.getBaseAmountValue()).isEqualTo(50);
        assertThat(updated.getAdditionalAmountValue("cashback")).isEqualTo(12);
    }

    @Test
    public void canOffsetWithNegativeAmounts() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        Amounts updated = new AmountsModifier(original)
                .offsetBaseAmount(-50)
                .build();

        assertThat(updated.getBaseAmountValue()).isEqualTo(50);
    }
}
