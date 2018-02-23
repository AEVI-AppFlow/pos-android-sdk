package com.aevi.sdk.pos.flow.model;

import com.aevi.util.json.JsonConverter;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.junit.Before;
import org.junit.Test;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.*;
import static com.aevi.sdk.flow.constants.AdditionalDataValues.*;
import static com.aevi.sdk.flow.constants.TransactionTypes.SALE;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {

    private Payment defaultPayment;

    @Before
    public void setup() {
        defaultPayment = getValidRequest(1000, "GBP", SALE);
    }

    @Test
    public void canBuildValidRequest() {
        Payment payment = getValidRequest(1000, "GBP", SALE);

        assertValues(payment, 1000, "GBP", SALE);
    }

    @Test
    public void canSerialise() {
        Payment payment = getValidRequest(1000, "GBP", SALE);

        String json = payment.toJson();

        assertThat(json).isNotNull();
    }

    @Test
    public void canDeserialize() {
        Payment payment = getValidRequest(1000, "GBP", SALE);

        Payment result = Payment.fromJson(payment.toJson());

        assertValues(result, 1000, "GBP", SALE);
        assertThat(payment).isEqualTo(result);
    }

    @Test
    public void canSetCardEntryMethods() throws MalformedJsonException {
        setupArrayOfOptions(defaultPayment, DATA_KEY_CARD_ENTRY_METHODS, CARD_ENTRY_METHOD_INSERT, CARD_ENTRY_METHOD_SWIPE);

        Payment result = toFromJson(defaultPayment);

        assertArrayOfOptions(result, DATA_KEY_CARD_ENTRY_METHODS, CARD_ENTRY_METHOD_INSERT, CARD_ENTRY_METHOD_SWIPE);
    }

    @Test
    public void canSetCardNetworks() throws MalformedJsonException {
        setupArrayOfOptions(defaultPayment, DATA_KEY_CARD_NETWORKS, CARD_NETWORK_AMEX, CARD_NETWORK_DINERS, CARD_NETWORK_GIFT);

        Payment result = toFromJson(defaultPayment);

        assertArrayOfOptions(result, DATA_KEY_CARD_NETWORKS, CARD_NETWORK_AMEX, CARD_NETWORK_DINERS, CARD_NETWORK_GIFT);
    }

    @Test(expected = JsonParseException.class)
    public void invalidJsonWillThrow() {
        Payment.fromJson("{}ahjj");
    }

    private Payment toFromJson(Payment payment) {
        return JsonConverter.deserialize(payment.toJson(), Payment.class);
    }

    private void assertValues(Payment payment, long amount, String currency, String type) {
        assertThat(payment).isNotNull();
        assertThat(payment.getAmounts()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amounts(amount, currency));
        assertThat(payment.getAmounts().getCurrency()).isEqualTo(currency);
        assertThat(payment.getTransactionType()).isEqualTo(type);
    }

    private void assertArrayOfOptions(Payment result, String key, String... types) {
        String[] resultTypes = (String[]) result.getAdditionalData().getValue(key);
        assertThat(resultTypes).isNotEmpty();
        assertThat(resultTypes.length).isEqualTo(types.length);
        assertThat(resultTypes).containsExactly(types);
    }

    private void setupArrayOfOptions(Payment payment, String key, String... types) {
        payment.getAdditionalData().addData(key, types);
    }

    private Payment getValidRequest(long amount, String currency, String type) {
        return new PaymentBuilder().withTransactionType(type).withAmounts(new Amounts(amount, currency)).build();
    }
}
