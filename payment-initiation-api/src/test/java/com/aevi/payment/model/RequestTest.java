package com.aevi.payment.model;

import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.util.json.JsonConverter;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.junit.Before;
import org.junit.Test;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.*;
import static com.aevi.sdk.flow.constants.AdditionalDataValues.*;
import static com.aevi.sdk.flow.constants.TransactionTypes.TYPE_PAY;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestTest {

    private com.aevi.sdk.pos.flow.model.Request defaultRequest;

    @Before
    public void setup() {
        defaultRequest = getValidRequest(1000, "GBP", TYPE_PAY);
    }

    @Test
    public void canBuildValidRequest() {
        com.aevi.sdk.pos.flow.model.Request request = getValidRequest(1000, "GBP", TYPE_PAY);

        assertValues(request, 1000, "GBP", TYPE_PAY);
    }

    @Test
    public void canSerialise() {
        com.aevi.sdk.pos.flow.model.Request request = getValidRequest(1000, "GBP", TYPE_PAY);

        String json = request.toJson();

        assertThat(json).isNotNull();
    }

    @Test
    public void canDeserialize() {
        com.aevi.sdk.pos.flow.model.Request request = getValidRequest(1000, "GBP", TYPE_PAY);

        com.aevi.sdk.pos.flow.model.Request result = com.aevi.sdk.pos.flow.model.Request.fromJson(request.toJson());

        assertValues(result, 1000, "GBP", TYPE_PAY);
        assertThat(request).isEqualTo(result);
    }

    @Test
    public void canSetCardEntryMethods() throws MalformedJsonException {
        setupArrayOfOptions(defaultRequest, DATA_KEY_CARD_ENTRY_METHODS, CARD_ENTRY_METHOD_INSERT, CARD_ENTRY_METHOD_SWIPE);

        com.aevi.sdk.pos.flow.model.Request result = toFromJson(defaultRequest);

        assertArrayOfOptions(result, DATA_KEY_CARD_ENTRY_METHODS, CARD_ENTRY_METHOD_INSERT, CARD_ENTRY_METHOD_SWIPE);
    }

    @Test
    public void canSetCardNetworks() throws MalformedJsonException {
        setupArrayOfOptions(defaultRequest, DATA_KEY_CARD_NETWORKS, CARD_NETWORK_AMEX, CARD_NETWORK_DINERS, CARD_NETWORK_GIFT);

        com.aevi.sdk.pos.flow.model.Request result = toFromJson(defaultRequest);

        assertArrayOfOptions(result, DATA_KEY_CARD_NETWORKS, CARD_NETWORK_AMEX, CARD_NETWORK_DINERS, CARD_NETWORK_GIFT);
    }

    @Test(expected = JsonParseException.class)
    public void invalidJsonWillThrow() {
        com.aevi.sdk.pos.flow.model.Request.fromJson("{}ahjj");
    }

    private com.aevi.sdk.pos.flow.model.Request toFromJson(com.aevi.sdk.pos.flow.model.Request request) {
        return JsonConverter.deserialize(request.toJson(), com.aevi.sdk.pos.flow.model.Request.class);
    }

    private void assertValues(com.aevi.sdk.pos.flow.model.Request request, long amount, String currency, String type) {
        assertThat(request).isNotNull();
        assertThat(request.getAmounts()).isEqualTo(new com.aevi.sdk.pos.flow.model.Amounts(amount, currency));
        assertThat(request.getAmounts().getCurrency()).isEqualTo(currency);
        assertThat(request.getTransactionType()).isEqualTo(type);
    }

    private void assertArrayOfOptions(com.aevi.sdk.pos.flow.model.Request result, String key, String... types) {
        String[] resultTypes = (String[]) result.getAdditionalData().getValue(key);
        assertThat(resultTypes).isNotEmpty();
        assertThat(resultTypes.length).isEqualTo(types.length);
        assertThat(resultTypes).containsExactly(types);
    }

    private void setupArrayOfOptions(com.aevi.sdk.pos.flow.model.Request request, String key, String... types) {
        request.getAdditionalData().addData(key, types);
    }

    private com.aevi.sdk.pos.flow.model.Request getValidRequest(long amount, String currency, String type) {
        return new com.aevi.sdk.pos.flow.model.Request.Builder().withTransactionType(type).withAmounts(new Amounts(amount, currency)).build();
    }
}
