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

import com.aevi.sdk.flow.FlowBaseConfig;

import java.util.ArrayList;
import java.util.List;

import static com.aevi.sdk.flow.util.Preconditions.checkNotEmpty;

/**
 * Builder class for {@link FlowConfig}.
 */
public class FlowConfigBuilder {

    private String name;
    private String type;
    private int version = 1;
    private int apiMajorVersion = getMajorVersionNumber();
    private String description;
    private String restrictedToApp;
    private List<FlowStage> stages = new ArrayList<>();

    /**
     * Set the name for the flow.
     *
     * The name is a unique identifier for a flow and is used to indicate what flow should be used when initiating a request.
     *
     * Mandatory field.
     *
     * @param name The name of the flow
     * @return This builder
     */
    public FlowConfigBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the type for the flow.
     *
     * The flow type represents what function the flow fills - such as a sale/purchase or tokenisation.
     *
     * There is a set of defined types via the documentation. In addition, flow services can support custom types.
     *
     * @param type The type of the flow
     * @return This builder
     */
    public FlowConfigBuilder withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Set the version of the flow.
     *
     * This is a simple integer counter that should be bumped each time a flow changes.
     *
     * If not set, it will default to 1.
     *
     * @param version The version of the flow
     * @return This builder
     */
    public FlowConfigBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    /**
     * Set the API major version compatibility for this flow.
     *
     * This indicates what major API version this flow is compatible with, as there may be breaking changes in the flow configs between major
     * releases.
     *
     * This field is mainly useful for flows defined via JSON where there is no direct link to the current API version.
     *
     * If not set, it will default to the current API version.
     *
     * @param apiMajorVersion The major API version this flow is compatible with
     * @return This builder
     */
    public FlowConfigBuilder withApiMajorVersionCompatibility(int apiMajorVersion) {
        this.apiMajorVersion = apiMajorVersion;
        return this;
    }

    /**
     * Set the description of the flow.
     *
     * This can be used to add a human readable description of what the flow is for and what it does.
     *
     * @param description Description of the flow
     * @return This builder
     */
    public FlowConfigBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set a restriction for what client app is allowed to read and use this flow.
     *
     * This can be used to limit a flow to a specific client app (a POS app etc).
     *
     * @param appId The app package name to filter by
     * @return This builder
     */
    public FlowConfigBuilder withRestrictedToApp(String appId) {
        this.restrictedToApp = appId;
        return this;
    }

    /**
     * Set the stages for this flow.
     *
     * @param stages The stages for this flow
     * @return This builder
     */
    public FlowConfigBuilder withStages(List<FlowStage> stages) {
        this.stages = stages;
        return this;
    }

    /**
     * Build a flow config instance.
     *
     * @return The flow config
     */
    public FlowConfig build() {
        checkNotEmpty(name, "Name must be set");
        checkNotEmpty(type, "Type must be set");
        return new FlowConfig(name, type, version, apiMajorVersion, description, restrictedToApp, stages);
    }

    private static int getMajorVersionNumber() {
        String version = FlowBaseConfig.VERSION;
        return Character.getNumericValue(version.charAt(0));
    }
}
