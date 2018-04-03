package com.aevi.sdk.pos.flow.paymentinitiationsample;


public class ApiFunction {

    public enum ApiMethod {
        SYSTEM_INFO,
        DEVICES,
        FLOW_SERVICES,
        SUBSCRIBE_EVENTS,
        PAYMENT_SERVICES,
        GENERIC_REQUEST,
        INITIATE_PAYMENT
    }

    private final ApiMethod apiMethod;
    private final String name;
    private final String description;

    public ApiFunction(ApiMethod apiMethod, String name, String description) {
        this.apiMethod = apiMethod;
        this.name = name;
        this.description = description;
    }

    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
