package com.aevi.sdk.pos.flow.model.events;

import com.aevi.sdk.flow.model.FlowEvent;

import org.junit.Test;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.EVENT_RECEIPT;
import static org.assertj.core.api.Assertions.assertThat;

public class ReceiptTest {

    @Test
    public void canSerialiseAsPayload() {
        String receiptText = "{ \"blah\": \"blah\" }";
        Receipt receipt = new Receipt("customer", receiptText);
        receipt.setReceiptData("json", receiptText);
        FlowEvent fe = new FlowEvent(EVENT_RECEIPT, receipt);

        String json = fe.toJson();
        System.out.println(json);
        FlowEvent result = FlowEvent.fromJson(json);

        Receipt resultData = result.getEventData(Receipt.class);
        assertThat(resultData).isEqualTo(receipt);
    }
}
