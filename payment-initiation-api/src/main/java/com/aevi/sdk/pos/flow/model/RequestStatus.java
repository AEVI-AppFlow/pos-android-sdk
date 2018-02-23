package com.aevi.sdk.pos.flow.model;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.SendableId;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * This object represents the current request status of a {@link Payment} object currently being processed by a payment service
 */
public class RequestStatus extends SendableId {

    private final String status;

    public RequestStatus(PaymentStage paymentStage) {
        this.status = paymentStage.name();
    }

    public RequestStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieve the current request status in raw string form.
     *
     * @return String representation of the current request status of the {@link Payment}
     */
    @NonNull
    public String getStatus() {
        return status;
    }

    /**
     * Retrieve the current request status as a {@link PaymentStage} object.
     *
     * Note that this may be null if the current request status set is not a PaymentStage value.
     *
     * @return The PaymentStage, or null
     */
    @Nullable
    public PaymentStage asPaymentStage() {
        if (status != null) {
            try {
                return PaymentStage.valueOf(status);
            } catch (IllegalArgumentException e) {
                // Fall-through
            }
        }
        return null;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static RequestStatus fromJson(String json) {
        return JsonConverter.deserialize(json, RequestStatus.class);
    }
}
