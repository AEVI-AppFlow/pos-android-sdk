package com.aevi.sdk.pos.flow.model;

/**
 * Amount represented by a value in its subunit form (such as cents or pence) and currency (ISO 4217).
 */
public class Amount {

    private final long value;
    private final String currency;

    /**
     * Create a new Amount instance.
     *
     * @param value    The value in subunit form (cents, pence, etc)
     * @param currency The ISO-4217 currency code
     */
    public Amount(long value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    /**
     * @return The subunit representation of this amount
     */
    public long getValue() {
        return value;
    }

    /**
     * The ISO-4217 currency code for this amount.
     *
     * @return The The ISO-4217 currency code
     */
    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Amount amount = (Amount) o;

        if (value != amount.value) {
            return false;
        }
        return currency != null ? currency.equals(amount.currency) : amount.currency == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (value ^ (value >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }
}
