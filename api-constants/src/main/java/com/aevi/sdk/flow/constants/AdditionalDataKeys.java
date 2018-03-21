package com.aevi.sdk.flow.constants;


public interface AdditionalDataKeys {

    String DATA_KEY_BASKET = "basket";
    String DATA_KEY_CUSTOMER = "customer";
    String DATA_KEY_TOKEN = "token";

    String DATA_KEY_TRANSACTION_ID = "transactionId";
    String DATA_KEY_TRANSACTION_RESPONSE = "transactionResponse";

    String DATA_KEY_AMOUNT = "amount";
    String DATA_KEY_MERCHANT_ID = "merchantId";
    String DATA_KEY_ACCESSIBLE_MODE = "accessibleMode";
    String DATA_KEY_CARD_ENTRY_METHODS = "cardEntryMethods"; // insert, tap, swipe, manual
    String DATA_KEY_CARD_NETWORKS = "cardNetworks"; // VISA, AMEX, MAESTRO, etc
    String DATA_KEY_ACCOUNT_TYPES = "accountTypes"; // credit, debit, etc

    String DATA_KEY_PREV_TXN_REFERENCES = "prevTxnReferences"; // Transaction references returned via the response of a previous txn
    String DATA_KEY_PREV_TXN_REQUEST_ID = "prevTxnRequestId"; // The Request id of a previous txn, useful for reversals/completions

    String DATA_KEY_SPLIT_TXN = "splitTxn"; // true if part of a split, false or not set otherwise
    String DATA_KEY_NUM_SPLITS = "numSplits"; // The number of splits in total

    String LEGACY_PRINT_DATA = "legacyPrintData";
}
