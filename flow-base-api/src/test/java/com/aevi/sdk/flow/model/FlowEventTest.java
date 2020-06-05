package com.aevi.sdk.flow.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FlowEventTest {


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canSerialise() {
        FlowEvent fe = new FlowEvent("blah");

        String json = fe.toJson();

        FlowEvent result = FlowEvent.fromJson(json);

        assertThat(fe).isEqualTo(result);
    }

    @Test
    public void canSetAndGetDomainEventData() {
        TestEventObjectData data = new TestEventObjectData();
        FlowEvent fe = new FlowEvent("blah", data);

        assertThat(fe.getEventData(TestEventObjectData.class)).isNotNull();
        assertThat(fe.getEventData(TestEventObjectData.class)).isEqualTo(data);
    }

    @Test
    public void shouldReturnNullIfWrongTypeUsed() {
        TestEventObjectData data = new TestEventObjectData();
        FlowEvent fe = new FlowEvent("blah", data);

        assertThat(fe.getEventData(FlowEvent.class)).isNull();
    }

    class TestEventObjectData {
        String banana = "Woo";
        int apple = 10;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestEventObjectData that = (TestEventObjectData) o;
            return apple == that.apple &&
                    Objects.equals(banana, that.banana);
        }

        @Override
        public int hashCode() {
            return Objects.hash(banana, apple);
        }
    }

}
