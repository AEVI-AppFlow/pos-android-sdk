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

/**
 * Amount represented by a value in its subunit form (such as cents or pence) and currency (ISO 4217).
 */
public class Amount {

    private final long value;
    private final String currency;

    // Default constructor for deserialisation
    Amount() {
        value = 0;
        currency = "XXX";
    }

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
