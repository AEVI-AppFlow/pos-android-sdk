package com.aevi.payment.api.sample.model;


import android.content.Context;

import com.aevi.sdk.flow.FlowApi;
import com.aevi.sdk.flow.FlowClient;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

// To keep the sample simple, we use a singleton context (aka service provider) as opposed to dependency injection.
public class SampleContext {

    private static SampleContext INSTANCE;

    public static SampleContext getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SampleContext(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private final FlowClient flowClient;
    private final PaymentClient paymentClient;

    private PaymentResponse lastReceivedPaymentResponse;

    private SampleContext(Context context) {
        this.flowClient = FlowApi.getFlowClient(context);
        this.paymentClient = PaymentApi.getPaymentClient(context);
    }

    public FlowClient getFlowClient() {
        return flowClient;
    }

    public PaymentClient getPaymentClient() {
        return paymentClient;
    }

    public List<ApiFunction> getApiChoices() {
        List<ApiFunction> apiFunctions = new ArrayList<>();
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.SYSTEM_INFO, "System overview", "Overview of devices, applications and capabilities "));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.FLOW_SERVICES, "List flow services", "Query the API for information about available flow services"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.PAYMENT_SERVICES, "List payment services", "Query the API for information about available payment services"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.DEVICES, "List devices", "Query the API for a list of available devices"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.GENERIC_REQUEST, "Initiate a non-payment request", "Initiate a general financial request, such as reversal or tokenisation"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.INITIATE_PAYMENT, "Initiate a payment", "Choose between a wide range of options to initiate a specific payment"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.SUBSCRIBE_EVENTS, "Subscribe to events", "Subscribe to general system events"));
        return apiFunctions;
    }

    public PaymentResponse getLastReceivedPaymentResponse() {
        return lastReceivedPaymentResponse;
    }

    public void setLastReceivedPaymentResponse(PaymentResponse lastReceivedPaymentResponse) {
        this.lastReceivedPaymentResponse = lastReceivedPaymentResponse;
    }

}
