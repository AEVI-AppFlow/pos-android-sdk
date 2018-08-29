/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.model;


import com.aevi.sdk.flow.FlowClient;
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
     * Note that this list is not filtered to take flow configurations into account, meaning that flow services in this list are not
     * necessarily eligible to be called for any given request/transaction type.
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
     * Retrieve a consolidated set of all the supported request types across all the flow services.
     *
     * Note that not all of these types may be allowed for use - see {@link FlowClient#getSupportedRequestTypes()} for a filtered list.
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
