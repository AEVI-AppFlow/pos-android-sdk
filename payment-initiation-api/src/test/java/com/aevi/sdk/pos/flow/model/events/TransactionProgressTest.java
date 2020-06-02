package com.aevi.sdk.pos.flow.model.events;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.CardBuilder;

import org.junit.Test;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionProgressTest {
    @Test
    public void canSerialiseAsPayload() {
        Amounts amounts = new Amounts(3662, "GBP");
        Card card = new CardBuilder().withCardholderName("Bob").withMaskedPan("PeterPan").build();
        AdditionalData refs = new AdditionalData();
        refs.addData("referreee", "offside");

        TransactionProgress transactionProgress = new TransactionProgress(amounts, card, refs);
        FlowEvent fe = new FlowEvent(EVENT_TRANSACTION_PROGRESS, transactionProgress);

        String json = fe.toJson();
        System.out.println(json);
        FlowEvent result = FlowEvent.fromJson(json);

        TransactionProgress resultData = result.getEventData(TransactionProgress.class);
        assertThat(resultData).isEqualTo(transactionProgress);
    }
}
