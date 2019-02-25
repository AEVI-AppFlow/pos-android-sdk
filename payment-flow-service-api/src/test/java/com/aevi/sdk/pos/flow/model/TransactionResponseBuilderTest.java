package com.aevi.sdk.pos.flow.model;


import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransactionResponseBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfIdNotSet() throws Exception {
        new TransactionResponseBuilder(null).approve().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutcomeNotSet() throws Exception {
        new TransactionResponseBuilder("1234").build();
    }

    @Test
    public void checkIsValid() {
        TransactionResponseBuilder trb = new TransactionResponseBuilder("1234");

        assertThat(trb.isValid()).isFalse();

        trb.approve();

        assertThat(trb.isValid()).isTrue();
    }
}
