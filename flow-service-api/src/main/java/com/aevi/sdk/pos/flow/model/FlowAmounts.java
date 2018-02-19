package com.aevi.sdk.pos.flow.model;

/**
 * Model used to augment request amounts for the current transaction.
 *
 * This is created via {@link FlowAmountsBuilder} and used instead of {@link Amounts} to allow
 * flow apps to indicate with precision how the amounts and currencies need to be augmented.
 */
public final class FlowAmounts {

    public static final int NOT_SET = -1;

    private final long baseAmount;
    private final long tipAmount;
    private final long otherAmount;
    private final String currency;

    FlowAmounts(long baseAmount, long tipAmount, long otherAmount, String currency) {
        this.baseAmount = baseAmount;
        this.tipAmount = tipAmount;
        this.otherAmount = otherAmount;
        this.currency = currency;
    }

    public long getBaseAmount() {
        return baseAmount;
    }

    public long getTipAmount() {
        return tipAmount;
    }

    public long getOtherAmount() {
        return otherAmount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowAmounts that = (FlowAmounts) o;

        if (baseAmount != that.baseAmount) return false;
        if (tipAmount != that.tipAmount) return false;
        if (otherAmount != that.otherAmount) return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (baseAmount ^ (baseAmount >>> 32));
        result = 31 * result + (int) (tipAmount ^ (tipAmount >>> 32));
        result = 31 * result + (int) (otherAmount ^ (otherAmount >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FlowAmounts{" +
                "baseAmount=" + (baseAmount == NOT_SET ? "NOT SET" : baseAmount) +
                ", tipAmount=" + (tipAmount == NOT_SET ? "NOT SET" : tipAmount) +
                ", otherAmount=" + (otherAmount == NOT_SET ? "NOT SET" : otherAmount) +
                ", currency='" + (currency == null ? "NOT SET" : currency) + '\'' +
                '}';
    }
}
