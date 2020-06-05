package com.aevi.sdk.pos.flow.model.events;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.model.Amounts;

import org.junit.Test;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.EVENT_FINAL_AMOUNT_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class FinalAmountRequestTest {

    @Test
    public void checkAmountsSet() {
        FinalAmountResponse finalAmount = new FinalAmountResponse(new Amounts(2772, "GBP"));

        assertThat(finalAmount.getAmounts().getBaseAmountValue()).isEqualTo(2772);
        assertThat(finalAmount.getAmounts().getCurrency()).isEqualTo("GBP");
    }

    @Test
    public void canSerialiseAsEventPayload() {
        FinalAmountResponse finalAmount = new FinalAmountResponse(new Amounts(1000, "GBP"));
        FlowEvent fe = new FlowEvent(EVENT_FINAL_AMOUNT_REQUEST, finalAmount);

        String json = fe.toJson();
        System.out.println(json);
        FlowEvent result = FlowEvent.fromJson(json);

        FinalAmountResponse resultFinalAmount = result.getEventData(FinalAmountResponse.class);
        assertThat(resultFinalAmount.getAmounts()).isEqualTo(finalAmount.getAmounts());
        assertThat(resultFinalAmount.getType()).isEqualTo(finalAmount.getType());
    }
}
