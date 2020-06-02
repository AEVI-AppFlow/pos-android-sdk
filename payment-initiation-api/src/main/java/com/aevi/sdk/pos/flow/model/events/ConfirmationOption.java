package com.aevi.sdk.pos.flow.model.events;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Represents a confirmation option consisting of some text value to display.
 *
 * To be included in a {@link ConfirmationRequest} when required
 */
public class ConfirmationOption {

    @NonNull
    private final String value;

    @NonNull
    private final String label;

    public ConfirmationOption(@NonNull String value, @NonNull String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * @return The value of the confirmation option
     */
    @NonNull
    public String getValue() {
        return value;
    }

    /**
     * @return A label that should be shown for the value to be selected
     */
    @NonNull
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationOption that = (ConfirmationOption) o;
        return value.equals(that.value) &&
                label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, label);
    }

    @Override
    public String toString() {
        return "ConfirmationOption{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
