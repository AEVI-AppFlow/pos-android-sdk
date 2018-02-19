package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.util.Preconditions;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * Representation of all the amounts relevant for a transaction.
 */
public class Amounts implements Jsonable {

    private final Amount base;
    private final Amount tip;
    private final Amount other;

    /**
     * Initialise with a base amount only.
     *
     * @param base     The base amount in subunit form (cents, pence, etc)
     * @param currency The ISO-4217 currency code
     */
    public Amounts(long base, String currency) {
        this(new Amount(base, currency), null, null);
    }

    /**
     * Initialise with a base and tip amount.
     *
     * @param base     The base amount in subunit form (cents, pence, etc)
     * @param tip      The tip amount in subunit form (cents, pence, etc)
     * @param currency The ISO-4217 currency code
     */
    public Amounts(long base, long tip, String currency) {
        this(new Amount(base, currency), new Amount(tip, currency), null);
    }

    /**
     * Initialise with a base, tip and other amount.
     *
     * @param base     The base amount in subunit form (cents, pence, etc)
     * @param tip      The tip amount in subunit form (cents, pence, etc)
     * @param other    The other amount in subunit form (cents, pence, etc)
     * @param currency The ISO-4217 currency code
     */
    public Amounts(long base, long tip, long other, String currency) {
        this(new Amount(base, currency), new Amount(tip, currency), new Amount(other, currency));
    }

    /**
     * Initialise with a base, tip and other amount.
     *
     * Note that an exception will be thrown if the currencies differ.
     *
     * @param base  The base amount in subunit form (cents, pence, etc) with currency
     * @param tip   The tip amount in subunit form (cents, pence, etc) with currency
     * @param other The other amount in subunit form (cents, pence, etc) with currency
     */
    public Amounts(Amount base, Amount tip, Amount other) {
        this.base = base;
        this.tip = tip != null && tip.getValue() > 0 ? tip : null;
        this.other = other != null && other.getValue() > 0 ? other : null;
        validateAmounts();
    }

    /**
     * Initialise with another Amounts object and copy over the values.
     *
     * @param from The amounts to copy from
     */
    public Amounts(Amounts from) {
        this(from.getBaseAmount(), from.getTipAmount(), from.getOtherAmount());
    }

    private void validateAmounts() {
        Preconditions.checkArgument(base != null && base.getValue() >= 0, "Base amount must be set and have a positive value");
        if (tip != null) {
            Preconditions.checkArgument(tip.getCurrency().equals(base.getCurrency()),
                    "Tip currency must match base currency and have a positive value");
        }
        if (other != null) {
            Preconditions.checkArgument(other.getCurrency().equals(base.getCurrency()),
                    "Other currency must match base currency and have a positive value");
        }
    }

    /**
     * Get the currency for these amounts.
     *
     * @return The ISO-4217 currency code
     */
    public String getCurrency() {
        return base.getCurrency();
    }

    /**
     * Get the base amount model.
     *
     * Note - base amount is mandatory and never null, but can have a zero value.
     *
     * @return The base amount
     */
    public Amount getBaseAmount() {
        return base;
    }

    /**
     * Get the base amount in subunit form (cents, pence, etc).
     *
     * Note that base amount can be 0.
     *
     * @return The base amount in subunit form
     */
    public long getBaseAmountValue() {
        return base.getValue();
    }

    /**
     * Get the tip amount model.
     *
     * Note - this can be null if tip has not been set.
     *
     * @return The tip amount
     */
    public Amount getTipAmount() {
        return tip;
    }

    /**
     * Get the tip amount in subunit form (cents, pence, etc).
     *
     * This will return zero if the tip amount has not been set.
     *
     * @return The tip amount in subunit form
     */
    public long getTipAmountValue() {
        return tip != null ? tip.getValue() : 0;
    }

    /**
     * Get the other amount model.
     *
     * Note - this can be null if other has not been set.
     *
     * @return The other amount
     */
    public Amount getOtherAmount() {
        return other;
    }

    /**
     * Get the other amount in subunit form (cents, pence, etc).
     *
     * This will return zero if other amount has not been set.
     *
     * @return The other amount in subunit form
     */
    public long getOtherAmountValue() {
        return other != null ? other.getValue() : 0;
    }

    /**
     * Get the total amount which includes base, tip and other.
     *
     * @return The total amount.
     */
    public Amount getTotalAmount() {
        return new Amount(getTotalAmountValue(), getCurrency());
    }

    /**
     * Get the total amount (base + tip + other) in subunit form.
     *
     * @return The total amount
     */
    public long getTotalAmountValue() {
        long total = base.getValue();
        if (other != null) {
            total += other.getValue();
        }
        if (tip != null) {
            total += tip.getValue();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Amounts that = (Amounts) o;

        if (base != null ? !base.equals(that.base) : that.base != null) {
            return false;
        }
        if (tip != null ? !tip.equals(that.tip) : that.tip != null) {
            return false;
        }
        return other != null ? other.equals(that.other) : that.other == null;

    }

    @Override
    public int hashCode() {
        int result = base != null ? base.hashCode() : 0;
        result = 31 * result + (tip != null ? tip.hashCode() : 0);
        result = 31 * result + (other != null ? other.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Amounts{" +
                "base=" + base +
                ", tip=" + tip +
                ", other=" + other +
                '}';
    }

    /**
     * Add together two amounts and get a new Amounts instance back with the result.
     *
     * @param a1 The first amount to add with
     * @param a2 The second amount to add with
     * @return The combined result
     */
    public static Amounts addAmounts(Amounts a1, Amounts a2) {
        if (a1 == null || a2 == null || !a1.getCurrency().equals(a2.getCurrency())) {
            throw new IllegalArgumentException("Invalid amounts or trying to combine different currencies");
        }
        return new Amounts(a1.getBaseAmountValue() + a2.getBaseAmountValue(), a1.getTipAmountValue() + a2.getTipAmountValue(),
                a1.getOtherAmountValue() + a2.getOtherAmountValue(),
                a1.getCurrency());
    }

    /**
     * Subtract one amounts from another.
     *
     * @param a1 The amounts to subtract a2 from
     * @param a2 The amount of which a1 will be reduced by
     * @return The reduced amounts
     */
    public static Amounts subtractAmounts(Amounts a1, Amounts a2) {
        long remainingBase = Math.max(a1.getBaseAmountValue() - a2.getBaseAmountValue(), 0);
        long remainingTip = Math.max(a1.getTipAmountValue() - a2.getTipAmountValue(), 0);
        long remainingOther = Math.max(a1.getOtherAmountValue() - a2.getOtherAmountValue(), 0);
        return new Amounts(remainingBase, remainingTip, remainingOther, a1.getCurrency());
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }
}
