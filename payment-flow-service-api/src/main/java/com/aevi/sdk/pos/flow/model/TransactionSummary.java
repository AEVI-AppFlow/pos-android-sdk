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

package com.aevi.sdk.pos.flow.model;


import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.util.json.JsonConverter;

/**
 * Transaction summary model provided to post-payment applications.
 */
public class TransactionSummary extends Transaction {

    private final String transactionType;
    private final DeviceAudience deviceAudience;
    private final Card card;

    // Default constructor for deserialisation
    TransactionSummary() {
        transactionType = "";
        deviceAudience = DeviceAudience.MERCHANT;
        card = null;
    }

    public TransactionSummary(Transaction transaction, String transactionType, DeviceAudience deviceAudience, Card card) {
        super(transaction, transaction.getRequestedAmounts());
        this.transactionType = transactionType;
        this.deviceAudience = deviceAudience != null ? deviceAudience : DeviceAudience.MERCHANT;
        this.card = card != null ? card : Card.getEmptyCard();
    }

    /**
     * The transaction type.
     *
     * @return The transaction type.
     */
    @NonNull
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Get the type of audience (merchant or customer) that will be interacting with the device your app is running on.
     *
     * This information can be used to customise your application for the audience appropriately.
     *
     * @return The type of audience (merchant or customer) of the device this app is running on
     */
    @NonNull
    public DeviceAudience getDeviceAudience() {
        return deviceAudience;
    }

    /**
     * Get the card details provided from the VAA or from the payment card reading step.
     *
     * Note that all fields in this model are optional.
     *
     * @return The card details (may be empty)
     */
    @NonNull
    public Card getCard() {
        return card;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static TransactionSummary fromJson(String json) {
        return JsonConverter.deserialize(json, TransactionSummary.class);
    }

    @Override
    public String toString() {
        return "TransactionSummary{" +
                "transactionType='" + transactionType + '\'' +
                ", deviceAudience=" + deviceAudience +
                ", card=" + card +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TransactionSummary that = (TransactionSummary) o;

        if (transactionType != null ? !transactionType.equals(that.transactionType) : that.transactionType != null) return false;
        if (deviceAudience != that.deviceAudience) return false;
        return card != null ? card.equals(that.card) : that.card == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (deviceAudience != null ? deviceAudience.hashCode() : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }
}
