package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.model.AdditionalData;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void canGetOptionOfComplexType() throws IOException {
        additionalData.addData("basket", new com.aevi.sdk.pos.flow.model.Basket());
        assertThat(additionalData.getValue("basket", com.aevi.sdk.pos.flow.model.Basket.class)).isNotNull();
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
}
