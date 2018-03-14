package com.aevi.sdk.flow.model;


import android.content.Context;

import com.aevi.sdk.flow.ContextHelper;
import com.aevi.sdk.pos.flow.model.FlowServiceInfoBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FlowServicesTest {

    private FlowServiceInfo flowServiceInfoOne;
    private FlowServiceInfo flowServiceInfoTwo;

    private Context context;
    private FlowServices flowServices;

    @Before
    public void setUp() throws Exception {
        context = ContextHelper.mockContext("com.test", "1.2.3");
        buildFlowServiceOne();
        buildFlowServiceTwo();
        List<FlowServiceInfo> flowServiceInfoList = new ArrayList<>();
        flowServiceInfoList.add(flowServiceInfoOne);
        flowServiceInfoList.add(flowServiceInfoTwo);
        flowServices = new FlowServices(flowServiceInfoList);
    }

    @Test
    public void shouldContainListOfTwoEntries() throws Exception {
        assertThat(flowServices.getAllFlowServices()).hasSize(2);
    }

    @Test
    public void shouldFilterStagesCorrectly() throws Exception {
        assertThat(flowServices.getAllFlowServicesForStage("stage1")).hasSize(1);
        assertThat(flowServices.getAllFlowServicesForStage("stage2")).hasSize(2);
        assertThat(flowServices.getAllFlowServicesForStage("stage3")).hasSize(1);
    }

    @Test
    public void shouldCollateCapabilitiesCorrectly() throws Exception {
        assertThat(flowServices.getAllCapabilities()).hasSize(3).containsOnly("capOne", "capTwo", "capThree");
    }

    @Test
    public void shouldFilterCapabilitiesCorrectly() throws Exception {
        assertThat(flowServices.getAllFlowServicesWithCapability("capOne")).hasSize(1);
        assertThat(flowServices.getAllFlowServicesWithCapability("capTwo")).hasSize(2);
        assertThat(flowServices.getAllFlowServicesWithCapability("capThree")).hasSize(1);
    }

    @Test
    public void shouldCollatePaymentMethodsCorrectly() throws Exception {
        Set<String> allSupportedPaymentMethods = flowServices.getAllSupportedPaymentMethods();
        assertThat(allSupportedPaymentMethods).hasSize(3);
        assertThat(allSupportedPaymentMethods).containsOnly("pigeon", "yak", "horse");
    }

    @Test
    public void shouldCollateDataKeysCorrectly() throws Exception {
        Set<String> allSupportedDataKeys = flowServices.getAllSupportedDataKeys();
        assertThat(allSupportedDataKeys).hasSize(3);
        assertThat(allSupportedDataKeys).containsOnly("dataOne", "dataTwo", "dataThree");
    }

    @Test
    public void shouldCheckDataKeySupportedCorrectly() throws Exception {
        assertThat(flowServices.isDataKeySupported("dataOne")).isTrue();
        assertThat(flowServices.isDataKeySupported("dataTwo")).isTrue();
        assertThat(flowServices.isDataKeySupported("banana")).isFalse();
    }

    @Test
    public void shouldCollateAccessibleServicesCorrectly() throws Exception {
        List<FlowServiceInfo> flowServicesSupportingAccessibilityMode = flowServices.getFlowServicesSupportingAccessibilityMode();
        assertThat(flowServicesSupportingAccessibilityMode).hasSize(1);
    }

    @Test
    public void shouldCollateRequestTypesCorrectly() throws Exception {
        Set<String> allSupportedRequestTypes = flowServices.getAllSupportedRequestTypes();
        assertThat(allSupportedRequestTypes).hasSize(3).containsOnly("reqOne", "reqTwo", "reqThree");
    }

    private void buildFlowServiceOne() {
        flowServiceInfoOne = new FlowServiceInfoBuilder()
                .withVendor("Test One")
                .withDisplayName("Test One")
                .withSupportedRequestTypes("one", "two")
                .withStages("stage1", "stage2")
                .withCapabilities("capOne", "capTwo")
                .withCanPayAmounts(true, "pigeon", "horse")
                .withSupportedDataKeys("dataOne", "dataTwo")
                .withSupportedRequestTypes("reqOne", "reqTwo")
                .build(context);
    }

    private void buildFlowServiceTwo() {
        flowServiceInfoTwo = new FlowServiceInfoBuilder()
                .withVendor("Test Two")
                .withDisplayName("Test Two")
                .withSupportedRequestTypes("two", "three")
                .withStages("stage2", "stage3")
                .withCapabilities("capTwo", "capThree")
                .withCanPayAmounts(true, "pigeon", "yak")
                .withSupportedDataKeys("dataTwo", "dataThree")
                .withSupportsAccessibilityMode(true)
                .withSupportedRequestTypes("reqTwo", "reqThree")
                .build(context);
    }
}
