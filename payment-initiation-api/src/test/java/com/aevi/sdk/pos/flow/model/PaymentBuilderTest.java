package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Token;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PaymentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenAmountsNotSet() throws Exception {
        new PaymentBuilder().withTransactionType("whatever").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenTransactionTypeNotSet() throws Exception {
        new PaymentBuilder().withAmounts(new Amounts(1000, "GBP")).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCardTokenAndSplitSetTogether() throws Exception {
        new PaymentBuilder()
                .withTransactionType("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .enableSplit()
                .withCardToken(new Token("123", "card"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowBasketAndAmountsTotalMismatch() throws Exception {
        Basket basket = new Basket();
        basket.addItem(new BasketItem("an item", 900));
        new PaymentBuilder()
                .withTransactionType("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, basket)
                .build();
    }

    @Test
    public void shouldBuildPaymentWithCorrectSetup() throws Exception {
        Basket basket = new Basket();
        basket.addItem(new BasketItem("an item", 1000));
        Payment payment = new PaymentBuilder()
                .withTransactionType("sale")
                .withAmounts(new Amounts(1000, "GBP"))
                .addAdditionalData(AdditionalDataKeys.DATA_KEY_BASKET, basket)
                .enableSplit()
                .build();

        assertThat(payment.getTransactionType()).isEqualTo("sale");
        assertThat(payment.getAmounts()).isEqualTo(new Amounts(1000, "GBP"));
        assertThat(payment.isSplitEnabled()).isTrue();
        assertThat(payment.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_BASKET)).isNotNull();
    }
}
