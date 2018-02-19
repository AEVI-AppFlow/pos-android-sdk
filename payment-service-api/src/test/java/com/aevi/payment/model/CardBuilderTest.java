package com.aevi.payment.model;


import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CardBuilderTest {

    @Test
    public void canConstructCard() throws Exception {
        com.aevi.sdk.pos.flow.model.Card card = new com.aevi.sdk.pos.flow.model.CardBuilder()
                .withPan("1234 XXXX XXXX 5678")
                .withExpiryDate("12/20")
                .withCardholderName("Mr T")
                .withCardToken(new com.aevi.sdk.flow.model.Token("abcdefgh12345678", "card", "blaha"))
                .withAdditionalData("blaha", "bluhu")
                .build();
        card.getCardToken().setPaymentServiceId("123");

        assertThat(card.getMaskedPan()).isEqualTo("1234 XXXX XXXX 5678");
        assertThat(card.getExpiryDate()).isEqualTo("12/20");
        assertThat(card.getCardholderName()).isEqualTo("Mr T");
        assertThat(card.getCardToken().getValue()).isEqualTo("abcdefgh12345678");
        assertThat(card.getCardToken().getSource()).isEqualTo("card");
        assertThat(card.getCardToken().getAlgorithm()).isEqualTo("blaha");
        assertThat(card.getAdditionalData().getValue("blaha")).isEqualTo("bluhu");
        assertThat(card.getCardToken().getPaymentServiceId()).isEqualTo("123");
    }
}
