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
     * Must be 0 or greater.
     *
     * @param baseAmountValue The new base amount
     * @return This builder
     */
    public AmountsModifier updateBaseAmount(long baseAmountValue) {
        if (baseAmountValue >= 0) {
            baseAmount = baseAmountValue;
        }
        return this;
    }

    /**
     * Set or update an additional request amount.
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
        }
        return this;
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

}
