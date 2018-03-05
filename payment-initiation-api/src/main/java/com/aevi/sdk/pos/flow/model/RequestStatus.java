package com.aevi.sdk.pos.flow.model;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.SendableId;

import io.reactivex.annotations.NonNull;

/**
 * This object represents the current request status of a request object currently being processed by the flow processing service
 */
public class RequestStatus extends SendableId {

    private final String status;

    public RequestStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieve the current request status in raw string form.
     *
     * @return String representation of the current status of the request
     */
    @NonNull
    public String getStatus() {
        return status;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static RequestStatus fromJson(String json) {
        return JsonConverter.deserialize(json, RequestStatus.class);
    }
}
