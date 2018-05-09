package com.aevi.sdk.pos.flow.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Represents a purchasable item with associated label, category and amount.
 */
public class BasketItem {

    private int count = 1;
    private final String label;
    private final String category;
    private final long amount;

    /**
     * Create a new basket item with label and amount, but no category.
     *
     * @param label  The label of the item, such as "Red onion"
     * @param amount The purchase amount for this (individual) item
     */
    public BasketItem(String label, long amount) {
        this(1, label, null, amount);
    }

    /**
     * Create a new basket item with label and amount and category.
     *
     * @param label    The label of the item, such as "Red onion"
     * @param category The category the item belongs to, such as "vegetables" or "dairy"
     * @param amount   The purchase amount for this (individual) item
     */
    public BasketItem(String label, String category, long amount) {
        this(1, label, category, amount);
    }

    /**
     * Create a new basket item with label, category and amount.
     *
     * @param count    The number of this type of basket item (default is 1, below 0 will produce an exception)
     * @param label    The label of the item, such as "Red onion"
     * @param category The category the item belongs to, such as "vegetables" or "dairy"
     * @param amount   The purchase amount for this (individual) item
     */
    public BasketItem(int count, String label, String category, long amount) {
        if (count < 0) {
            throw new IllegalArgumentException("Basket item must have a count of zero or more");
        }
        if (label == null) {
            throw new IllegalArgumentException("A basket item must have a label");
        }
        this.count = count;
        this.label = label;
        this.category = category;
        this.amount = amount;
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
    @NonNull
    public long getIndividualAmount() {
        return amount;
    }

    /**
     * Get the total cost (amount) for the items of this type.
     *
     * @return The total cost (amount) for the items of this type.
     */
    @NonNull
    public long getTotalAmount() {
        return amount * count;
    }

    /**
     * Get the item count.
     *
     * @return The item count
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the number of items in this basketItem entry by one.
     */
    public void addOne() {
        this.count++;
    }

    /**
     * Decrements the number of items in this basketItem entry by one as long as the count is greater than zero.
     *
     * Otherwise this method has no effect.
     */
    public void removeOne() {
        if (this.count > 0) {
            this.count--;
        }
    }

    /**
     * Sets the new count for this basket item
     *
     * @param count A positive integer greater than or equal to 0
     */
    public void setCount(int count) {
        if (count >= 0) {
            this.count = count;
        }
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "count=" + count +
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

        if (count != that.count) return false;
        if (amount != that.amount) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return category != null ? category.equals(that.category) : that.category == null;
    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }
}
