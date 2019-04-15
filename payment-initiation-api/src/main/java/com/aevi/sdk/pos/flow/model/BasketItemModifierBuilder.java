package com.aevi.sdk.pos.flow.model;

import android.support.annotation.NonNull;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;
import static com.aevi.sdk.flow.util.Preconditions.checkNotEmpty;

/**
 * A builder to create {@link BasketItemModifier} instances
 */
public class BasketItemModifierBuilder {

    private final String name;
    private final String type;

    private String id;
    private Float amount;
    private Float percentage;

    /**
     * Create an instance of this builder
     *
     * @param name The name of the modifier you are building
     * @param type The type of modifier you are building
     */
    public BasketItemModifierBuilder(@NonNull String name, @NonNull String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Set the id for this item modifier (optional).
     *
     * @param id The id
     * @return This builder
     */
    @NonNull
    public BasketItemModifierBuilder withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the absolute amount of this modifier in subunit form (e.g pence / cents). This amount may be fractional i.e. fractions of one cent/penny.
     * A fractional value may used in cases such as representing a tax amount that needs to be correct to several decimal places of accuracy when
     * these item modifiers are added together as a part of a whole transaction. If a fractional value is not required then use
     * {@link BasketItemModifierBuilder#withAmount(long)} instead.
     *
     * This value can be negative to indicate a reduction/discount to the item amount.
     *
     * @param amount The amount
     * @return This builder
     */
    @NonNull
    public BasketItemModifierBuilder withFractionalAmount(float amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Set the absolute amount of this modifier in subunit form (e.g pence / cents). Use this method if you do not care about fractional amounts
     * see {@link BasketItemModifierBuilder#withFractionalAmount(float)}.
     *
     * This value can be negative to indicate a reduction/discount to the item amount.
     *
     * @param amount The amount
     * @return This builder
     */
    public BasketItemModifierBuilder withAmount(long amount) {
        this.amount = (float) amount;
        return this;
    }

    /**
     * Set the percentage rate for this modifier.
     *
     * The percentage is represented as a float value, such as 25.75 for 25.75%.
     *
     * This value can be negative to indicate a reduction/discount to the item amount.
     *
     * @param percentage The percentage
     * @return This builder
     */
    public BasketItemModifierBuilder withPercentage(float percentage) {
        this.percentage = percentage;
        return this;
    }

    /**
     * Build the modifier instance.
     *
     * As well as name and type either an absolute amount or a percentage must also be set otherwise this method will throw an {@link IllegalArgumentException}.
     *
     * @return A {@link BasketItemModifier} instance
     */
    @NonNull
    public BasketItemModifier build() {
        checkNotEmpty(name, "Name must be set");
        checkNotEmpty(type, "Type must be set");
        checkArgument(amount != null || percentage != null, "Either amount or percentage must be set");

        return new BasketItemModifier(id, name, type, amount, percentage);
    }
}
