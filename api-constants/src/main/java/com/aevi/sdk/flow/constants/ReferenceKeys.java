package com.aevi.sdk.flow.constants;


public interface ReferenceKeys {

    // TODO - May not be what we want - temporary until we figure out how to define references and how to manage legacy mappings
    String MERCHANT_ID = "merchantId";
    String MERCHANT_NAME = "merchantName";
    String TERMINAL_ID = "terminalTd";
    String TRANSACTION_DATE_TIME = "transactionDateTime"; // ms since epoch
    String LEGACY_2X_ERROR_CODE = "legacyErrorCode";

    String PAYMENT_SERVICE = "paymentService";

}
