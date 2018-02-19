package com.aevi.sdk.flow.model;


public class Event {

    public static final String EVENT_DEVICE_ADDED = "deviceAdded";
    public static final String EVENT_DEVICE_REMOVED = "deviceRemoved";
    public static final String EVENT_PAYMENT_SERVICE_ADDED = "paymentServiceAdded";
    public static final String EVENT_PAYMENT_SERVICE_REMOVED = "paymentServiceRemoved";
    public static final String EVENT_FLOW_SERVICE_ADDED = "flowServiceAdded";
    public static final String EVENT_FLOW_SERVICE_REMOVED = "flowServiceRemoved";

    private final String type;
    private final String data;

    public Event(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
