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

import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.pos.flow.model.PaymentStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/**
 * Exposes the various flows and helper methods to query for information.
 */
public class FlowConfigurations {

    private final List<FlowConfig> flowConfigurations;

    public FlowConfigurations(List<FlowConfig> flowConfigurations) {
        this.flowConfigurations = flowConfigurations;
    }

    /**
     * Get the list of flow configurations.
     *
     * @return The list of {@link FlowConfig}
     */
    public List<FlowConfig> getAll() {
        return flowConfigurations;
    }

    /**
     * Stream the list of flow configurations for simple filtering, conversions, etc.
     *
     * @return An Observable stream of {@link FlowConfig}
     */
    public Observable<FlowConfig> stream() {
        return Observable.fromIterable(flowConfigurations);
    }

    /**
     * Get the flow configuration with the provided name.
     *
     * @param flowName The flow name
     * @return The flow config
     */
    public FlowConfig getFlowConfiguration(final String flowName) {
        return fromName(flowName);
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
        return stream()
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

}
