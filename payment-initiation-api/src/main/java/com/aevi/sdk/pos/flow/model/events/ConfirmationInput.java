package com.aevi.sdk.pos.flow.model.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * Represents an input value that should be shown to the user as part of a {@link ConfirmationRequest}
 */
public class ConfirmationInput {

    @NonNull
    private final String valueType;

    private int maxValueLength = -1;

    private int minValueLength = -1;

    @Nullable
    private String valueRegEx;

    public ConfirmationInput(@NonNull String valueType) {
        this.valueType = valueType;
    }

    public ConfirmationInput(@NonNull String valueType, int minValueLength, int maxValueLength) {
        this(valueType);
        this.maxValueLength = maxValueLength;
        this.minValueLength = minValueLength;
    }

    public ConfirmationInput(@NonNull String valueType, int minValueLength, int maxValueLength, @Nullable String valueRegEx) {
        this(valueType, minValueLength, maxValueLength);
        this.valueRegEx = valueRegEx;
    }

    /**
     * @return The type of value this is e.g. text, numeric
     */
    @NonNull
    public String getValueType() {
        return valueType;
    }

    /**
     * @return The maximum number of characters for this input or -1 if not set
     */
    public int getMaxValueLength() {
        return maxValueLength;
    }

    /**
     * Sets the maximum length the input should be
     *
     * @param maxValueLength The max length
     */
    public void setMaxValueLength(int maxValueLength) {
        this.maxValueLength = maxValueLength;
    }

    /**
     * @return The minimum number of characters required for this input or -1 if not set
     */
    public int getMinValueLength() {
        return minValueLength;
    }

    /**
     * Sets the minimum length the input should be
     *
     * @param minValueLength The min length
     */
    public void setMinValueLength(int minValueLength) {
        this.minValueLength = minValueLength;
    }

    /**
     * @return A regular expression that should be used to validate the input or null if no validation required
     */
    @Nullable
    public String getValueRegEx() {
        return valueRegEx;
    }

    /**
     * Sets a regex that should be used to validate the input string
     *
     * @param valueRegEx A regular expression
     */
    public void setValueRegEx(@Nullable String valueRegEx) {
        this.valueRegEx = valueRegEx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationInput that = (ConfirmationInput) o;
        return maxValueLength == that.maxValueLength &&
                minValueLength == that.minValueLength &&
                valueType.equals(that.valueType) &&
                Objects.equals(valueRegEx, that.valueRegEx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueType, maxValueLength, minValueLength, valueRegEx);
    }

    @Override
    public String toString() {
        return "ConfirmationInput{" +
                "valueType='" + valueType + '\'' +
                ", maxValueLength=" + maxValueLength +
                ", minValueLength=" + minValueLength +
                ", valueRegEx='" + valueRegEx + '\'' +
                '}';
    }
}
