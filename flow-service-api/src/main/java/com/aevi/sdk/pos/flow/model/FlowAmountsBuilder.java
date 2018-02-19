package com.aevi.sdk.pos.flow.model;


public class FlowAmountsBuilder {

    private String currency;
    private long baseAmount = FlowAmounts.NOT_SET;
    private long tipAmount = FlowAmounts.NOT_SET;
    private long otherAmount = FlowAmounts.NOT_SET;

    /**
     * Change the currency of the transaction.
     *
     * @param currency The new currency
     * @return This builder
     */
    public FlowAmountsBuilder changeCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Update the base amount for the transaction.
     *
     * Must be 0 or greater.
     *
     * @param amount The new base amount
     * @return This builder
     */
    public FlowAmountsBuilder updateBaseAmount(long amount) {
        if (amount >= 0) {
            baseAmount = amount;
        }
        return this;
    }

    /**
     * Set/update the tip amount for this transaction.
     *
     * Zero is not allowed. If tip should be removed from the transaction, please use {@link #removeTip()}
     *
     * @param amount The new tip amount (non-zero)
     * @return This builder
     */
    public FlowAmountsBuilder setTipAmount(long amount) {
        if (amount > 0) {
            tipAmount = amount;
        }
        return this;
    }

    /**
     * Remove any tip set from this transaction.
     *
     * @return This builder
     */
    public FlowAmountsBuilder removeTip() {
        tipAmount = 0;
        return this;
    }

    /**
     * Set/update the other amount for this transaction.
     *
     * Zero is not allowed. If other should be removed from the transaction, please use {@link #removeOther()}
     *
     * @param amount The new other amount (non-zero)
     * @return This builder
     */
    public FlowAmountsBuilder setOtherAmount(long amount) {
        if (amount > 0) {
            otherAmount = amount;
        }
        return this;
    }

    /**
     * Remove any other amount set from this transaction.
     *
     * @return This builder
     */
    public FlowAmountsBuilder removeOther() {
        otherAmount = 0;
        return this;
    }

    /**
     * Build the amounts.
     *
     * @return FlowAmounts
     */
    public FlowAmounts buildAmounts() {
        return new FlowAmounts(baseAmount, tipAmount, otherAmount, currency);
    }

}
