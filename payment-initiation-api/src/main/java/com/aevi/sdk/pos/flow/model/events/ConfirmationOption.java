/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aevi.sdk.pos.flow.model.events;

import androidx.annotation.NonNull;

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
