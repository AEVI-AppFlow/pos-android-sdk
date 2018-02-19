package com.aevi.payment.model;


import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.Transaction;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransactionTest {

    private Transaction transaction;

    @Test
    public void remainingAmountShouldEqualTotalForNoResponses() throws Exception {
        transaction = new com.aevi.sdk.pos.flow.model.Transaction(new com.aevi.sdk.pos.flow.model.Amounts(1000, "GBP"));

        assertThat(transaction.getRemainingAmounts()).isEqualTo(transaction.getRequestedAmounts());
    }

    @Test
    public void remainingAmountShouldTakeProcessedAmountsIntoAccount() throws Exception {
        transaction = new com.aevi.sdk.pos.flow.model.Transaction(new com.aevi.sdk.pos.flow.model.Amounts(1000, 200, 100, "GBP"));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(200, 50, 20, "GBP")));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(100, "GBP")));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(50, 10, 20, "GBP")));

        com.aevi.sdk.pos.flow.model.Amounts expectedRemaining = new com.aevi.sdk.pos.flow.model.Amounts(650, 140, 60, "GBP");
        assertThat(transaction.getRemainingAmounts()).isEqualTo(expectedRemaining);
    }

    @Test
    public void processedAmountsShouldReturnSumOfResponsesProcessed() throws Exception {
        transaction = new com.aevi.sdk.pos.flow.model.Transaction(new Amounts(1000, 200, 100, "GBP"));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(200, 50, 20, "GBP")));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(100, "GBP")));
        transaction.addTransactionResponse(getResponse(new com.aevi.sdk.pos.flow.model.Amounts(50, 10, 20, "GBP")));

        com.aevi.sdk.pos.flow.model.Amounts expectedProcessed = new com.aevi.sdk.pos.flow.model.Amounts(350, 60, 40, "GBP");
        assertThat(transaction.getProcessedAmounts()).isEqualTo(expectedProcessed);
    }

    private static com.aevi.sdk.pos.flow.model.TransactionResponse getResponse(com.aevi.sdk.pos.flow.model.Amounts amounts) {
        return new com.aevi.sdk.pos.flow.model.TransactionResponse("", null, null, null, amounts, null, null, null);
    }
}
