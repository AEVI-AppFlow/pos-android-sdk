package com.aevi.sdk.pos.flow.model;


import org.junit.Test;

public class PaymentServiceInfoBuilderTest {

    @Test
    public void shouldBuildWithMandatoryFieldsSet() throws Exception {
        getBuilderWithMandatoryFieldsSet().build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfVendorNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withVendor(null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfDisplayNameNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withDisplayName(null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfTerminalIdNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withTerminalId(null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfRequestTypesNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withSupportedRequestTypes((String[]) null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfTransactionTypesNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withSupportedTransactionTypes((String[]) null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfDefaultCurrencyNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withDefaultCurrency(null).build("com.test", "1.2.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfSupportedCurrenciesNull() throws Exception {
        getBuilderWithMandatoryFieldsSet().withSupportedCurrencies((String[]) null).build("com.test", "1.2.3");
    }

    private PaymentServiceInfoBuilder getBuilderWithMandatoryFieldsSet() {
        return new PaymentServiceInfoBuilder()
                .withVendor("Test")
                .withDisplayName("PA one")
                .withTerminalId("1234")
                .withMerchantIds("5678")
                .withSupportedRequestTypes("hawk", "snail")
                .withSupportedTransactionTypes("banana", "pear")
                .withSupportedCurrencies("GBP", "AUD")
                .withDefaultCurrency("GBP");
    }
}
