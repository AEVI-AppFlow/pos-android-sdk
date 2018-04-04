package com.aevi.sdk.pos.flow.paymentservicesample.util;


import com.aevi.sdk.pos.flow.model.TransactionResponse;

public class InMemoryStore {

    private static final InMemoryStore INSTANCE = new InMemoryStore();

    public static InMemoryStore getInstance() {
        return INSTANCE;
    }

    private TransactionResponse lastTransactionResponseGenerated;

    public TransactionResponse getLastTransactionResponseGenerated() {
        return lastTransactionResponseGenerated;
    }

    public void setLastTransactionResponseGenerated(TransactionResponse lastTransactionResponseGenerated) {
        this.lastTransactionResponseGenerated = lastTransactionResponseGenerated;
    }
}
