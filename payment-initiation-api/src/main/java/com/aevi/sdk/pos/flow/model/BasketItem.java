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
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * Represents an immutable basket item with associated id, label, category, quantity and amount.
 *
 * Please create via {@link BasketItemBuilder}.
 */
public class BasketItem {

    private final String id;
    private final String label;
    private final String category;
    private final long amount;
    private final int quantity;

    // Default constructor for deserialisation
    BasketItem() {
        this("", "", null, 0, 0);
    }

    /**
     * Create a new basket item with label, category, amount and quantity.
     *
     * @param id       The identifier (SKU or similar) for this item
     * @param label    The label of the item to show to merchants/customers, such as "Red onion"
     * @param category The category the item belongs to, such as "vegetables" or "dairy"
     * @param amount   The purchase amount for this (individual) item
     * @param quantity    The number of this type of basket item (default is 1, below 0 will produce an exception)
     */
    BasketItem(String id, String label, String category, long amount, int quantity) {
        this.id = id;
        this.label = label;
        this.category = category;
        this.amount = amount;
        this.quantity = quantity;
    }

    /**
     * Get the id of this item.
     *
     * @return The id of the item.
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Get the label for this item.
     *
     * @return The label
     */
    @NonNull
    public String getLabel() {
        return label;
    }

    /**
     * Get the category for this item, if any.
     *
     * @return The category
     */
    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * Get the cost (amount) for a single item of this type.
     *
     * @return The cost (amount) for a single item of this type
     */
    public long getIndividualAmount() {
        return amount;
    }

    /**
     * Get the total cost (amount) for the items of this type.
     *
     * @return The total cost (amount) for the items of this type.
     */
    public long getTotalAmount() {
        return amount * quantity;
    }

    /**
     * Get the item quantity.
     *
     * @return The item quantity
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketItem that = (BasketItem) o;
        return quantity == that.quantity &&
                amount == that.amount &&
                Objects.equals(id, that.id) &&
                Objects.equals(label, that.label) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, label, category, amount);
    }
}
