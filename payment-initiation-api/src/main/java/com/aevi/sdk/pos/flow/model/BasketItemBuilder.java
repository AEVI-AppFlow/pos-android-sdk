package com.aevi.sdk.pos.flow.model;

import java.util.UUID;

/**
 * Builder to create {@link BasketItem} instances.
 */
public class BasketItemBuilder {

    private String id;
    private int count = 1;
    private String label;
    private String category;
    private long amount;

    /**
     * Initialise the builder with a default random id.
     */
    public BasketItemBuilder() {
        generateRandomId();
    }

    /**
     * Initialise the builder from the provided basket item.
     *
     * @param copyFrom The item to copy from
     */
    public BasketItemBuilder(BasketItem copyFrom) {
        this.id = copyFrom.getId();
        this.count = copyFrom.getCount();
        this.label = copyFrom.getLabel();
        this.category = copyFrom.getCategory();
        this.amount = copyFrom.getIndividualAmount();
    }

    /**
     * Generate a new random id (UUID) for this item.
     *
     * Note that the builder is initialised with a random id, meaning this only has to be called to generate a new random id.
     *
     * @return This builder
     */
    public BasketItemBuilder generateRandomId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Set the id for this item, overriding the default generated random id.
     *
     * @param id The id
     * @return This builder
     */
    public BasketItemBuilder withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the initial count for this item.
     *
     * Defaults to 1 if not set.
     *
     * @param count The item count
     * @return This builder
     */
    public BasketItemBuilder withCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * Increment the count by one.
     *
     * @return This builder
     */
    public BasketItemBuilder incrementCount() {
        this.count++;
        return this;
    }

    /**
     * Decrements the item count by one as long as the current count is greater than zero.
     *
     * If the count is already zero, this method has no effect.
     *
     * @return This builder
     */
    public BasketItemBuilder decrementCount() {
        this.count--;
        return this;
    }

    /**
     * Modify the current count with the provided offset.
     *
     * This effectively does count += offset, and can be used to increase or decrease the count.
     *
     * @param offset The value to modify the current count with.
     * @return This builder
     */
    public BasketItemBuilder offsetCountBy(int offset) {
        this.count += offset;
        return this;
    }

    /**
     * Set the label for this item.
     *
     * @param label The label
     * @return This builder
     */
    public BasketItemBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Set the category for this item (such as "drinks" or "vegetables")
     *
     * @param category The category the item belongs to
     * @return This builder
     */
    public BasketItemBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Set the item amount value.
     *
     * @param amount The item amount value
     * @return This builder
     */
    public BasketItemBuilder withAmount(long amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Build the instance with a default count of 1 (if not set).
     *
     * @return A {@link BasketItem} instance
     */
    public BasketItem build() {
        if (count < 0) {
            throw new IllegalArgumentException("Basket item must have a count of zero or more");
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A basket item must have an id");
        }
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("A basket item must have a label");
        }
        return new BasketItem(id, label, category, amount, count);
    }
}
