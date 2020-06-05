package com.aevi.sdk.flow.model;

import android.support.annotation.Nullable;

import com.aevi.sdk.flow.constants.ErrorConstants;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Objects;

/**
 * An exception that will be returned to a client for fatal flow errors
 *
 * This exception will contain an errorCode that can be used to handle the error or display relevant information to the user.
 *
 * The errorMessage contained in the exception is a human readable message that should be used for debugging purposes only
 */
public class FlowException extends Exception implements Jsonable {

    private final String errorCode;
    private final String errorMessage;

    private String originatingRequestId;

    private String target;

    public FlowException(String errorCode, String message) {
        this.errorMessage = message;
        this.errorCode = errorCode;
    }

    /**
     * @return An error code indicting why the failure has occurred see {@link ErrorConstants} for details
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return A human readable message. This should be used for debugging only and NOT be displayed to the user.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowException fromJson(String json) {
        return JsonConverter.deserialize(json, FlowException.class);
    }

    /**
     * Get the id of the request that initiated this flow
     *
     * @return The id of the request
     */
    public String getOriginatingRequestId() {
        return originatingRequestId;
    }

    /**
     * Set the id originating request that initiated this flow
     *
     * @param originatingRequestId The id
     */
    public void setOriginatingRequestId(String originatingRequestId) {
        this.originatingRequestId = originatingRequestId;
    }

    /**
     * Get the target for this exception.
     *
     * @return The targett.
     */
    @Nullable
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target i.e. where this exception should be delivered to
     *
     * @param target The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowException that = (FlowException) o;
        return errorCode.equals(that.errorCode) &&
                errorMessage.equals(that.errorMessage) &&
                Objects.equals(originatingRequestId, that.originatingRequestId) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage, originatingRequestId, target);
    }

    @Override
    public String toString() {
        return "FlowException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", originatingRequestId='" + originatingRequestId + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
