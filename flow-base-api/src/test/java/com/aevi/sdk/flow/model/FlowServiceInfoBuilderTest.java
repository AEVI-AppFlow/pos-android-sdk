package com.aevi.sdk.flow.model;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowServiceInfoBuilderTest {

    private FlowServiceInfoBuilder flowServiceInfoBuilder;

    @Mock
    private Context context;

    @Before
    public void setUp() throws Exception {
        when(context.getPackageName()).thenReturn("com.test");
        flowServiceInfoBuilder = new FlowServiceInfoBuilder();
        setAllMandatoryFields();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenVendorNotSet() throws Exception {
        flowServiceInfoBuilder.withVendor(null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenVersionNotSet() throws Exception {
        flowServiceInfoBuilder.withVersion(null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentDisplayNameWhenNotSet() throws Exception {
        flowServiceInfoBuilder.withDisplayName(null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenCapabilitiesNotSet() throws Exception {
        flowServiceInfoBuilder.withCapabilities((String[]) null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenStagesNotSet() throws Exception {
        flowServiceInfoBuilder.withStages((String[]) null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenSupportedRequestTypesNotSet() throws Exception {
        flowServiceInfoBuilder.withSupportedRequestTypes((String[]) null).build(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPaymentMethodsNotSet() throws Exception {
        flowServiceInfoBuilder.withCanPayAmounts(true, (String[]) null);
    }

    @Test
    public void shouldBuildCorrectFlowServiceInfo() throws Exception {
        FlowServiceInfo serviceInfo = flowServiceInfoBuilder
                .withSupportedDataKeys("key")
                .withSupportedTransactionTypes("banana")
                .withCanPayAmounts(true, new String[]{"pigeon"})
                .withCanAdjustAmounts(true)
                .withSupportedCurrencies("GBP", "AUD")
                .withBackgroundOnly(true)
                .withRequiresCardToken(false)
                .withSupportsAccessibilityMode(true)
                .build(context);

        assertThat(serviceInfo.getVendor()).isEqualTo("Test");
        assertThat(serviceInfo.getVersion()).isEqualTo("1.2.3");
        assertThat(serviceInfo.getDisplayName()).isEqualTo("Hello");
        assertThat(serviceInfo.getCapabilities()).isEqualTo(new String[]{"stuff"});
        assertThat(serviceInfo.getStages()).isEqualTo(new String[]{"stage 1"});
        assertThat(serviceInfo.getSupportedRequestTypes()).isEqualTo(new String[]{"tea making"});
        assertThat(serviceInfo.getSupportedTransactionTypes()).isEqualTo(new String[]{"banana"});
        assertThat(serviceInfo.canAdjustAmounts()).isEqualTo(true);
        assertThat(serviceInfo.canPayAmounts()).isEqualTo(true);
        assertThat(serviceInfo.getPaymentMethods()).isEqualTo(new String[]{"pigeon"});
        assertThat(serviceInfo.getSupportedCurrencies()).isEqualTo(new String[]{"GBP", "AUD"});
        assertThat(serviceInfo.supportsAccessibilityMode()).isEqualTo(true);
        assertThat(serviceInfo.isBackgroundOnly()).isEqualTo(true);
        assertThat(serviceInfo.requiresCardToken()).isEqualTo(false);
    }

    private void setAllMandatoryFields() {
        flowServiceInfoBuilder
                .withVendor("Test")
                .withVersion("1.2.3")
                .withDisplayName("Hello")
                .withCapabilities("stuff")
                .withStages("stage 1")
                .withSupportedRequestTypes("tea making");
    }
}
