package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wrapper for any internal data (as in data not passed to external apps) that may need to be passed. Mainly for future proofing.
 */
public class InternalData implements Jsonable {

    private String senderApiVersion;
    private String senderComponentName;
    private Map<String, String> additionalData = new ConcurrentHashMap<>();

    public InternalData(String senderApiVersion) {
        this.senderApiVersion = senderApiVersion;
    }

    public String getSenderApiVersion() {
        return senderApiVersion;
    }

    public String getSenderComponentName() {
        return senderComponentName;
    }

    public void setSenderComponentName(String senderComponentName) {
        this.senderComponentName = senderComponentName;
    }

    public void addAdditionalData(String key, String value) {
        additionalData.put(key, value);
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static InternalData fromJson(String json) {
        return JsonConverter.deserialize(json, InternalData.class);
    }
}
