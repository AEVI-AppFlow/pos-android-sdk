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

package com.aevi.sdk.pos.flow.model.config;

import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.config.*;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServices;
import com.aevi.sdk.pos.flow.model.PaymentStage;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.*;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Predicate;

/**
 * Represents all available payment settings, configurations and flow service information.
 */
public class PaymentSettings implements Jsonable {

    private final List<FlowConfig> flowConfigurations;
    private final PaymentFlowServices allServices;
    private final FpsSettings fpsSettings;
    private final AdditionalData additionalSettings;

    public PaymentSettings(List<FlowConfig> flowConfigurations, PaymentFlowServices paymentFlowServices,
                           FpsSettings fpsSettings, AdditionalData additionalSettings) {
        this.flowConfigurations = Collections.unmodifiableList(flowConfigurations);
        this.allServices = paymentFlowServices;
        this.fpsSettings = fpsSettings;
        this.additionalSettings = additionalSettings;
    }

    /**
     * Retrieve the current settings for FPS, the Flow Processing Service, that is responsible for processing requests.
     *
     * These settings are typically controlled by the acquirer and/or merchant.
     *
     * @return The FPS settings
     */
    @NonNull
    public FpsSettings getFpsSettings() {
        return fpsSettings;
    }

    /**
     * Get any additional settings relating to the overall system or device.
     *
     * See documentation for possible keys and values.
     *
     * @return Additional settings
     */
    @NonNull
    public AdditionalData getAdditionalSettings() {
        return additionalSettings;
    }

    /**
     * Get an instance of {@link PaymentFlowServices} which can be used to query for information across all the flow services.
     *
     * @return An instance of {@link PaymentFlowServices}
     */
    @NonNull
    public PaymentFlowServices getPaymentFlowServices() {
        return allServices;
    }

    /**
     * Returns a stream of flow configurations which can be used to easily apply filters or transform into other types.
     *
     * See documentation for examples.
     *
     * @return An observable stream of {@link FlowConfig}
     */
    public Observable<FlowConfig> getFlowConfigurations() {
        return Observable.fromIterable(flowConfigurations);
    }

    /**
     * Check whether a flow type is supported or not.
     *
     * A flow type is defined as supported if there is at least one flow configuration defined for that type.
     *
     * @param type The flow type to check
     * @return True if there is at least one flow for this type, false otherwise
     */
    public boolean isFlowTypeSupported(final String type) {
        return getFlowConfigurations()
                .filter(new Predicate<FlowConfig>() {
                    @Override
                    public boolean test(FlowConfig flowConfig) throws Exception {
                        return flowConfig.getType().equals(type);
                    }
                })
                .count()
                .blockingGet() > 0;
    }

    /**
     * Get an instance of {@link PaymentFlowServices} that contains a filtered list of services based on the provided flow configuration.
     *
     * This can be used to ensure that you retrieve the correct set of supported currencies, payment methods, etc based on the flow that
     * will be processed.
     *
     * If no applications are defined in the flow config, it is assumed any is eligible and the full list of services will be returned.
     *
     * @param flowName The name of the flow configuration to filter services by
     * @return An instance of {@link PaymentFlowServices} with filtered set of services, or null if no flow config found
     */
    // TODO needs optimisation
    @Nullable
    public PaymentFlowServices getServicesForFlow(String flowName) {
        FlowConfig flowConfig = fromName(flowName);
        if (flowConfig == null) {
            return null;
        }

        Set<PaymentFlowServiceInfo> paymentFlowServices = new HashSet<>();
        for (FlowStage flowStage : flowConfig.getStages(true)) {
            if (flowStage.getAppExecutionType() != AppExecutionType.NONE && !flowStage.getFlowApps().isEmpty()) {
                for (FlowApp flowApp : flowStage.getFlowApps()) {
                    paymentFlowServices.add(allServices.getFlowServiceFromId(flowApp.getId()));
                }
            }
        }
        if (!paymentFlowServices.isEmpty()) {
            return new PaymentFlowServices(paymentFlowServices);
        }

        return allServices;
    }

    /**
     * Get a list of all the flow names that are associated with the provided types.
     *
     * @param typesArray The types to filter by
     * @return The list of flow names
     */
    @NonNull
    public List<String> getFlowNamesForType(String... typesArray) {
        List<String> flowNames = new ArrayList<>();
        List<String> types = Arrays.asList(typesArray);
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (types.contains(flowConfiguration.getType())) {
                flowNames.add(flowConfiguration.getName());
            }
        }
        return flowNames;
    }

    /**
     * Get a list of all the flow configs that are associated with the provided types.
     *
     * @param typesArray The types to filter by
     * @return The list of flow names
     */
    @NonNull
    public List<FlowConfig> getFlowConfigsForType(String... typesArray) {
        List<FlowConfig> flowConfigs = new ArrayList<>();
        List<String> types = Arrays.asList(typesArray);
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (types.contains(flowConfiguration.getType())) {
                flowConfigs.add(flowConfiguration);
            }
        }
        return flowConfigs;
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

    public static PaymentSettings fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentSettings.class);
    }
}
