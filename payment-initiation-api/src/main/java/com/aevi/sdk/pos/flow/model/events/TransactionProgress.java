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

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.Card;

import java.util.Objects;

/**
 * Represents information about transaction received during its processing, eg. with merchant receipt
 */
public class TransactionProgress {

    @NonNull
    private final Amounts amounts;

    @Nullable
    private Card card;

    @NonNull
    private AdditionalData references = new AdditionalData();

    /**
     * Creates a progress event with some {@link Amounts}
     *
     * @param amounts The current amounts
     */
    public TransactionProgress(@NonNull Amounts amounts) {
        this.amounts = amounts;
    }

    /**
     * Creates a progress event with some {@link Amounts} and a {@link Card}
     *
     * @param amounts The current amounts
     * @param card    THe current card details
     */
    public TransactionProgress(@NonNull Amounts amounts, @NonNull Card card) {
        this(amounts, card, new AdditionalData());
    }

    /**
     * Creates a progress event with some {@link Amounts} a {@link Card} and some reference data
     *
     * @param amounts    The current amounts
     * @param card       The current card details
     * @param references A set of references
     */
    public TransactionProgress(@NonNull Amounts amounts, @NonNull Card card, @NonNull AdditionalData references) {
        this.amounts = amounts;
        this.card = card;
        this.references = references;
    }

    /**
     * @return The current amounts being used for the transaction in progress
     */
    @NonNull
    public Amounts getAmounts() {
        return amounts;
    }

    /**
     * @return The current card details being used for the transaction (if known)
     */
    @Nullable
    public Card getCard() {
        return card;
    }

    /**
     * @return Any additional references of the transaction set by the payment service
     */
    @NonNull
    public AdditionalData getReferences() {
        return references;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionProgress that = (TransactionProgress) o;
        return amounts.equals(that.amounts) &&
                Objects.equals(card, that.card) &&
                references.equals(that.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amounts, card, references);
    }

    @Override
    public String toString() {
        return "TransactionProgress{" +
                "amounts=" + amounts +
                ", card=" + card +
                ", references=" + references +
                '}';
    }
}
