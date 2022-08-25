package com.aevi.sdk.pos.flow.model;

import java.util.Objects;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;
import static com.aevi.sdk.flow.util.Preconditions.checkNotEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A {@link BasketItemModifier} can be used to associate additional cost/amounts with a basket item, such as tax, discounts or add-ons.
 *
 * Either an absolute amount or a percentage must be set.
 */
public class BasketItemModifier {

    private final String id;
    private final String name;
    private final String type;
    private final Float amount;
    private final Float percentage;

    /**
     * Create an instance of a modifier.
     *
     * Either an absolute amount or a percentage must be set.
     *
     * @param id         The id, if one exists. May be null
     * @param name       The name of the modifier.
     * @param type       The type of the modifier.
     * @param amount     The absolute amount of the modifier.
     * @param percentage The percentage applied to the item amounts.
     */
    BasketItemModifier(String id, String name, String type, Float amount, Float percentage) {
        checkNotEmpty(name, "Name must be set");
        checkNotEmpty(type, "Type must be set");
        checkArgument(amount != null || percentage != null, "Either amount or percentage must be set");
        this.id = id;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.percentage = percentage;
    }

    /**
     * The id of this modifier, if any.
     *
     * @return The id of the modifier, or null if none is set
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * The name of this modifier.
     *
     * @return The name of this modifier.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * The type of this modifier.
     *
     * @return The type of this modifier
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * The absolute amount of this modifier in subunit form (e.g pence / cents). This amount may be fractional i.e. fractions of one cent/penny.
     * A fractional value may used in cases such as representing a tax amount that needs to be correct to several decimal places of accuracy when
     * these item modifiers are added together as a part of a whole transaction.
     *
     * This may be null if not set, or zero or negative if set.
     *
     * @return The absolute amount of this modifier, or null
     */
    @Nullable
    public Float getFractionalAmount() {
        return amount;
    }

    /**
     * The absolute amount of this modifier in subunit form (e.g pence / cents). Use this method if you do not care about fractional amounts
     * see {@link BasketItemModifier#getFractionalAmount()}.
     *
     * This may be null if not set, or zero or negative if set.
     *
     * @return The absolute amount of this modifier, or null
     */
    @Nullable
    public Long getAmount() {
        return amount != null ? amount.longValue() : null;
    }

    /**
     * The percentage this modifier applies to the basket item amounts.
     *
     * The percentage is represented as a float value, such as 25.75 for 25.75%.
     *
     * It may be null if not set, or be a negative value if set.
     *
     * @return The percentage, or null
     */
    @Nullable
    public Float getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "BasketItemModifier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", percentage=" + percentage +
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
        BasketItemModifier modifier = (BasketItemModifier) o;
        return Objects.equals(id, modifier.id) &&
                Objects.equals(name, modifier.name) &&
                Objects.equals(type, modifier.type) &&
                Objects.equals(amount, modifier.amount) &&
                Objects.equals(percentage, modifier.percentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, amount, percentage);
    }
}
