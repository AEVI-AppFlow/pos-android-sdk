package com.aevi.sdk.pos.flow.model.config;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.PaymentClient;

import org.junit.Test;

import java.util.List;

import io.reactivex.functions.Consumer;

public class PaymentSettingsTest {

    @Test
    public void name() throws Exception {
        PaymentSettings paymentSettings = null;
        PaymentClient paymentClient = null;

        if (paymentSettings.isFlowTypeSupported("tokenisation")) {
            String flow = chooseFlow(paymentSettings.getFlowNamesForType("tokenisation"));
            Request request = new Request(flow);
            paymentClient.initiateRequest(request)
                    .subscribe(new Consumer<Response>() {
                        @Override
                        public void accept(Response response) throws Exception {
                            // Implement
                        }
                    });
        }
    }

    private String chooseFlow(List<String> flows) {
        return "";
    }
}
