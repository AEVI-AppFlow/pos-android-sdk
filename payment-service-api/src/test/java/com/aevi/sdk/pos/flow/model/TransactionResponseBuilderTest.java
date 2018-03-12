package com.aevi.sdk.pos.flow.model;


import org.junit.Test;

public class TransactionResponseBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfIdNotSet() throws Exception {
        new TransactionResponseBuilder(null).withOutcome(TransactionResponse.Outcome.APPROVED).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutcomeNotSet() throws Exception {
        new TransactionResponseBuilder("1234").build();
    }
}
