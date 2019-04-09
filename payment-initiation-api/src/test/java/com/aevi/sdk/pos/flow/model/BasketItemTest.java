package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemTest {

    private BasketItem defaultItem =
            new BasketItem("123", "Pandoras Box", "Greek Myths", 1000, 500, 2, new Measurement(2.5f, "kg"), null, null, null);

    @Test
    public void checkFieldSetCorrectly() {
        assertThat(defaultItem.getLabel()).isEqualTo("Pandoras Box");
        assertThat(defaultItem.getCategory()).isEqualTo("Greek Myths");
        assertThat(defaultItem.getIndividualAmount()).isEqualTo(1000);
        assertThat(defaultItem.getIndividualBaseAmount()).isEqualTo(500);
        assertThat(defaultItem.getTotalAmount()).isEqualTo(2000);
        assertThat(defaultItem.getTotalBaseAmount()).isEqualTo(1000);
        assertThat(defaultItem.getQuantity()).isEqualTo(2);
        assertThat(defaultItem.hasMeasurement()).isTrue();
        assertThat(defaultItem.getMeasurement()).isEqualTo(new Measurement(2.5f, "kg"));
    }
}
