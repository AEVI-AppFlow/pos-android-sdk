package com.aevi.sdk.flow.service;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

public class FlowServiceException extends RuntimeException implements Jsonable {

    private String errorCode;

    public FlowServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowServiceException fromJson(String json) {
        return JsonConverter.deserialize(json, FlowServiceException.class);
    }
}
