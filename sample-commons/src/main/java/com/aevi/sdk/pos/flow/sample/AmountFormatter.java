package com.aevi.sdk.pos.flow.sample;

import java.text.DecimalFormat;
import java.util.Currency;

public class AmountFormatter {

    private static final DecimalFormat TWO_PLACES = new DecimalFormat("0.00");

    public static String formatAmount(String currency, long amount) {
        return Currency.getInstance(currency).getSymbol() + TWO_PLACES.format(amount / (double) 100);
    }
}
