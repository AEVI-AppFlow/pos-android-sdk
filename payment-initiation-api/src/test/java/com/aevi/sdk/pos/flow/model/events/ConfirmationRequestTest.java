package com.aevi.sdk.pos.flow.model.events;

import com.aevi.sdk.flow.model.FlowEvent;

import org.junit.Test;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.EVENT_CONFIRMATION_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationRequestTest {

    @Test
    public void canSerialiseAsPayload() {
        ConfirmationRequest confirmationRequest = new ConfirmationRequest("apple");
        confirmationRequest.setTitleText("Would you like a banana?");
        confirmationRequest.setConfirmationOptions(new ConfirmationOption[]{
                new ConfirmationOption("yes", "Yes please"),
                new ConfirmationOption("no", "No thanks")
        }, false);
        FlowEvent fe = new FlowEvent(EVENT_CONFIRMATION_REQUEST, confirmationRequest);

        String json = fe.toJson();
        System.out.println(json);
        FlowEvent result = FlowEvent.fromJson(json);

        ConfirmationRequest resultData = result.getEventData(ConfirmationRequest.class);
        assertThat(resultData).isEqualTo(confirmationRequest);
        assertThat(resultData.getConfirmationOptions()).hasSize(2);

    }
}
