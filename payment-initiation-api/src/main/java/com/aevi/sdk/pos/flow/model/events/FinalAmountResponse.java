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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aevi.sdk.pos.flow.model.Amounts;

import java.util.Objects;

/**
 * Sent in response to a {@link FinalAmountRequest} when the final amount is known by the POS.
 */
public class FinalAmountResponse {

    @NonNull
    private final FinalAmountStatusType type;

    @Nullable
    private Amounts amount;

    /**
     * Create a final amount response using an {@link Amounts} object
     *
     * @param amount The final amount including any additional amounts
     */
    public FinalAmountResponse(@NonNull Amounts amount) {
        this.amount = amount;
        this.type = FinalAmountStatusType.KNOWN;
    }

    /**
     * Create a final amount using only a type. Usually indicates failure or unknown final amount.
     *
     * @param type The type of the final amount response
     */
    public FinalAmountResponse(@NonNull FinalAmountStatusType type) {
        this.type = type;
    }

    /**
     * @return The required final {@link Amounts}
     */
    @Nullable
    public Amounts getAmounts() {
        return amount;
    }

    /**
     * @return The type of final amount response this is
     */
    @NonNull
    public FinalAmountStatusType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalAmountResponse that = (FinalAmountResponse) o;
        return type.equals(that.type) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount);
    }

    @Override
    public String toString() {
        return "FinalAmountResponse{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
