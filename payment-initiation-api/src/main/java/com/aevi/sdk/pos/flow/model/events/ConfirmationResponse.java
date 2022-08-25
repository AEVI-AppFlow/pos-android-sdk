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

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a confirmation response event
 *
 * The response can contain one or more selectedValues. Always represented as an arrat of String(s)
 */
public class ConfirmationResponse {

    @NonNull
    private final String confirmationRequestId;

    @NonNull
    private final String[] selectedValues;

    /**
     * Creates a response to a previous {@link ConfirmationRequest}
     *
     * @param confirmationRequestId The id of the request that this is a response for
     * @param selectedValues        The value or values selected
     */
    public ConfirmationResponse(@NonNull String confirmationRequestId, @NonNull String[] selectedValues) {
        this.confirmationRequestId = confirmationRequestId;
        this.selectedValues = selectedValues;
    }

    /**
     * @return The id of the request that this is a response for
     */
    @NonNull
    public String getConfirmationRequestId() {
        return confirmationRequestId;
    }

    /**
     * @return The selected value or values of the response
     */
    @NonNull
    public String[] getSelectedValues() {
        return selectedValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationResponse that = (ConfirmationResponse) o;
        return confirmationRequestId.equals(that.confirmationRequestId) &&
                Arrays.equals(selectedValues, that.selectedValues);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(confirmationRequestId);
        result = 31 * result + Arrays.hashCode(selectedValues);
        return result;
    }

    @Override
    public String toString() {
        return "ConfirmationResponse{" +
                "confirmationRequestId='" + confirmationRequestId + '\'' +
                ", selectedValues=" + Arrays.toString(selectedValues) +
                '}';
    }
}
