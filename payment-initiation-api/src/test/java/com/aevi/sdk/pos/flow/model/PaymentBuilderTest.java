package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Token;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PaymentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenAmountsNotSet() throws Exception {
        new PaymentBuilder().withPaymentFlow("whatever").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenTransactionTypeNotSet() throws Exception {
        new PaymentBuilder().withAmounts(new Amounts(1000, "GBP")).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCardTokenAndSplitSetTogether() throws Exception {
        new PaymentBuilder()
                .withPaymentFlow("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .withSplitEnabled(true)
                .withCardToken(new Token("123", "card"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowBasketAndAmountsTotalMismatch() throws Exception {
        Basket basket = new Basket(new BasketItemBuilder().generateRandomId().withLabel("bla").withAmount(900).build());
        new PaymentBuilder()
                .withPaymentFlow("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, basket)
                .build();
    }

    @Test
    public void shouldBuildPaymentWithCorrectSetup() throws Exception {
        Basket basket = new Basket(new BasketItemBuilder().generateRandomId().withLabel("bla").withAmount(1000).build());
        Payment payment = new PaymentBuilder()
                .withPaymentFlow("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, basket)
                .withSplitEnabled(true)
                .build();

        assertThat(payment.getFlowName()).isEqualTo("sale");
        assertThat(payment.getAmounts()).isEqualTo(new Amounts(1000, "GBP"));
        assertThat(payment.isSplitEnabled()).isTrue();
        assertThat(payment.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_BASKET)).isNotNull();
    }
}
