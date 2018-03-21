package com.aevi.sdk.flow.model;


import com.aevi.sdk.flow.util.ComparisonUtil;

import java.util.*;

/**
 * Represents the available flow services and exposes a range of helper functions.
 */
public class FlowServices {

    private final List<FlowServiceInfo> flowServiceInfoList;

    public FlowServices(List<FlowServiceInfo> flowServiceInfoList) {
        this.flowServiceInfoList = flowServiceInfoList;
    }

    /**
     * Retrieve the full list of available flow services.
     *
     * @return The full list of available flow services
     */
    public List<FlowServiceInfo> getAllFlowServices() {
        return flowServiceInfoList;
    }

    /**
     * Retrieve a list of all the flow services that operates in the given stage.
     *
     * @param stage The stage to match against
     * @return A list of all flow services that operates in the given stage
     */
    public List<FlowServiceInfo> getAllFlowServicesForStage(String stage) {
        List<FlowServiceInfo> flowServices = new ArrayList<>();
        for (FlowServiceInfo flowServiceInfo : flowServiceInfoList) {
            if (flowServiceInfo.containsStage(stage)) {
                flowServices.add(flowServiceInfo);
            }
        }
        return flowServices;
    }

    /**
     * Retrieve a consolidated set of all the capabilities provided by the flow services.
     *
     * @return A consolidated set of all the capabilities provided by the flow services
     */
    public Set<String> getAllCapabilities() {
        Set<String> capabilities = new HashSet<>();
        for (FlowServiceInfo flowServiceInfo : flowServiceInfoList) {
            capabilities.addAll(Arrays.asList(flowServiceInfo.getCapabilities()));
        }
        return capabilities;
    }

    /**
     * Retrieve a list of all the flow services that provides the given capability.
     *
     * @param capability The capability to match against
     * @return A list of all flow services that provides the given capability
     */
    public List<FlowServiceInfo> getAllFlowServicesWithCapability(String capability) {
        List<FlowServiceInfo> flowServices = new ArrayList<>();
        for (FlowServiceInfo flowServiceInfo : flowServiceInfoList) {
            if (flowServiceInfo.providesCapability(capability)) {
                flowServices.add(flowServiceInfo);
            }
        }
        return flowServices;
    }

    /**
     * Retrieve a consolidated set of all the supported request types across all the flow services.
     *
     * @return A consolidated set of all the supported request types across all the flow services
     */
    public Set<String> getAllSupportedRequestTypes() {
        Set<String> requestTypes = new HashSet<>();
        for (FlowServiceInfo serviceInfo : flowServiceInfoList) {
            requestTypes.addAll(Arrays.asList(serviceInfo.getSupportedRequestTypes()));
        }
        return requestTypes;
    }

    /**
     * Retrieve a consolidated set of supported payment methods across all the flow services.
     *
     * @return A consolidated set of supported payment methods across all the flow services
     */
    public Set<String> getAllSupportedPaymentMethods() {
        Set<String> paymentMethods = new HashSet<>();
        for (FlowServiceInfo serviceInfo : flowServiceInfoList) {
            paymentMethods.addAll(Arrays.asList(serviceInfo.getPaymentMethods()));
        }
        return paymentMethods;
    }

    /**
     * Check whether a particular data key (as used with {@link com.aevi.sdk.flow.model.AdditionalData}) is supported by at least one of the flow services.
     *
     * @param dataKey The data key to check if supported
     * @return True if at least one flow service support it, false otherwise
     */
    public boolean isDataKeySupported(String dataKey) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(getAllSupportedDataKeys(), dataKey);
    }

    /**
     * Retrieve a consolidated set of supported data keys ((as used with {@link com.aevi.sdk.flow.model.AdditionalData}) across all the flow services.
     *
     * @return A consolidated set of supported data keys across all the flow services
     */
    public Set<String> getAllSupportedDataKeys() {
        Set<String> dataKeys = new HashSet<>();
        for (FlowServiceInfo serviceInfo : flowServiceInfoList) {
            dataKeys.addAll(Arrays.asList(serviceInfo.getSupportedDataKeys()));
        }
        return dataKeys;
    }

    /**
     * Retrieve a list of all the flow services that support accessibility mode for visually impaired users.
     *
     * @return A list of all flow services that supports accessibility mode for visually impaired users
     */
    public List<FlowServiceInfo> getFlowServicesSupportingAccessibilityMode() {
        List<FlowServiceInfo> flowServices = new ArrayList<>();
        for (FlowServiceInfo serviceInfo : flowServiceInfoList) {
            if (serviceInfo.supportsAccessibilityMode()) {
                flowServices.add(serviceInfo);
            }
        }
        return flowServices;
    }
}
