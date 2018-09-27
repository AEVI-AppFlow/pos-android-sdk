package com.aevi.sdk.pos.flow.model;


import android.content.Context;

import com.aevi.sdk.flow.constants.ServiceInfoDataKeys;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentFlowServiceInfoBuilderTest {

    private PaymentFlowServiceInfoBuilder paymentFlowServiceInfoBuilder;

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = ContextHelper.mockContext("com.test", "1.2.3");
        paymentFlowServiceInfoBuilder = new PaymentFlowServiceInfoBuilder();
        setAllMandatoryFields();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenVendorNotSet() throws Exception {
        paymentFlowServiceInfoBuilder.withVendor(null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentDisplayNameWhenNotSet() throws Exception {
        paymentFlowServiceInfoBuilder.withDisplayName(null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPaymentMethodsNotSet() throws Exception {
        paymentFlowServiceInfoBuilder.withCanPayAmounts(true, (String[]) null);
    }

    @Test
    public void shouldBuildCorrectFlowServiceInfo() throws Exception {
        PaymentFlowServiceInfo serviceInfo = paymentFlowServiceInfoBuilder
                .withSupportedDataKeys("key")
                .withCustomRequestTypes("tea making", "knitting")
                .withCanPayAmounts(true, "pigeon", "rock")
                .withCanAdjustAmounts(true)
                .withSupportedCurrencies("GBP", "AUD")
                .withDefaultCurrency("GBP")
                .withLogicalDeviceId("12345")
                .withSupportsAccessibilityMode(true)
                .withManualEntrySupport(true)
                .build(context);

        assertThat(serviceInfo.getVendor()).isEqualTo("Test");
        assertThat(serviceInfo.getServiceVersion()).isEqualTo("1.2.3");
        assertThat(serviceInfo.getDisplayName()).isEqualTo("Hello");
        assertThat(serviceInfo.getCustomRequestTypes()).containsExactlyInAnyOrder("tea making", "knitting");
        assertThat(serviceInfo.canAdjustAmounts()).isEqualTo(true);
        assertThat(serviceInfo.canPayAmounts()).isEqualTo(true);
        assertThat(serviceInfo.getPaymentMethods()).containsExactlyInAnyOrder("pigeon", "rock");
        assertThat(serviceInfo.getSupportedCurrencies()).containsExactlyInAnyOrder("GBP", "AUD");
        assertThat(serviceInfo.getDefaultCurrency()).isEqualTo("GBP");
        assertThat(serviceInfo.getLogicalDeviceId()).isEqualTo("12345");
        assertThat(serviceInfo.supportsAccessibilityMode()).isEqualTo(true);
        assertThat(serviceInfo.getAdditionalInfo().getValue(ServiceInfoDataKeys.SUPPORTS_MANUAL_ENTRY, Boolean.class)).isTrue();
    }

    private void setAllMandatoryFields() {
        paymentFlowServiceInfoBuilder
                .withVendor("Test")
                .withDisplayName("Hello");
    }
}
