package com.aevi.sdk.pos.flow.model;


import android.content.Context;

import com.aevi.sdk.pos.flow.TestEnvironment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentFlowServicesTest {

    private PaymentFlowServiceInfo paymentFlowServiceInfoOne;
    private PaymentFlowServiceInfo paymentFlowServiceInfoTwo;

    private Context context;
    private PaymentFlowServices paymentFlowServices;

    @Before
    public void setUp() throws Exception {
        context = TestEnvironment.mockContext("com.test", "1.2.3");
        buildFlowServiceOne();
        buildFlowServiceTwo();
        List<PaymentFlowServiceInfo> paymentFlowServiceInfoList = new ArrayList<>();
        paymentFlowServiceInfoList.add(paymentFlowServiceInfoOne);
        paymentFlowServiceInfoList.add(paymentFlowServiceInfoTwo);
        paymentFlowServices = new PaymentFlowServices(paymentFlowServiceInfoList);
    }

    @Test
    public void shouldContainListOfTwoEntries() throws Exception {
        assertThat(paymentFlowServices.getNumberOfFlowServices()).isEqualTo(2);
    }

    @Test
    public void shouldCollatePaymentMethodsCorrectly() throws Exception {
        Set<String> allSupportedPaymentMethods = paymentFlowServices.getAllSupportedPaymentMethods();
        assertThat(allSupportedPaymentMethods).hasSize(3);
        assertThat(allSupportedPaymentMethods).containsOnly("pigeon", "yak", "horse");
    }

    @Test
    public void shouldCollateDataKeysCorrectly() throws Exception {
        Set<String> allSupportedDataKeys = paymentFlowServices.getAllSupportedDataKeys();
        assertThat(allSupportedDataKeys).hasSize(3);
        assertThat(allSupportedDataKeys).containsOnly("dataOne", "dataTwo", "dataThree");
    }

    @Test
    public void shouldCheckDataKeySupportedCorrectly() throws Exception {
        assertThat(paymentFlowServices.isDataKeySupported("dataOne")).isTrue();
        assertThat(paymentFlowServices.isDataKeySupported("dataTwo")).isTrue();
        assertThat(paymentFlowServices.isDataKeySupported("banana")).isFalse();
    }

    @Test
    public void shouldCollateRequestTypesCorrectly() throws Exception {
        Set<String> allSupportedRequestTypes = paymentFlowServices.getAllSupportedRequestTypes();
        assertThat(allSupportedRequestTypes).hasSize(3).containsOnly("reqOne", "reqTwo", "reqThree");
    }

    @Test
    public void isCurrencySupportedShouldMatchCorrectly() throws Exception {
        assertThat(paymentFlowServices.isCurrencySupported("AUD")).isTrue();
        assertThat(paymentFlowServices.isCurrencySupported("GBP")).isTrue();
        assertThat(paymentFlowServices.isCurrencySupported("USD")).isTrue();
        assertThat(paymentFlowServices.isCurrencySupported("SEK")).isFalse();
        assertThat(paymentFlowServices.isCurrencySupported("EUR")).isFalse();
    }

    @Test
    public void shouldCollateSupportedCurrenciesCorrectly() throws Exception {
        Set<String> allSupportedCurrencies = paymentFlowServices.getAllSupportedCurrencies();
        assertThat(allSupportedCurrencies).hasSize(3).containsOnly("GBP", "AUD", "USD");
    }


    private void buildFlowServiceOne() {
        paymentFlowServiceInfoOne = new PaymentFlowServiceInfoBuilder()
                .withVendor("Test One")
                .withDisplayName("Test One")
                .withSupportedRequestTypes("one", "two")
                .withCanPayAmounts(true, "pigeon", "horse")
                .withSupportedDataKeys("dataOne", "dataTwo")
                .withSupportedRequestTypes("reqOne", "reqTwo")
                .withSupportedCurrencies("GBP", "AUD")
                .withDefaultCurrency("GBP")
                .build(context);

        paymentFlowServiceInfoOne.setStages("stage1", "stage2");
    }

    private void buildFlowServiceTwo() {
        paymentFlowServiceInfoTwo = new PaymentFlowServiceInfoBuilder()
                .withVendor("Test Two")
                .withDisplayName("Test Two")
                .withSupportedRequestTypes("two", "three")
                .withCanPayAmounts(true, "pigeon", "yak")
                .withSupportedDataKeys("dataTwo", "dataThree")
                .withSupportsAccessibilityMode(true)
                .withSupportedRequestTypes("reqTwo", "reqThree")
                .withSupportedCurrencies("AUD", "USD")
                .withDefaultCurrency("AUD")
                .build(context);

        paymentFlowServiceInfoTwo.setStages("stage2", "stage3");
    }
}
