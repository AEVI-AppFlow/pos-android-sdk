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

package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.config.AppExecutionType;
import com.aevi.sdk.flow.model.config.FlowApp;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.flow.model.config.FlowStage;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the payment flow configuration.
 */
public class PaymentFlowConfiguration implements Jsonable {

    private final List<FlowConfig> flowConfigurations;
    private final PaymentFlowServices allServices;
    private final List<String> flowNames;
    private final Set<String> flowTypes;

    public PaymentFlowConfiguration(List<FlowConfig> flowConfigurations, PaymentFlowServices paymentFlowServices, String callingPackageId) {
        this.flowConfigurations = flowConfigurations;
        this.allServices = paymentFlowServices;
        this.flowNames = new ArrayList<>();
        this.flowTypes = new HashSet<>();
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (!flowConfiguration.hasAppFilter() || flowConfiguration.getAppFilter().equals(callingPackageId)) {
                flowNames.add(flowConfiguration.getName());
                flowTypes.add(flowConfiguration.getType());
            }
        }
    }

    /**
     * Get the list of the names of all the defined flows.
     *
     * @return The list of flow names
     */
    public List<String> getFlowNames() {
        return flowNames;
    }

    /**
     * Get the set of flow types for all the defined flows.
     *
     * @return The list of flow types
     */
    public Set<String> getFlowTypes() {
        return flowTypes;
    }

    /**
     * Get an instance of {@link PaymentFlowServices} which can be used to query for information across all the flow services.
     *
     * @return An instance of {@link PaymentFlowServices}
     */
    public PaymentFlowServices getPaymentFlowServices() {
        return allServices;
    }

    /**
     * Returns a union of defined generic flow types and custom request types reported by the services.
     *
     * TODO This should be removed once FPS proactively creates flow configs for custom types
     *
     * @return A set of all the generic request types
     */
    public Set<String> getAllGenericRequestTypes() {
        Set<String> union = new HashSet<>();
        union.addAll(getFlowNamesForType(FlowConfig.TYPE_GENERIC));
        union.addAll(allServices.getAllCustomRequestTypes());
        return union;
    }

    /**
     * Get an instance of {@link PaymentFlowServices} that contains a filtered list of services based on the provided flow configuration.
     *
     * @param flowName The name of the flow configuration to filter services by
     * @return An instance of {@link PaymentFlowServices} with filtered set of services
     */
    // TODO needs optimisation
    public PaymentFlowServices getServicesForFlow(String flowName) {
        FlowConfig flowConfig = fromName(flowName);
        if (flowConfig != null) {
            Set<PaymentFlowServiceInfo> paymentFlowServices = new HashSet<>();
            for (FlowStage flowStage : flowConfig.getAllStages()) {
                if (flowStage.getAppExecutionType() != AppExecutionType.NONE && !flowStage.getFlowApps().isEmpty()) {
                    for (FlowApp flowApp : flowStage.getFlowApps()) {
                        paymentFlowServices.add(allServices.getFlowServiceFromId(flowApp.getId()));
                    }
                }
            }
            if (!paymentFlowServices.isEmpty()) {
                return new PaymentFlowServices(paymentFlowServices);
            }
        }
        return allServices;
    }

    /**
     * Get a list of all the flow names that are associated with the provided type.
     *
     * @param type The type to filter by
     * @return The list of flow names
     */
    public List<String> getFlowNamesForType(String type) {
        List<String> flowNames = new ArrayList<>();
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (flowConfiguration.getType().equals(type)) {
                flowNames.add(flowConfiguration.getName());
            }
        }
        return flowNames;
    }

    /**
     * Check whether split is enabled as a stage for a particular flow.
     *
     * @param flowName The flow to check if split is enabled for
     * @return True if split is enabled as a stage, false otherwise
     */
    public boolean isSplitEnabledForFlow(String flowName) {
        FlowConfig flowConfig = fromName(flowName);
        if (flowConfig != null) {
            return flowConfig.hasStage(PaymentStage.SPLIT.name());
        }
        return false;
    }

    private FlowConfig fromName(String flowName) {
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (flowConfiguration.getName().equals(flowName)) {
                return flowConfiguration;
            }
        }
        return null;
    }

    private FlowConfig fromType(String flowType) {
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (flowConfiguration.getType().equals(flowType)) {
                return flowConfiguration;
            }
        }
        return null;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentFlowConfiguration fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentFlowConfiguration.class);
    }
}
