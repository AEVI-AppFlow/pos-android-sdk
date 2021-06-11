package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonOption;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AdditionalDataTest {

    private AdditionalData additionalData;

    class NonPrimitiveClass {
        public String name;

        public NonPrimitiveClass(String name) {
            this.name = name;
        }
    }

    @Before
    public void setUp() throws Exception {
        additionalData = new AdditionalData();
    }

    @Test
    public void canSetStringOption() {
        additionalData.addData("myExtra", "ext");
        assertThat(additionalData.getValue("myExtra")).isEqualTo("ext");

        setForced("myExtra", "string", "ext");
        assertThat(additionalData.getValue("myExtra")).isEqualTo("ext");
    }

    @Test
    public void canSetBooleanOption() {
        additionalData.addData("myBoolean", true);
        assertThat(additionalData.getValue("myBoolean")).isEqualTo(true);

        setForced("myBoolean", "boolean", true);
        assertThat(additionalData.getValue("myBoolean")).isEqualTo(true);
    }

    @Test
    public void canSetIntOption() {
        additionalData.addData("myInt", 42322);
        assertThat(additionalData.getValue("myInt")).isEqualTo(42322);

        setForced("myInt", "integer", 42322);
        assertThat(additionalData.getValue("myInt")).isEqualTo(42322);
    }

    @Test
    public void canSetLongOption() {
        additionalData.addData("myLong", 7736663L);
        assertThat(additionalData.getValue("myLong")).isEqualTo(7736663L);

        setForced("myLong", "long", 7736663L);
        assertThat(additionalData.getValue("myLong")).isEqualTo(7736663L);
    }

    @Test
    public void canSetArrayOption() {
        Integer[] array = new Integer[]{1, 2, 3};
        additionalData.addData("myArray", array);
        assertThat(additionalData.getValue("myArray")).isEqualTo(array);

        setForced("myArray", "integer[]", array);
        assertThat(additionalData.getValue("myArray", Integer[].class)).isEqualTo(array);
    }

    @Test
    public void canSetNonPrimitiveArrayOption() {
        NonPrimitiveClass[] array = new NonPrimitiveClass[]{new NonPrimitiveClass("one"), new NonPrimitiveClass("two")};
        additionalData.addData("myArray", array);
        assertThat(additionalData.getValue("myArray")).isEqualTo(array);

        setForced("myArray", "nonprimitiveclass[]", array);
        assertThat(additionalData.getValue("myArray", NonPrimitiveClass[].class)).isEqualTo(array);
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

        setForced("customer", "customer", customer);
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

        setForced("test", "string", "hello");
        assertThat(additionalData.getValue("test", String[].class)).hasSize(1).containsOnly("hello");
    }

    @Test
    public void canRetrieveSingleBoxedValueAsAnArray() throws Exception {
        additionalData.addData("test", 1);
        Integer[] tests = additionalData.getValue("test", Integer[].class);
        assertThat(tests).hasSize(1).containsOnly(1);

        setForced("test", "integer", 1);
        assertThat(additionalData.getValue("test", Integer[].class)).hasSize(1).containsOnly(1);
    }

    @Test
    public void canRetrieveSingleNonPrimitiveValueAsAnArray() throws Exception {
        NonPrimitiveClass npc = new NonPrimitiveClass("one");
        additionalData.addData("test", npc);
        NonPrimitiveClass[] tests = additionalData.getValue("test", NonPrimitiveClass[].class);
        assertThat(tests).hasSize(1).containsOnly(npc);

        setForced("test", "nonprimitiveclass", npc);
        assertThat(additionalData.getValue("test", NonPrimitiveClass[].class)).hasSize(1).containsOnly(npc);
    }

    @Test
    public void retrievingBoxedPrimitiveAsPrimitiveArrayShouldReturnNull() throws Exception {
        additionalData.addData("test", 1);
        int[] tests = additionalData.getValue("test", int[].class);
        assertThat(tests).isNull();

        setForced("test", "integer", 1);
        assertThat(additionalData.getValue("test", int[].class)).isNull();
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
    public void unsupportedTypeIsNull() {
        setForced("myExtra", "unknown", "ext");
        assertThat(additionalData.getValue("myExtra")).isNull();
    }

    private void setForced(String key, String type, Object value) {
        Map<String, JsonOption> map = new HashMap();
        map.put(key, new JsonOption(value, type));
        additionalData = new AdditionalData(map);
    }
}
