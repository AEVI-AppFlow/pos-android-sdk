package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.constants.AmountIdentifiers;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AmountsModifierTest {

    @Test
    public void shouldUpdateAmountsCorrectly() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount(AmountIdentifiers.AMOUNT_CASHBACK, 25);

        Amounts updated = new AmountsModifier(original)
                .updateBaseAmount(50)
                .setAdditionalAmount(AmountIdentifiers.AMOUNT_CASHBACK, 20)
                .setAdditionalAmount(AmountIdentifiers.AMOUNT_TIP, 15)
                .build();

        assertThat(updated.getBaseAmountValue()).isEqualTo(50);
        assertThat(updated.getAdditionalAmountValue(AmountIdentifiers.AMOUNT_CASHBACK)).isEqualTo(20);
        assertThat(updated.getAdditionalAmountValue(AmountIdentifiers.AMOUNT_TIP)).isEqualTo(15);
    }

    @Test
    public void shouldIgnoreNegativeAmounts() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount(AmountIdentifiers.AMOUNT_CASHBACK, 25);

        Amounts updated = new AmountsModifier(original)
                .updateBaseAmount(-50)
                .setAdditionalAmount(AmountIdentifiers.AMOUNT_CASHBACK, -20)
                .build();

        assertThat(updated).isEqualTo(original);
    }

    @Test
    public void shouldConvertAmountsCorrectly() throws Exception {
        Amounts original = new Amounts(100, "GBP");
        original.addAdditionalAmount(AmountIdentifiers.AMOUNT_CASHBACK, 25);

        double exchangeRate = 0.5;
        Amounts updated = new AmountsModifier(original).changeCurrency("USD", exchangeRate).build();

        assertThat(updated.getCurrency()).isEqualTo("USD");
        assertThat(updated.getOriginalCurrency()).isEqualTo("GBP");
        assertThat(updated.getCurrencyExchangeRate()).isEqualTo(exchangeRate);
        assertThat(updated.getBaseAmountValue()).isEqualTo(50);
        assertThat(updated.getAdditionalAmountValue(AmountIdentifiers.AMOUNT_CASHBACK)).isEqualTo(12);
    }
}
