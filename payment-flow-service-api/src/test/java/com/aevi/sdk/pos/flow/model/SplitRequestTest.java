package com.aevi.sdk.pos.flow.model;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SplitRequestTest {

    private static final long ORIGINAL_AMOUNT = 1000;
    private static final long TOTAL_AMOUNT = 2000;
    private static final String CURRENCY = "GBP";

    private Payment payment;
    private SplitRequest splitRequest;

    @Before
    public void setUp() throws Exception {
        payment = new PaymentBuilder().withAmounts(new Amounts(ORIGINAL_AMOUNT, CURRENCY)).withPaymentFlow("sale").build();
    }

    @Test
    public void shouldCalculateRemainingAmountsCorrectly() throws Exception {
        setupSplitRequestWith75PercentProcessed();

        Amounts remainingAmounts = splitRequest.getRemainingAmounts();

        assertThat(remainingAmounts).isEqualTo(new Amounts(TOTAL_AMOUNT / 4, CURRENCY));
    }

    @Test
    public void remainingShouldReturnRequestAmountsIfNothingProcessed() throws Exception {
        setupWithNothingProcessed();

        Amounts remainingAmounts = splitRequest.getRemainingAmounts();

        assertThat(remainingAmounts).isEqualTo(new Amounts(TOTAL_AMOUNT, CURRENCY));
    }

    @Test
    public void remainingShouldReturnZeroIfAllProcessed() throws Exception {
        setupWithAllProcessed();

        Amounts remainingAmounts = splitRequest.getRemainingAmounts();

        assertThat(remainingAmounts).isEqualTo(new Amounts(0, CURRENCY));
    }

    @Test
    public void shouldCalculateProcessedAmountsCorrectly() throws Exception {
        setupSplitRequestWith75PercentProcessed();

        Amounts processedAmounts = splitRequest.getProcessedAmounts();

        assertThat(processedAmounts).isEqualTo(new Amounts((TOTAL_AMOUNT / 4) * 3, CURRENCY));
    }

    @Test
    public void processedShouldReturnZeroIfNothingProcessed() throws Exception {
        setupWithNothingProcessed();

        Amounts processedAmounts = splitRequest.getProcessedAmounts();

        assertThat(processedAmounts).isEqualTo(new Amounts(0, CURRENCY));
    }

    @Test
    public void processedShouldReturnTotalIfAllProcessed() throws Exception {
        setupWithAllProcessed();

        Amounts processedAmounts = splitRequest.getProcessedAmounts();

        assertThat(processedAmounts).isEqualTo(new Amounts(TOTAL_AMOUNT, CURRENCY));
    }

    private void setupWithNothingProcessed() {
        List<Transaction> transactions = new ArrayList<>();
        splitRequest = new SplitRequest(payment, new Amounts(TOTAL_AMOUNT, CURRENCY), transactions);
    }

    private void setupWithAllProcessed() {
        Transaction transaction = new Transaction(new Amounts(TOTAL_AMOUNT, CURRENCY), null, null, null);
        transaction.addTransactionResponse(getResponse(new Amounts(TOTAL_AMOUNT, CURRENCY)));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        splitRequest = new SplitRequest(payment, new Amounts(TOTAL_AMOUNT, CURRENCY), transactions);
    }

    private void setupSplitRequestWith75PercentProcessed() {
        Transaction transaction = new Transaction(new Amounts(TOTAL_AMOUNT / 2, CURRENCY), null, null, null);
        transaction.addTransactionResponse(getResponse(new Amounts(TOTAL_AMOUNT / 4, CURRENCY)));

        Transaction transaction2 = new Transaction(new Amounts(TOTAL_AMOUNT / 2, CURRENCY), null, null, null);
        transaction2.addTransactionResponse(getResponse(new Amounts(TOTAL_AMOUNT / 2, CURRENCY)));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        splitRequest = new SplitRequest(payment, new Amounts(TOTAL_AMOUNT, CURRENCY), transactions);
    }

    private static TransactionResponse getResponse(Amounts amounts) {
        return new TransactionResponse("", null, TransactionResponse.Outcome.APPROVED, null, amounts, null, null, null);
    }
}
