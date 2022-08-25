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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.util.json.JsonConverter;

import java.util.*;

/**
 * Represents an immutable basket item with associated id, label, category, quantity (and optionally measurement) and amount.
 *
 * A basket item may have a positive or negative amount, representing both purchases and applied discounts.
 *
 * Additional information such as tax, add-ons, etc can be added as modifiers and other arbitrary data as item data.
 *
 * Please create instances via {@link BasketItemBuilder}.
 */
public class BasketItem {

    private final String id;
    private final String label;
    private final String category;
    private final long amount;
    private final float baseAmount;
    private final int quantity;
    private final Measurement measurement;
    private final List<BasketItemModifier> modifiers;
    @Deprecated
    private final Map<String, String> references;
    private final AdditionalData itemData;

    // Default constructor for deserialisation
    BasketItem() {
        this("", "", null, 0, 0, 0, null, null, null, null);
    }

    /**
     * Create a new basket item with label, category, amount (inclusive of tax) and quantity.
     *
     * @param id          The identifier (SKU or similar) for this item
     * @param label       The label of the item to show to merchants/customers, such as "Red onion"
     * @param category    The category the item belongs to, such as "vegetables" or "dairy"
     * @param amount      The amount (cost, rounded) for this (individual) item, inclusive of modifiers and tax
     * @param baseAmount  The base amount (cost) for this (individual) item, exclusive of modifiers
     * @param quantity    The quantity (count) of this basket item (default is 1, below 0 will produce an exception)
     * @param measurement The measurement of this basket item (for items that are measured in fractions and require a unit)
     * @param modifiers   The modifiers for the basket item
     * @param references  Custom references for this basket item (deprecated - use itemData instead)
     * @param itemData    The item additional data
     */
    BasketItem(String id, String label, String category, long amount, float baseAmount, int quantity,
               Measurement measurement, List<BasketItemModifier> modifiers, @Deprecated Map<String, String> references,
               AdditionalData itemData) {
        this.id = id;
        this.label = label;
        this.category = category;
        this.amount = amount;
        this.baseAmount = baseAmount;
        this.quantity = quantity;
        this.measurement = measurement;
        this.modifiers = modifiers;
        this.references = references;
        this.itemData = itemData;
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
     * Get the cost (amount) for a single item of this type, inclusive of all modifiers and tax.
     *
     * This value has been rounded to closest whole sub-unit if the calculated amount has decimal points.
     *
     * Note that the amount may be negative in the case of discounts, etc.
     *
     * @return The cost (amount) for a single item of this type, including modifiers
     */
    public long getIndividualAmount() {
        return amount;
    }

    /**
     * Get the base cost (amount) for a single item of this type, excluding any modifiers that have been set.
     *
     * If no modifiers are set, this returns the same value as {@link #getIndividualAmount()}.
     *
     * Note that the amount may be negative in the case of discounts, etc.
     *
     * @return The base cost (amount) for a single item of this type, excluding modifiers
     */
    public float getIndividualBaseAmount() {
        return baseAmount;
    }

    /**
     * Get the total cost (amount) for the items of this type, inclusive of modifiers.
     *
     * This value has been rounded to closest whole sub-unit if the calculated amount has decimal points.
     *
     * Note that the amount may be negative in the case of discounts, etc.
     *
     * @return The total cost (amount) for the items of this type, including modifiers
     */
    @JsonConverter.ExposeMethod(value = "totalAmount")
    public long getTotalAmount() {
        return amount * quantity;
    }

    /**
     * Get the total cost (amount) for the items of this type, calculated from the base amount with modifiers applied.
     *
     * This can be used when rounding does not provide enough accuracy for calculating the total basket value.
     *
     * @return The total fractional amount, calculated from base amount with modifiers applied
     */
    public float getTotalFractionalAmount() {
        if (!hasModifiers()) {
            return getTotalAmount();
        }
        float amount = calculateFinalAmount(baseAmount, modifiers);
        return amount * quantity;
    }

    /**
     * Get the total base cost (amount) for the items of this type, exclusive of any modifiers.
     *
     * Note that the amount may be negative in the case of discounts, etc.
     *
     * @return The total cost (amount) for the items of this type, excluding modifiers
     */
    @JsonConverter.ExposeMethod(value = "totalBaseAmount")
    public float getTotalBaseAmount() {
        return baseAmount * quantity;
    }

    /**
     * Get the item quantity.
     *
     * Also see {@link #getMeasurement()}.
     *
     * @return The item quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Check if the item has any measurement set.
     *
     * @return True if measurement set, false otherwise
     */
    @JsonConverter.ExposeMethod(value = "hasMeasurement")
    public boolean hasMeasurement() {
        return measurement != null;
    }

    /**
     * Get the item measurement, if any.
     *
     * The measurement is relevant for items that are measured in fractions and require a unit, such as "1.25 kilograms".
     *
     * It is set separately to the quantity to support scenarios such as "2 x 1.25 kgs of sand".
     *
     * @return The measurement if set, or null
     */
    @Nullable
    public Measurement getMeasurement() {
        return measurement;
    }

    /**
     * Check whether this item has any modifiers.
     *
     * @return True if there are modifiers, false otherwise
     */
    @JsonConverter.ExposeMethod(value = "hasModifiers")
    public boolean hasModifiers() {
        return modifiers != null && !modifiers.isEmpty();
    }

    /**
     * Get the list of modifiers applied to this item, if any.
     *
     * A {@link BasketItemModifier} can be used to associate additional information that impacts the item cost/amounts, such as tax or add-ons.
     *
     * Note that these modifiers are not validated by AppFlow - it is the responsibility of the application that created the basket item to
     * ensure that the amount and base amount match up correctly with the modifiers.
     *
     * @return The list of modifiers
     */
    @NonNull
    public List<BasketItemModifier> getModifiers() {
        return modifiers != null ? modifiers : new ArrayList<>();
    }

    /**
     * Check whether this basket item has any associated item data.
     *
     * @return True if there is any item data, false otherwise
     */
    @JsonConverter.ExposeMethod(value = "hasItemData")
    public boolean hasItemData() {
        return itemData != null && !itemData.isEmpty();
    }

    /**
     * Get the additional item data for this basket item.
     *
     * Note that if there is no item data, this will return a new ad-hoc instance each time it is called.
     *
     * @return The additional item data
     */
    @NonNull
    public AdditionalData getItemData() {
        return itemData != null ? itemData : new AdditionalData();
    }

    /**
     * Check whether this basket item has any custom references.
     *
     * @return True if there are references, false otherwise
     * @deprecated Please use {@link #getItemData()} instead
     */
    @Deprecated
    @JsonConverter.ExposeMethod(value = "hasReferences")
    public boolean hasReferences() {
        return references != null && !references.isEmpty();
    }

    /**
     * Get the value for a reference by its key.
     *
     * Note that reference keys are case sensitive.
     *
     * @param referenceKey The key of the reference
     * @return The reference value, or null if no such reference exists
     * @deprecated Please use {@link #getItemData()} instead
     */
    @Nullable
    @Deprecated
    public String getReference(String referenceKey) {
        if (references != null) {
            return references.get(referenceKey);
        }
        return null;
    }

    /**
     * Get all item references.
     *
     * Note that reference keys are case sensitive.
     *
     * @return The item reference map
     * @deprecated Please use {@link #getItemData()} instead
     */
    @NonNull
    @Deprecated
    public Map<String, String> getReferences() {
        return references != null ? references : new HashMap<>();
    }

    private boolean referenceEquals(Map<String, String> refsOne, Map<String, String> refsTwo) {
        if ((refsOne == null || refsOne.isEmpty()) && (refsTwo == null || refsTwo.isEmpty())) {
            return true;
        }
        return Objects.equals(refsOne, refsTwo);
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", baseAmount=" + baseAmount +
                ", quantity=" + quantity +
                ", measurement=" + measurement +
                ", modifiers=" + modifiers +
                ", references=" + references +
                ", itemData=" + itemData +
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
        BasketItem that = (BasketItem) o;
        return amount == that.amount &&
                baseAmount == that.baseAmount &&
                quantity == that.quantity &&
                Objects.equals(id, that.id) &&
                Objects.equals(label, that.label) &&
                Objects.equals(category, that.category) &&
                Objects.equals(measurement, that.measurement) &&
                Objects.equals(modifiers, that.modifiers) &&
                referenceEquals(references, that.references) &&
                Objects.equals(itemData, that.itemData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, category, amount, baseAmount, quantity, measurement, modifiers, references, itemData);
    }

    /**
     * Calculate the final amount after applying modifiers to the base amount.
     *
     * @param baseAmount The base amount value
     * @param modifiers  The list of modifiers
     * @return The amount value calculated from base with modifiers applied
     */
    public static float calculateFinalAmount(float baseAmount, List<BasketItemModifier> modifiers) {
        float amount = baseAmount;
        for (BasketItemModifier modifier : modifiers) {
            if (modifier.getFractionalAmount() != null && modifier.getFractionalAmount() != 0.0f) {
                amount += modifier.getFractionalAmount();
            } else if (modifier.getPercentage() != null) {
                amount += baseAmount * (modifier.getPercentage() / 100.0f);
            }
        }
        return amount;
    }
}
