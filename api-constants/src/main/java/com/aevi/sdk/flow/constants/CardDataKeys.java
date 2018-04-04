package com.aevi.sdk.flow.constants;


public interface CardDataKeys {

    String CARD_ENTRY_METHODS = "cardEntryMethods"; // insert, tap, swipe, manual
    String CARD_NETWORKS = "cardNetworks"; // VISA, AMEX, MAESTRO, etc
    String ACCOUNT_TYPES = "accountTypes"; // credit, debit, etc

    String NETWORK = "network";
    String ENTRY_METHOD = "entryMethod";
    String ACCOUNT_TYPE = "accountType";
    String AID = "aid";
    String SERVICE_CODE = "serviceCode";
    String CVV = "cvv";
    String LANGUAGES = "languages";

    String CURRENCY_CHANGE_ALLOWED = "currencyChangeAllowed";
    String AMOUNT_CHANGE_ALLOWED = "amountChangeAllowed";
}
