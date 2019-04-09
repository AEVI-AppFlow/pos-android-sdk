package com.aevi.sdk.pos.flow.model;

import java.util.Objects;

import static com.aevi.sdk.flow.util.Preconditions.checkNotEmpty;

/**
 * Represents a measurement with a value and unit.
 *
 * Examples are "2.5 kilograms" or "13.45 feet".
 */
public class Measurement {

    private final float value;
    private final String unit;

    /**
     * Create a new measurement.
     *
     * @param value The value
     * @param unit  The unit
     */
    public Measurement(float value, String unit) {
        checkNotEmpty(unit, "Unit must be set for measurement");
        this.value = value;
        this.unit = unit;
    }

    /**
     * Get the value of this measurement.
     *
     * @return The value of this measurement
     */
    public float getValue() {
        return value;
    }

    /**
     * Get the unit of this measurement.
     *
     * @return The unit of this measurement
     */
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Measurement that = (Measurement) o;
        return Float.compare(that.value, value) == 0 &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
