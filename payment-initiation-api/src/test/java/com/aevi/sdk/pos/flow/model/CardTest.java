package com.aevi.sdk.pos.flow.model;


import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CardTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowPanWithMoreThan10Digits() throws Exception {
        new Card("1234567890123456", "Mr T", "0102", null, null);
    }

    @Test
    public void shouldAllowPanWithLessThan10Digits() throws Exception {
        Card card = new Card("123456789XXXXXX", "Mr T", "0102", null, null);
        assertThat(card.getMaskedPan()).isEqualTo("123456789XXXXXX");
    }

    @Test
    public void shouldFormatExpiryDateCorrectly() throws Exception {
        Card card = new Card("123456789XXXXXX", "Mr T", "2010", null, null);
        assertThat(card.getFormattedExpiryDate("MM/yyyy")).isEqualTo("10/2020");
    }
}
