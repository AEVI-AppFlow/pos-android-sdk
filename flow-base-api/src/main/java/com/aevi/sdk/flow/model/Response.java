package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;

import io.reactivex.annotations.NonNull;

/**
 * Response to a generic {@link Request} that contains the outcome and bespoke response data for that request type.
 */
public class Response extends BaseModel {

    private final Request originatingRequest;
    private final boolean success;
    private final String outcomeMessage;
    private final AdditionalData responseData;

    /**
     * Initialise a response with the originating request and outcome details.
     *
     * @param request        The originating request
     * @param success        The success flag of the processing
     * @param outcomeMessage The outcome message
     */
    public Response(Request request, boolean success, String outcomeMessage) {
        this(request, success, outcomeMessage, new AdditionalData());
    }

    /**
     * Initialise a response with the originating request, outcome details and response data.
     *
     * See reference values in the documentation for possible values.
     *
     * @param request        The originating request
     * @param success        The success flag of the processing
     * @param outcomeMessage The outcome message
     * @param responseData   The bespoke response data
     */
    public Response(Request request, boolean success, String outcomeMessage, AdditionalData responseData) {
        super(request.getId());
        this.originatingRequest = request;
        this.success = success;
        this.outcomeMessage = outcomeMessage;
        this.responseData = responseData;
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
        responseData.addData(key, values);
    }

    /**
     * Get the request that triggered this response.
     *
     * @return The originating request
     */
    @NonNull
    public Request getOriginatingRequest() {
        return originatingRequest;
    }

    /**
     * Check whether the processing was successful or not
     *
     * @return True if the processing of the request succeeded, false otherwise
     */
    public boolean wasSuccessful() {
        return success;
    }

    /**
     * Get the outcome message that further describes the processing outcome.
     *
     * @return The outcome message
     */
    @NonNull
    public String getOutcomeMessage() {
        return outcomeMessage;
    }

    /**
     * Get the response data bespoke to this request type.
     *
     * See reference values in the documentation for possible values.
     *
     * @return The response data
     */
    @NonNull
    public AdditionalData getResponseData() {
        return responseData;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Response fromJson(String json) {
        return JsonConverter.deserialize(json, Response.class);
    }
}
