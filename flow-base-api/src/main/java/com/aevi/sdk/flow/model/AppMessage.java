package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * Application message data for use between PCS and applications it calls.
 */
public class AppMessage implements Jsonable {

    public static final String EMPTY_DATA = "{}";

    private final String messageType; // See AppMessageTypes
    private final String messageData; // The message data in JSON
    private String internalData; // Data that may be useful for internal use, such as API version, etc

    public AppMessage(String messageType, String messageData, InternalData internalData) {
        this.messageType = messageType;
        this.messageData = messageData;
        setInternalData(internalData);
    }

    public AppMessage(String messageType, String messageData) {
        this(messageType, messageData, null);
    }

    public AppMessage(String messageType, InternalData internalData) {
        this(messageType, null, internalData);
    }

    public AppMessage(String messageType) {
        this(messageType, null, null);
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageData() {
        return messageData != null ? messageData : EMPTY_DATA;
    }

    private void setInternalData(InternalData internalData) {
        this.internalData = internalData != null ? internalData.toJson() : null;
    }

    public void updateInternalData(InternalData internalData) {
        setInternalData(internalData);
    }

    public InternalData getInternalData() {
        return internalData != null ? InternalData.fromJson(internalData) : null;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static AppMessage fromJson(String json) {
        return JsonConverter.deserialize(json, AppMessage.class);
    }
}
