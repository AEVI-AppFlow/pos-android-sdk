package com.aevi.sdk.pos.flow.paymentservicesample.util;

public final class IdProvider {

    private IdProvider() {

    }

    public static String getTerminalId() {
        return "12345678";
    }

    public static String getMerchantId() {
        return "1234abcd";
    }

    public static String getMerchantName() {
        return "Sample Merchant";
    }
}
