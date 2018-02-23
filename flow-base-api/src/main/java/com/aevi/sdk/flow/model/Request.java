package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;

import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Generic request that contains a request type and arbitrary data bespoke to each request type.
 */
public class Request extends BaseModel {

    private final String requestType;
    private final AdditionalData requestData;
    private String targetAppId;

    /**
     * Initialise this request with a request type and empty data.
     *
     * The request type indicates how the request will be processed and what data is expected.
     *
     * See reference values in the documentation for possible values.
     *
     * @param requestType The request type
     */
    public Request(String requestType) {
        this(requestType, new AdditionalData());
    }

    /**
     * Initialise this request with a request type and data.
     *
     * The request type indicates how the request will be processed and what data is expected.
     *
     * See reference values in the documentation for possible values.
     *
     * @param requestType The request type
     * @param requestData The data for the request
     */
    public Request(String requestType, AdditionalData requestData) {
        super(UUID.randomUUID().toString());
        this.requestType = requestType;
        this.requestData = requestData;
    }

    /**
     * Get the request type which indicates how it will be processed and what data to expect.
     *
     * @return The request type
     */
    @NonNull
    public String getRequestType() {
        return requestType;
    }

    /**
     * Set the application to target for this request.
     *
     * @param targetAppId The id of the application to target
     */
    public void setTargetAppId(String targetAppId) {
        this.targetAppId = targetAppId;
    }

    /**
     * Get the application target id.
     *
     * @return The application target id
     */
    @Nullable
    public String getTargetAppId() {
        return targetAppId;
    }

    /**
     * Convenience wrapper for adding additional data.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     */
    public <T> void addAdditionalData(String key, T... values) {
        requestData.addData(key, values);
    }

    /**
     * Get the request data.
     *
     * See reference values in the documentation for possible values.
     *
     * @return The request data
     */
    @NonNull
    public AdditionalData getRequestData() {
        return requestData;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Request fromJson(String json) {
        return JsonConverter.deserialize(json, Request.class);
    }
}
