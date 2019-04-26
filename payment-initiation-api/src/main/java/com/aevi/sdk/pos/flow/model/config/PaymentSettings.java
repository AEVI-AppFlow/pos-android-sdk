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
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents all available payment settings, configurations and flow service information.
 */
public class PaymentSettings implements Jsonable {

    private final FlowConfigurations flowConfigurations;
    private final PaymentFlowServices allServices;
    private final FpsSettings fpsSettings;
    private final AppFlowSettings appFlowSettings;
    private final AdditionalData additionalSettings;

    public PaymentSettings(FlowConfigurations flowConfigurations, PaymentFlowServices paymentFlowServices,
                           FpsSettings fpsSettings, AppFlowSettings appFlowSettings, AdditionalData additionalSettings) {
        this.flowConfigurations = flowConfigurations;
        this.allServices = paymentFlowServices;
        this.fpsSettings = fpsSettings != null ? fpsSettings : new FpsSettings();
        this.appFlowSettings = appFlowSettings != null ? appFlowSettings : new AppFlowSettings();
        this.additionalSettings = additionalSettings != null ? additionalSettings : new AdditionalData();
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
     * Retrieve the current AppFlow settings that are generally applicable to any application integrated with AppFlow.
     *
     * These settings are typically controlled by the acquirer and/or merchant.
     *
     * @return The AppFlow settings
     */
    @NonNull
    public AppFlowSettings getAppFlowSettings() {
        return appFlowSettings;
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
     * Get an instance of {@link FlowConfigurations} which can be used to retrieve information about all the flows.
     *
     * @return An instance of {@link FlowConfigurations}
     */
    @NonNull
    public FlowConfigurations getFlowConfigurations() {
        return flowConfigurations;
    }

    /**
     * Get an instance of {@link PaymentFlowServices} that contains a filtered list of services based on the provided flow configuration.
     *
     * This can be used to ensure that you retrieve the correct set of supported currencies, payment methods, etc based on the flow that
     * will be processed.
     *
     * If no applications are defined in the flow config, it is assumed any is eligible and the full list of services will be returned.
     *
     * If there is no flow by the provided name, all services will be returned.
     *
     * @param flowName The name of the flow configuration to filter services by
     * @return An instance of {@link PaymentFlowServices} with filtered set of services, or null if no flow config found
     */
    @NonNull
    public PaymentFlowServices getServicesForFlow(String flowName) {
        FlowConfig flowConfig = flowConfigurations.getFlowConfiguration(flowName);
        if (flowConfig == null) {
            return allServices;
        }

        Set<PaymentFlowServiceInfo> paymentFlowServices = new HashSet<>();
        for (FlowStage flowStage : flowConfig.getStages(true)) {
            if (flowStage.getAppExecutionType() != AppExecutionType.NONE && !flowStage.getFlowApps().isEmpty()) {
                for (FlowApp flowApp : flowStage.getFlowApps()) {
                    PaymentFlowServiceInfo flowServiceFromId = allServices.getFlowServiceFromId(flowApp.getId());
                    if (flowServiceFromId != null) {
                        paymentFlowServices.add(flowServiceFromId);
                    }
                }
            }
        }
        if (!paymentFlowServices.isEmpty()) {
            return new PaymentFlowServices(paymentFlowServices);
        }

        return allServices;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentSettings fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentSettings.class);
    }
}
