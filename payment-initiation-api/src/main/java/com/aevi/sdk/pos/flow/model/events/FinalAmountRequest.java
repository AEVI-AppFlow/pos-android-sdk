package com.aevi.sdk.pos.flow.model.events;

import java.util.Objects;

/**
 * Final amount event request
 *
 * Usually issued by a payment service when a final amount is required for a previously provisional one
 *
 * The request can optionally contain a timeout which indicates how long the payment service will
 * wait for a {@link FinalAmountResponse} to be returned.
 */
public class FinalAmountRequest {

    private int timeout = -1;

    public FinalAmountRequest() {

    }

    /**
     * Creates a final amount with a timeout in ms
     *
     * @param timeout The timeout in ms. Set to -1 if not required or 0 to indicate waiting forever.
     */
    public FinalAmountRequest(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return Number of milliseconds the payment service will wait for a response. Will be -1 if not set or 0 to indicate waiting forever.
     */
    public int getTimeout() {
        return timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalAmountRequest that = (FinalAmountRequest) o;
        return timeout == that.timeout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeout);
    }

    @Override
    public String toString() {
        return "FinalAmountRequest{" +
                "timeout=" + timeout +
                '}';
    }
}
