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

package com.aevi.sdk.flow.model.config;


import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the processing service (system) settings.
 */
public class SystemSettings implements Jsonable {

    private final List<FlowConfig> flowConfigurations;
    private final List<String> flowTypes;
    private final FpsSettings fpsSettings;
    private final AdditionalData additionalSettings;

    public SystemSettings(List<FlowConfig> flowConfigurations, FpsSettings fpsSettings, AdditionalData additionalSettings) {
        this.flowConfigurations = flowConfigurations;
        this.fpsSettings = fpsSettings;
        this.additionalSettings = additionalSettings;
        this.flowTypes = new ArrayList<>();
        for (FlowConfig flowConfiguration : flowConfigurations) {
            flowTypes.add(flowConfiguration.getType());
        }
    }

    public List<FlowConfig> getFlowConfigurations() {
        return flowConfigurations;
    }

    public List<String> getFlowTypes() {
        return flowTypes;
    }

    public FpsSettings getFpsSettings() {
        return fpsSettings;
    }

    public AdditionalData getAdditionalSettings() {
        return additionalSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemSettings that = (SystemSettings) o;
        return Objects.equals(flowConfigurations, that.flowConfigurations) &&
                Objects.equals(fpsSettings, that.fpsSettings) &&
                Objects.equals(additionalSettings, that.additionalSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowConfigurations, fpsSettings, additionalSettings);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static SystemSettings fromJson(String json) {
        return JsonConverter.deserialize(json, SystemSettings.class);
    }
}
