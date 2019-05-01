package com.aevi.sdk.pos.flow.model;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketItemBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeQuantity() throws Exception {
        new BasketItemBuilder().withLabel("bla").withQuantity(-1).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfIdIsNull() throws Exception {
        new BasketItemBuilder().withId(null).withQuantity(1).withLabel("bla").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLabelNull() throws Exception {
        new BasketItemBuilder().withQuantity(1).build();
    }

    @Test
    public void shouldInitialiseWithDefaultRandomId() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withLabel("banana").build();
        assertThat(basketItem.getId()).isNotEmpty();
    }

    @Test
    public void hasMethodsShouldReturnFalseByDefault() throws Exception {
        BasketItem basketItem = new BasketItemBuilder().withLabel("banana").build();
        assertThat(basketItem.hasMeasurement()).isFalse();
        assertThat(basketItem.hasModifiers()).isFalse();
        assertThat(basketItem.hasItemData()).isFalse();
    }

    @Test
    public void shouldCreateCorrectBasketItem() throws Exception {
        BasketItemModifier modifier = new BasketItemModifier("bla", "Cheeeese!", "addon", 100f, 50.0f);
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withMeasurement(1.25f, "kg")
                        .withCategory("fruit")
                        .withBaseAmountAndModifiers(100, modifier)
                        .withItemData("bleep", "blarp")
                        .build();
        assertThat(basketItem.getId()).isEqualTo("123");
        assertThat(basketItem.getLabel()).isEqualTo("banana");
        assertThat(basketItem.getCategory()).isEqualTo("fruit");
        assertThat(basketItem.getQuantity()).isEqualTo(2);
        assertThat(basketItem.getIndividualAmount()).isEqualTo(200);
        assertThat(basketItem.getIndividualBaseAmount()).isEqualTo(100.0f);
        assertThat(basketItem.getTotalAmount()).isEqualTo(400);
        assertThat(basketItem.getTotalBaseAmount()).isEqualTo(200.0f);
        assertThat(basketItem.hasMeasurement()).isTrue();
        assertThat(basketItem.getMeasurement()).isEqualTo(new Measurement(1.25f, "kg"));
        assertThat(basketItem.hasModifiers()).isTrue();
        List<BasketItemModifier> modifiers = basketItem.getModifiers();
        assertThat(modifiers.get(0)).isEqualTo(modifier);
        assertThat(basketItem.hasItemData()).isTrue();
        assertThat(basketItem.getItemData().getStringValue("bleep")).isEqualTo("blarp");
    }

    @Test
    public void shouldAllowCreationFromExistingItem() throws Exception {
        BasketItemModifier modifier = new BasketItemModifier("bla", "Cheeeese!", "addon", 100f, 50.0f);
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withMeasurement(1.25f, "kg")
                        .withCategory("fruit")
                        .withBaseAmountAndModifiers(100, modifier)
                        .withItemData("bleep", "blarp")
                        .build();
        BasketItem newItem = new BasketItemBuilder(basketItem).withQuantity(4).build();

        assertThat(newItem.getId()).isEqualTo("123");
        assertThat(newItem.getLabel()).isEqualTo("banana");
        assertThat(newItem.getCategory()).isEqualTo("fruit");
        assertThat(newItem.getQuantity()).isEqualTo(4);
        assertThat(newItem.getIndividualAmount()).isEqualTo(200);
        assertThat(newItem.getIndividualBaseAmount()).isEqualTo(100.0f);
        assertThat(newItem.getTotalAmount()).isEqualTo(800);
        assertThat(newItem.getTotalBaseAmount()).isEqualTo(400.0f);
        assertThat(newItem.hasMeasurement()).isTrue();
        assertThat(newItem.getMeasurement()).isEqualTo(new Measurement(1.25f, "kg"));
        assertThat(newItem.hasModifiers()).isTrue();
        List<BasketItemModifier> modifiers = newItem.getModifiers();
        assertThat(modifiers.get(0)).isEqualTo(modifier);
    }

    @Test
    public void shouldAllowIncrementItemQuantity() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).incrementQuantity().build();
        assertThat(newItem.getQuantity()).isEqualTo(3);
    }

    @Test
    public void shouldAllowDecrementItemQuantity() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).decrementQuantity().build();
        assertThat(newItem.getQuantity()).isEqualTo(1);
    }

    @Test
    public void shouldAllowOffsetPositiveItemQuantity() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).offsetQuantityBy(2).build();
        assertThat(newItem.getQuantity()).isEqualTo(4);
    }

    @Test
    public void shouldAllowOffsetNegativeItemQuantity() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").withAmount(200).build();
        BasketItem newItem = new BasketItemBuilder(basketItem).offsetQuantityBy(-1).build();
        assertThat(newItem.getQuantity()).isEqualTo(1);
    }

    @Test
    public void withFractionalQuantityShouldUseMeasureIfUnitIsSet() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withAmount(200).withFractionalQuantity(2.0f, "kg").build();
        assertThat(basketItem.getQuantity()).isEqualTo(1);
        assertThat(basketItem.getMeasurement()).isEqualTo(new Measurement(2.0f, "kg"));
    }

    @Test
    public void withFractionalQuantityShouldUseQuantityIfUnitIsNotSet() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withAmount(200).withFractionalQuantity(2.0f, null).build();
        assertThat(basketItem.getQuantity()).isEqualTo(2);
        assertThat(basketItem.getMeasurement()).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void withFractionalQuantityShouldThrowExIfNoUnitWithFractions() throws Exception {
        new BasketItemBuilder().withId("123").withLabel("banana").withAmount(200).withFractionalQuantity(2.1f, null).build();
    }

    @Test
    public void amountsShouldDefaultToZero() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").build();
        assertThat(basketItem.getIndividualAmount()).isEqualTo(0);
        assertThat(basketItem.getIndividualBaseAmount()).isEqualTo(0);
    }

    @Test
    public void baseAmountShouldDefaultToAmount() throws Exception {
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit").withAmount(200).build();
        assertThat(basketItem.getIndividualBaseAmount()).isEqualTo(200.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTryingToSetAmountAfterSettingModifiers() throws Exception {
        new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit")
                .withBaseAmountAndModifiers(10.0f, new BasketItemModifierBuilder("1", "2").withPercentage(20f).build())
                .withAmount(50)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTryingToSetModifiersAfterAmount() throws Exception {
        new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withCategory("fruit")
                .withAmount(50)
                .withBaseAmountAndModifiers(10.0f, new BasketItemModifierBuilder("1", "2").withPercentage(20f).build())
                .build();
    }

    @Test
    public void shouldCalculateAmountIfModifiersAreSet() throws Exception {
        BasketItemModifier modifier1 = new BasketItemModifier("bla", "Cheeeese!", "addon", 100f, 50.0f);
        BasketItemModifier modifier2 = new BasketItemModifier("bla2", "Cheeeese!2", "discount", null, -5.5f);
        BasketItem basketItem =
                new BasketItemBuilder().withId("123").withLabel("banana").withQuantity(2).withMeasurement(1.25f, "kg")
                        .withCategory("fruit")
                        .withBaseAmountAndModifiers(100, modifier1, modifier2)
                        .withItemData("bleep", "blarp")
                        .build();

        assertThat(basketItem.getIndividualAmount()).isEqualTo(195);
    }
}
