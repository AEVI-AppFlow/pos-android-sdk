package com.aevi.sdk.pos.flow.paymentinitiationsample.model;

import android.util.Log;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.PaymentClient;

import java.util.ArrayList;
import java.util.List;

public class SystemEventHandler {

    private List<FlowEvent> receivedFlowEvents = new ArrayList<>();

    public void subscribeToEvents(PaymentClient paymentClient) {
        paymentClient.subscribeToSystemEvents().subscribe(flowEvent -> {
            Log.d("Andreas", "received flow event: " + flowEvent.getType());
            receivedFlowEvents.add(flowEvent);
        });
    }

    public List<FlowEvent> getReceivedFlowEvents() {
        return receivedFlowEvents;
    }
}
