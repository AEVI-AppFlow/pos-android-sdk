package com.aevi.sdk.flow.model;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdditionalDataTest {

    private AdditionalData additionalData;

    @Before
    public void setUp() throws Exception {
        additionalData = new AdditionalData();
    }

    @Test
    public void canSetStringOption() {
        additionalData.addData("myExtra", "ext");

        assertThat(additionalData.getValue("myExtra")).isEqualTo("ext");
    }

    @Test
    public void canSetBooleanOption() {
        additionalData.addData("myBoolean", true);

        assertThat(additionalData.getValue("myBoolean")).isEqualTo(true);
    }

    @Test
    public void canSetIntOption() {
        additionalData.addData("myInt", 42322);

        assertThat(additionalData.getValue("myInt")).isEqualTo(42322);
    }

    @Test
    public void canSetLongOption() {
        additionalData.addData("myLong", 7736663L);

        assertThat(additionalData.getValue("myLong")).isEqualTo(7736663L);
    }

    @Test
    public void canRemoveOption() {
        additionalData.addData("Trump", "Donald");

        assertThat(additionalData.getValue("Trump")).isEqualTo("Donald");
        assertThat(additionalData.hasData("Trump")).isTrue();

        additionalData.removeData("Trump");

        assertThat(additionalData.getValue("Trump")).isNull();
        assertThat(additionalData.hasData("Trump")).isFalse();
    }

    @Test
    public void canGetAllOptionsViaKeys() throws IOException {
        additionalData.addData("Trump", "Donald");
        additionalData.addData("myInt", 42322);
        additionalData.addData("myBoolean", true);

        Set<String> keys = additionalData.getKeys();

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(3);
        assertThat(keys).contains("Trump", "myInt", "myBoolean");
    }

    @Test
    public void canGetOptionOfType() throws IOException {
        additionalData.addData("accessibleMode", true);

        assertThat(additionalData.getValue("accessibleMode", Boolean.class)).isTrue();
        assertThat(additionalData.getValue("accessibleMode", String.class)).isNull();
        assertThat(additionalData.getValue("accessibleMode", Integer.class)).isNull();
        assertThat(additionalData.getValue("accessibleMode", Object.class)).isNull();
    }

    @Test
    public void canGetDefaultValue() throws Exception {
        assertThat(additionalData.getValue("hello")).isNull();
        assertThat(additionalData.getValue("hello", "default")).isEqualTo("default");
        assertThat(additionalData.getValue("hello", String.class)).isNull();
        assertThat(additionalData.getValue("hello", String.class, "default")).isEqualTo("default");
    }

    @Test
    public void canGetOptionOfComplexType() throws IOException {
        Customer customer = new Customer("123");
        additionalData.addData("customer", customer);
        assertThat(additionalData.getValue("customer", Customer.class)).isEqualTo(customer);
    }

    @Test
    public void canClearOptions() throws IOException {
        additionalData.addData("myExtra", "ext");
        assertThat(additionalData.isEmpty()).isFalse();

        additionalData.clearData();

        assertThat(additionalData.isEmpty()).isTrue();
    }

    @Test
    public void canRetrieveSingleStringValueAsAnArray() throws Exception {
        additionalData.addData("test", "hello");

        String[] tests = additionalData.getValue("test", String[].class);
        assertThat(tests).hasSize(1).containsOnly("hello");
    }

    @Test
    public void canRetrieveSingleBoxedValueAsAnArray() throws Exception {
        additionalData.addData("test", 1);

        Integer[] tests = additionalData.getValue("test", Integer[].class);
        assertThat(tests).hasSize(1).containsOnly(1);
    }

    @Test
    public void retrievingBoxedPrimitiveAsPrimitiveArrayShouldReturnNull() throws Exception {
        additionalData.addData("test", 1);

        int[] tests = additionalData.getValue("test", int[].class);
        assertThat(tests).isNull();
    }

    @Test
    public void canRetrieveAnyValueAsStringArray() throws Exception {
        additionalData.addData("one", 12);
        additionalData.addData("two", new int[]{1, 2});
        additionalData.addData("three", "hello", "bye");

        assertThat(additionalData.getValue("one", String[].class)).isEqualTo(new String[]{"12"});
        assertThat(additionalData.getValue("two", String[].class)).isEqualTo(new String[]{"1", "2"});
        assertThat(additionalData.getValue("three", String[].class)).isEqualTo(new String[]{"hello", "bye"});
    }

    @Test
    public void canGetAllValuesOfType() throws Exception {
        additionalData.addData("one", 1);
        additionalData.addData("two", 2);
        additionalData.addData("three", "hello");

        Map<String, Integer> dataOfType = additionalData.getDataOfType(Integer.class);
        assertThat(dataOfType).hasSize(2).containsValues(1, 2);
    }

    @Test
    public void canGetValuesOfSuperType() throws Exception {
        additionalData.addData("int", 1);
        additionalData.addData("long", 2L);
        additionalData.addData("double", 2.01);
        additionalData.addData("float", 2.01f);
        additionalData.addData("three", "hello");
        Map<String, Number> dataOfType = additionalData.getDataOfType(Number.class);
        assertThat(dataOfType).hasSize(4).containsKeys("int", "long", "double", "float");
    }

    @Test
    public void canExtractAdditionalDataFromV3Json() {
        String v3Json = "{ \"bob\": \"alice\", \"number\": 46, \"boolean\": true, \"friends\" : { \"john\": \"julie\", \"dave\": \"bob\" }, \"the-list\": [ \"cheeese\", \"bacon\", \"banana\", \"apple\", { \"sneaky\" : \"object\" } ] }";
        AdditionalData data = AdditionalData.fromJson(v3Json);
        assertThat(data).isNotNull();
        assertThat(data.getValue("bob")).isEqualTo("alice");
        assertThat(data.getValue("bob", String.class)).isEqualTo("alice");
        assertThat(data.getValue("number")).isEqualTo(46L);
        assertThat(data.getValue("boolean")).isEqualTo(true);
        assertThat(data.hasData("friends")).isTrue();
        assertThat(data.getValue("friends")).hasSameClassAs(new HashMap<>());
        assertThat(data.getValue("friends", HashMap.class).get("dave")).isEqualTo("bob");
        assertThat(data.hasData("the-list")).isTrue();
        assertThat(data.getValue("the-list")).hasSameClassAs(new Object[0]);
        assertThat(data.getValue("the-list", Object[].class)).hasSize(5);
        assertThat(data.getValue("the-list", Object[].class)[0]).isEqualTo("cheeese");
        assertThat(data.getValue("the-list", Object[].class)[4]).hasSameClassAs(new HashMap<>());
    }

    @Test
    public void canSerialiseToV3Json() {
        additionalData.addData("one", 12);
        additionalData.addData("two", new int[]{1, 2});
        additionalData.addData("three", "hello", "bye");
        Map<String, String> mapy = new HashMap<>();
        mapy.put("burp", "boop");
        mapy.put("barp", "beep");
        additionalData.addData("four", mapy);
        additionalData.addData("five", true);
        Customer c = new Customer("Bert");
        c.setFullName("Bert Blahdeblah");
        c.addToken(new Token("tiktok", "wingwang"));
        additionalData.addData("six", c);

        String json = additionalData.toJson();
        System.out.println(json);
        assertThat(json).isNotNull();

        AdditionalData deserialised = AdditionalData.fromJson(json);

        assertThat(deserialised.hasData("six")).isTrue();
        assertThat(deserialised.getValue("six", Customer.class).getFullName()).isEqualTo("Bert Blahdeblah");
        assertThat(deserialised.getValue("six", Customer.class).getTokens().get(0).getSource()).isEqualTo("wingwang");
        assertThat(deserialised.getValue("six", Customer.class).getTokens().get(0).getValue()).isEqualTo("tiktok");
    }
}
