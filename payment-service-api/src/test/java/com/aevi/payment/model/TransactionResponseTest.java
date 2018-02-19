package com.aevi.payment.model;


import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.model.TransactionResponseBuilder;

import org.junit.Test;

import static com.aevi.sdk.pos.flow.model.TransactionResponse.Outcome.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionResponseTest {

    @Test
    public void canSerializeTransactionResponse() {
        com.aevi.sdk.flow.model.AdditionalData refs = new com.aevi.sdk.flow.model.AdditionalData();
        refs.addData("AID", "3030100000000");
        com.aevi.sdk.pos.flow.model.TransactionResponse response = new TransactionResponseBuilder("12345")
                .withOutcome(APPROVED)
                .withResponseCode("F8")
                .withAmountsProcessed(new com.aevi.sdk.pos.flow.model.Amounts(1000, "GBP"))
                .withReferences(refs)
                .withCard(new com.aevi.sdk.pos.flow.model.CardBuilder()
                        .withPan("************4563")
                        .build())
                .build();

        String json = response.toJson();

        TransactionResponse deserialised = com.aevi.sdk.pos.flow.model.TransactionResponse.fromJson(json);
        assertThat(deserialised).isEqualTo(response);
    }

}
