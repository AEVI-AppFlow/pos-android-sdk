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

    @Test
    public void checkGetTotalFractionalAmount() throws Exception {
        BasketItem basketItem = new BasketItemBuilder()
                .withLabel("ice")
                .withQuantity(2)
                .withBaseAmountAndModifiers(500,
                                            new BasketItemModifierBuilder("tax1", "tax").withFractionalAmount(202.05f).build(),
                                            new BasketItemModifierBuilder("tax2", "tax").withPercentage(24.56f).build(),
                                            new BasketItemModifierBuilder("tax3", "tax").withAmount(200).build(),
                                            new BasketItemModifierBuilder("tax4", "tax").withFractionalAmount(-5.5f).build(),
                                            new BasketItemModifierBuilder("tax5", "tax").withPercentage(-25.003f).build()).build();

        float expected = 2 * (500 + 202.05f + (500 * 0.2456f) + 200 + (-5.5f) + (500 * -0.25003f));
        assertThat(basketItem.getTotalFractionalAmount()).isEqualTo(expected);
    }
}
