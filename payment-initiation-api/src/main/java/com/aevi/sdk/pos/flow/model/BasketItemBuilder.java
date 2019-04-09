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
import com.aevi.sdk.flow.model.AdditionalData;

import java.util.*;

/**
 * Builder to create {@link BasketItem} instances.
 */
@SuppressWarnings("WeakerAccess")
public class BasketItemBuilder {

    private String id;
    private int quantity = 1;
    private String label;
    private String category;
    private long baseAmount = Integer.MIN_VALUE;
    private long amount;
    private Measurement measurement;
    private List<BasketItemModifier> modifiers;
    private Map<String, String> references;
    private AdditionalData itemData;

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
        this.quantity = copyFrom.getQuantity();
        this.label = copyFrom.getLabel();
        this.category = copyFrom.getCategory();
        this.amount = copyFrom.getIndividualAmount();
        this.baseAmount = copyFrom.getIndividualBaseAmount();
        this.measurement = copyFrom.getMeasurement();
        if (copyFrom.hasReferences()) {
            this.references = copyFrom.getReferences();
        }
        if (copyFrom.hasItemData()) {
            this.itemData = copyFrom.getItemData();
        }
        if (copyFrom.hasModifiers()) {
            this.modifiers = copyFrom.getModifiers();
        }
    }


    /**
     * Generate a new random id (UUID) for this item.
     *
     * Note that the builder is initialised with a random id, meaning this only has to be called to generate a new random id.
     *
     * @return This builder
     */
    @NonNull
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
    @NonNull
    public BasketItemBuilder withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the quantity for this item.
     *
     * Defaults to 1 if not set.
     *
     * See {@link #withMeasurement(float, String)} and {@link #withFractionalQuantity(float, String)} for scenarios where quantities have or may have a fractional part and a unit.
     *
     * @param quantity The item quantity
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Increment the quantity by one.
     *
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder incrementQuantity() {
        this.quantity++;
        return this;
    }

    /**
     * Decrements the item quantity by one as long as the current quantity is greater than zero.
     *
     * If the quantity is already zero, this method has no effect.
     *
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder decrementQuantity() {
        this.quantity--;
        return this;
    }

    /**
     * Modify the current quantity with the provided offset.
     *
     * This effectively does quantity += offset, and can be used to increase or decrease the quantity.
     *
     * @param offset The value to modify the current quantity with.
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder offsetQuantityBy(int offset) {
        this.quantity += offset;
        return this;
    }

    /**
     * Set the measurement of this item to support items measured in fractions with a unit, such as "1.25 kilograms".
     *
     * The quantity (via {@link #withQuantity(int)} defaults to 1, but can also be set to represent multiple items, such as "2 x 1.25 kilograms".
     *
     * See {@link #withFractionalQuantity(float, String)} for a convenience function that sets quantity and measurement as per provided parameters.
     *
     * @param value The measurement value
     * @param unit  The unit (such as "kilograms" or "feet")
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withMeasurement(float value, String unit) {
        this.measurement = new Measurement(value, unit);
        return this;
    }

    /**
     * Convenience method for scenarios where the quantity is represented as float/double internally in the client application.
     *
     * If a unit is set, the provided values will be set as per {@link #withMeasurement(float, String)} and the quantity defaults to 1.
     *
     * If no unit is set (null) and the provided quantity is a whole number, the value will be set as per {@link #withQuantity(int)}.
     *
     * If no unit is set and the provided quantity has a fractional part, an exception will be thrown as unit is mandatory for measurement.
     *
     * @param quantity The floating point quantity
     * @param unit     The unit (such as "kilograms" or "feet") - may be null
     * @return This builder
     * @throws IllegalArgumentException When the unit is not set for a quantity with a fractional part
     */
    @NonNull
    public BasketItemBuilder withFractionalQuantity(float quantity, String unit) {
        if (unit != null) {
            withMeasurement(quantity, unit);
        } else if (!hasFractionalPart(quantity)) {
            withQuantity((int) quantity);
        } else {
            throw new IllegalArgumentException("Unit must be set for quantities with fractional parts");
        }
        return this;
    }

    private boolean hasFractionalPart(float value) {
        return value % 1 != 0;
    }

    /**
     * Set the label for this item.
     *
     * @param label The label
     * @return This builder
     */
    @NonNull
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
    @NonNull
    public BasketItemBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Set the item amount value, inclusive of any modifiers.
     *
     * Note that the amount value can be negative to represent discounts, etc.
     *
     * See {@link #withBaseAmount(long)} to set the amount exclusive of modifiers.
     *
     * @param amount The item amount value, inclusive of any modifiers
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withAmount(long amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Set the item *base* amount value, exclusive of any modifiers.
     *
     * Note that the amount value can be negative to represent discounts, etc.
     *
     * This defaults to the {@link #withAmount(long)} value if not set.
     *
     * @param baseAmount The item base amount value, exclusive of any modifiers
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withBaseAmount(long baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    /**
     * Apply modifiers to this item.
     *
     * Note that modifiers are NOT validated by AppFlow - it is up to the client to ensure they are correct and that {@link #withAmount(long)}
     * and {@link #withBaseAmount(long)} have been set with correct values based on the modifiers.
     *
     * @param basketItemModifiers The var-args list of modifiers
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withModifiers(BasketItemModifier... basketItemModifiers) {
        if (this.modifiers == null) {
            this.modifiers = new ArrayList<>();
        }
        this.modifiers.addAll(Arrays.asList(basketItemModifiers));
        return this;
    }

    /**
     * Apply modifiers to this item.
     *
     * Note that modifiers are NOT validated by AppFlow - it is up to the client to ensure they are correct and that {@link #withAmount(long)}
     * and {@link #withBaseAmount(long)} have been set with correct values based on the modifiers.
     *
     * @param basketItemModifiers The list of modifiers
     * @return This builder
     */
    @NonNull
    public BasketItemBuilder withModifiers(List<BasketItemModifier> basketItemModifiers) {
        this.modifiers = basketItemModifiers;
        return this;
    }

    /**
     * Add additional item data entries.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     * @return This builder
     */
    @SafeVarargs
    public final <T> BasketItemBuilder withItemData(String key, T... values) {
        if (itemData == null) {
            itemData = new AdditionalData();
        }
        itemData.addData(key, values);
        return this;
    }

    /**
     * Add additional item data.
     *
     * @param additionalData The additional item data
     * @return This builder
     */
    public BasketItemBuilder withItemData(AdditionalData additionalData) {
        this.itemData = additionalData;
        return this;
    }

    /**
     * Add a custom / additional reference to this basket item.
     *
     * This can be used to add further information about the basket item that is not covered by the primary fields.
     *
     * Note that reference keys are case sensitive.
     *
     * @param key   The reference key
     * @param value The reference value
     * @return This builder
     * @deprecated Please use {@link #withItemData(AdditionalData)} instead
     */
    @Deprecated
    public BasketItemBuilder withReference(String key, String value) {
        if (references == null) {
            references = new HashMap<>();
        }
        references.put(key, value);
        return this;
    }

    /**
     * Build the instance with a default quantity of 1 (if not set).
     *
     * @return A {@link BasketItem} instance
     */
    @NonNull
    public BasketItem build() {
        if (quantity < 0) {
            throw new IllegalArgumentException("Basket item must have a quantity of zero or more");
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A basket item must have an id");
        }
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("A basket item must have a label");
        }
        // Default base amount to amount
        if (this.baseAmount == Integer.MIN_VALUE) {
            this.baseAmount = amount;
        }
        return new BasketItem(id, label, category, amount, baseAmount, quantity, measurement, modifiers, references, itemData);
    }
}
