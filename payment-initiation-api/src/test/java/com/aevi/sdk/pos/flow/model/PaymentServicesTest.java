package com.aevi.sdk.pos.flow.model;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PaymentServicesTest {

    private PaymentServiceInfo paymentServiceInfoOne;
    private PaymentServiceInfo paymentServiceInfoTwo;

    private PaymentServices paymentServices;

    @Before
    public void setUp() throws Exception {
        buildPaymentServiceInfoOne();
        buildPaymentServiceInfoTwo();
        List<PaymentServiceInfo> paymentServiceInfoList = new ArrayList<>();
        paymentServiceInfoList.add(paymentServiceInfoOne);
        paymentServiceInfoList.add(paymentServiceInfoTwo);

        paymentServices = new PaymentServices(paymentServiceInfoList);
    }

    @Test
    public void shouldContainTwoEntries() throws Exception {
        assertThat(paymentServices.getAllPaymentServices()).hasSize(2);
    }

    @Test
    public void isCurrencySupportedShouldMatchCorrectly() throws Exception {
        assertThat(paymentServices.isCurrencySupported("AUD")).isTrue();
        assertThat(paymentServices.isCurrencySupported("GBP")).isTrue();
        assertThat(paymentServices.isCurrencySupported("USD")).isTrue();
        assertThat(paymentServices.isCurrencySupported("SEK")).isFalse();
        assertThat(paymentServices.isCurrencySupported("EUR")).isFalse();
    }

    @Test
    public void shouldCollateSupportedCurrenciesCorrectly() throws Exception {
        Set<String> allSupportedCurrencies = paymentServices.getAllSupportedCurrencies();
        assertThat(allSupportedCurrencies).hasSize(3).containsOnly("GBP", "AUD", "USD");
    }

    @Test
    public void isTransactionTypeSupportedShouldMatchCorrectly() throws Exception {
        assertThat(paymentServices.isTransactionTypeSupported("banana")).isTrue();
        assertThat(paymentServices.isTransactionTypeSupported("pear")).isTrue();
        assertThat(paymentServices.isTransactionTypeSupported("apple")).isTrue();
        assertThat(paymentServices.isTransactionTypeSupported("melon")).isFalse();
        assertThat(paymentServices.isTransactionTypeSupported("plum")).isFalse();
    }

    @Test
    public void shouldCollateSupportedTransactionTypesCorrectly() throws Exception {
        Set<String> allSupportedTransactionTypes = paymentServices.getAllSupportedTransactionTypes();
        assertThat(allSupportedTransactionTypes).hasSize(3).containsOnly("banana", "pear", "apple");
    }

    @Test
    public void isRequestTypeSupportedShouldMatchCorrectly() throws Exception {
        assertThat(paymentServices.isRequestTypeSupported("hawk")).isTrue();
        assertThat(paymentServices.isRequestTypeSupported("snail")).isTrue();
        assertThat(paymentServices.isRequestTypeSupported("elephant")).isTrue();
        assertThat(paymentServices.isRequestTypeSupported("ant")).isFalse();
        assertThat(paymentServices.isRequestTypeSupported("dog")).isFalse();
    }

    @Test
    public void shouldCollateSupportedRequestTypesCorrectly() throws Exception {
        Set<String> allSupportedRequestTypes = paymentServices.getAllSupportedRequestTypes();
        assertThat(allSupportedRequestTypes).hasSize(3).containsOnly("hawk", "snail", "elephant");
    }

    @Test
    public void shouldCollateSupportedPaymentMethods() throws Exception {
        Set<String> allSupportedPaymentMethods = paymentServices.getAllSupportedPaymentMethods();
        assertThat(allSupportedPaymentMethods).hasSize(3).containsOnly("pushups", "pullups", "situps");
    }

    @Test
    public void shouldMatchDataKeyCorrectly() throws Exception {
        assertThat(paymentServices.isDataKeySupported("dataOne")).isTrue();
        assertThat(paymentServices.isDataKeySupported("dataTwo")).isTrue();
        assertThat(paymentServices.isDataKeySupported("dataThree")).isTrue();
        assertThat(paymentServices.isDataKeySupported("r2-d2")).isFalse();
        assertThat(paymentServices.isDataKeySupported("hakuna")).isFalse();
    }

    @Test
    public void shouldCollateSupportedDataKeysCorrectly() throws Exception {
        Set<String> allSupportedDataKeys = paymentServices.getAllSupportedDataKeys();
        assertThat(allSupportedDataKeys).hasSize(3).containsOnly("dataOne", "dataTwo", "dataThree");
    }

    @Test
    public void shouldCollateServicesSupportingFlowCardReadingCorrectly() throws Exception {
        assertThat(paymentServices.getPaymentServicesSupportingCardReadingStep()).hasSize(1);
    }

    @Test
    public void shouldCollateServicesSupportingAccessibilityCorrectly() throws Exception {
        assertThat(paymentServices.getPaymentServicesSupportingAccessibilityMode()).hasSize(1);
    }

    private void buildPaymentServiceInfoOne() {
        paymentServiceInfoOne = new PaymentServiceInfoBuilder()
                .withVendor("Test")
                .withVersion("1.0.0")
                .withDisplayName("PA one")
                .withTerminalId("1234")
                .withMerchantIds("5678")
                .withSupportedRequestTypes("hawk", "snail")
                .withSupportedTransactionTypes("banana", "pear")
                .withSupportedCurrencies("GBP", "AUD")
                .withDefaultCurrency("GBP")
                .withSupportedDataKeys("dataOne", "dataTwo")
                .withPaymentMethods("pushups", "pullups")
                .withSupportsFlowCardReading(true)
                .build("com.test.one");
    }

    private void buildPaymentServiceInfoTwo() {
        paymentServiceInfoTwo = new PaymentServiceInfoBuilder()
                .withVendor("Test")
                .withVersion("1.0.0")
                .withDisplayName("PA two")
                .withTerminalId("4321")
                .withMerchantIds("8765")
                .withSupportedRequestTypes("hawk", "elephant")
                .withSupportedTransactionTypes("banana", "apple")
                .withSupportedCurrencies("USD", "AUD")
                .withDefaultCurrency("USD")
                .withSupportedDataKeys("dataTwo", "dataThree")
                .withPaymentMethods("pushups", "situps")
                .withSupportsFlowCardReading(false)
                .withSupportsAccessibilityMode(true)
                .build("com.test.two");
    }

}
