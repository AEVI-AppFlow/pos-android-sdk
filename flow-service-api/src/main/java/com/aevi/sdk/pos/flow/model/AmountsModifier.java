package com.aevi.sdk.pos.flow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper to augment {@link Amounts} in a correct way.
 */
public class AmountsModifier {

    private String originalCurrency;
    private String currency;
    private double exchangeRate;
    private long baseAmount;
    private Map<String, Long> additionalAmounts;
    private boolean hasModifications;

    public AmountsModifier(Amounts originalAmounts) {
        this.currency = this.originalCurrency = originalAmounts.getCurrency();
        this.baseAmount = originalAmounts.getBaseAmountValue();
        this.additionalAmounts = new HashMap<>(originalAmounts.getAdditionalAmounts());
    }

    /**
     * Change the currency associated with the amounts.
     *
     * Note that this will update all the amount values based on the provided exchange rate.
     *
     * The provided exchange rate must be the rate for converting the existing currency into the updated currency.
     *
     * As an example - if the current currency is USD and the updated currency is GBP, the exchange rate passed in must
     * be the USD -> GBP rate.
     *
     * @param currency     The new ISO-4217 currency code
     * @param exchangeRate The exchange rate (from original currency to updated currency)
     * @return This builder
     */
    public AmountsModifier changeCurrency(String currency, double exchangeRate) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        convertAmounts();
        hasModifications = true;
        return this;
    }

    private void convertAmounts() {
        baseAmount *= exchangeRate;
        for (String key : additionalAmounts.keySet()) {
            additionalAmounts.put(key, (long) (additionalAmounts.get(key) * exchangeRate));
        }
    }

    /**
     * Update the base request amount.
     *
     * Note that this will be rejected unless it is done in the "SPLIT" flow stage.
     *
     * For the common case of adding a fee, charity contribution, etc, please use {@link #setAdditionalAmount(String, long)}.
     *
     * Must be 0 or greater.
     *
     * @param baseAmountValue The new base amount
     * @return This builder
     */
    public AmountsModifier updateBaseAmount(long baseAmountValue) {
        if (baseAmountValue >= 0) {
            baseAmount = baseAmountValue;
            hasModifications = true;
        }
        return this;
    }

    /**
     * Set or update an additional request amount with an amount value.
     *
     * Must be 0 or greater.
     *
     * @param identifier The amount identifier
     * @param amount     The amount value
     * @return This builder
     */
    public AmountsModifier setAdditionalAmount(String identifier, long amount) {
        if (amount >= 0) {
            additionalAmounts.put(identifier, amount);
            hasModifications = true;
        }
        return this;
    }

    /**
     * Set or update an additional amount as a fraction of the base amount.
     *
     * This is useful for cases where a fee, charity contribution, etc is calculated as a fraction or percentage of the base amount.
     *
     * @param identifier The string identifier for the amount
     * @param fraction   The fraction of the base amount, ranging from 0.0 to 1.0f (0% to 100%)
     * @return This builder
     */
    public AmountsModifier setAdditionalAmountAsBaseFraction(String identifier, float fraction) {
        if (fraction < 0.0f || fraction > 1.0f) {
            throw new IllegalArgumentException("Fraction must be between 0.0 and 1.0");
        }
        setAdditionalAmount(identifier, (long) (baseAmount * fraction));
        return this;
    }

    /**
     * Check whether any modifications have been made.
     *
     * @return True if modifications, false otherwise.
     */
    public boolean hasModifications() {
        return hasModifications;
    }

    /**
     * Build the new {@link Amounts} instance
     *
     * @return The new Amounts
     */
    public Amounts build() {
        Amounts amounts = new Amounts(baseAmount, currency, additionalAmounts);
        if (!currency.equals(originalCurrency)) {
            amounts.setOriginalCurrency(originalCurrency);
            amounts.setCurrencyExchangeRate(exchangeRate);
        }
        return amounts;
    }

    public static float percentageToFraction(int percentage) {
        return percentage / 100f;
    }
}
