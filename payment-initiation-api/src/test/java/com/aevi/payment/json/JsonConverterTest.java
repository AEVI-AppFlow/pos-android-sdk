package com.aevi.util.json;

import com.aevi.util.json.testmodels.JsonOptionsHolderTest;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonConverterTest {

    @Test
    public void checkCanConvertToJson() {
        JsonOptionsHolderTest r = new JsonOptionsHolderTest();
        setupArrayOfOptions(r, "keeeey", "type1", "type2");

        String json = r.toJson();

        JsonOptionsHolderTest result = JsonConverter.deserialize(json, JsonOptionsHolderTest.class);
        assertThat(result).isNotNull();
        assertThat(result.getOptions().getOption("keeeey")).isNotNull();
    }

    private void setupArrayOfOptions(JsonOptionsHolderTest request, String key, String... types) {
        request.getOptions().addOption(key, types);
    }
}
