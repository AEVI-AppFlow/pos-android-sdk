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


import android.support.annotation.NonNull;
import com.aevi.sdk.flow.util.ComparisonUtil;

import java.util.*;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Common flags for service info models regardless of domain.
 */
public abstract class BaseServiceInfo extends BaseModel {

    private final String packageName;
    private final String vendor;
    private final String logicalDeviceId;
    private final String serviceVersion;
    private final String apiVersion;
    private final String displayName;
    private final boolean hasAccessibilityMode;
    private final Set<String> supportedFlowTypes;
    private final Set<String> customRequestTypes;
    private final Set<String> supportedDataKeys;
    private final AdditionalData additionalInfo;
    private Set<String> stages;
    private Map<String, String[]> flowAndStagesDefinitions;

    // Default constructor for deserialisation
    protected BaseServiceInfo() {
        this("", "", "", "", "", "", "", false,
             null, null, null, null);
    }

    protected BaseServiceInfo(String id, String packageName, String vendor, String logicalDeviceId, String serviceVersion, String apiVersion,
                              String displayName, boolean hasAccessibilityMode, Set<String> supportedFlowTypes, Set<String> customRequestTypes,
                              Set<String> supportedDataKeys, AdditionalData additionalInfo) {
        super(id);
        this.packageName = packageName;
        this.vendor = vendor;
        this.logicalDeviceId = logicalDeviceId;
        this.serviceVersion = serviceVersion;
        this.apiVersion = apiVersion;
        this.displayName = displayName;
        this.hasAccessibilityMode = hasAccessibilityMode;
        this.supportedFlowTypes = supportedFlowTypes != null ? supportedFlowTypes : new HashSet<String>();
        this.customRequestTypes = customRequestTypes != null ? customRequestTypes : new HashSet<String>();
        this.supportedDataKeys = supportedDataKeys != null ? supportedDataKeys : new HashSet<String>();
        this.additionalInfo = additionalInfo != null ? additionalInfo : new AdditionalData();
        this.flowAndStagesDefinitions = new HashMap<>();
        checkArguments();
    }

    private void checkArguments() {
        checkArgument(vendor != null, "Vendor must be set");
        checkArgument(serviceVersion != null, "Service version must be set");
        checkArgument(apiVersion != null, "API version must be set");
        checkArgument(displayName != null, "Display name must be set");
    }

    /**
     * Gets the service vendor name.
     *
     * @return The service vendor name
     */
    @NonNull
    public String getVendor() {
        return vendor;
    }

    /**
     * Get the logical device id, which is the id this service uses to identify the device it is running on.
     *
     * @return The logical device id
     */
    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    /**
     * Gets the service application version.
     *
     * @return The service application version string
     */
    @NonNull
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * Gets the version of the API the service is built against.
     *
     * @return The version of the API the service built against
     */
    @NonNull
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Get the name of this service for displaying to users.
     *
     * @return The display name of this service
     */
    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns whether or not this service supports an accessible mode.
     *
     * @return true if the service has an accessible mode.
     */
    public boolean supportsAccessibilityMode() {
        return hasAccessibilityMode;
    }

    /**
     * Get the list of supported pre-defined flow types.
     *
     * See full list of supported flow types on the AppFlow wiki. Each pre-defined flow type will have a statically defined flow configuration
     * associated with it, if supported on the current device.
     *
     * Note that what flow types are available to clients is entirely determined by the flow configurations and not what flow services reports.
     * Instead, this is used to ensure applications can handle flows they have been assigned to and for various other internal uses.
     *
     * @return The set of supported pre-defined flow types
     */
    @NonNull
    public Set<String> getSupportedFlowTypes() {
        return supportedFlowTypes;
    }

    /**
     * Check whether this service supports the given flow type.
     *
     * @param flowType The flow type to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsFlowType(String flowType) {
        return supportedFlowTypes.size() > 0 && ComparisonUtil.stringCollectionContainsIgnoreCase(supportedFlowTypes, flowType);
    }

    /**
     * Get the list of custom/bespoke request types that this service has defined.
     *
     * A custom request type is a type unique to a service for which a special type of generic flow is generated dynamically.
     *
     * @return The set of request types supported by the service
     */
    @NonNull
    public Set<String> getCustomRequestTypes() {
        return customRequestTypes;
    }

    /**
     * Check whether this service supports the given request type.
     *
     * @param requestType The request type to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsCustomRequestType(String requestType) {
        return customRequestTypes.size() > 0 && ComparisonUtil.stringCollectionContainsIgnoreCase(customRequestTypes, requestType);
    }

    /**
     * Get the stages this service operates in.
     *
     * For POS flow, the possible stages are defined in the FlowStages model.
     *
     * @return The set of stages the service operates in.
     */
    public Set<String> getStages() {
        return stages;
    }

    /**
     * Check whether the flow services operates in the given stage.
     *
     * @param stage The stage to check against
     * @return True if the flow service operates in the given stage, false otherwise
     */
    public boolean containsStage(String stage) {
        return stages.size() > 0 && ComparisonUtil.stringCollectionContainsIgnoreCase(stages, stage);
    }

    /**
     * Returns an array of supported request {@link AdditionalData} keys.
     *
     * A request can set various optional and custom flags in the {@link AdditionalData} object.
     * This array will return an array of the keys this service supports.
     *
     * May be empty.
     *
     * See reference values in the documentation for possible values.
     *
     * @return An array of supported AdditionalData keys
     */
    @NonNull
    public Set<String> getSupportedDataKeys() {
        return supportedDataKeys;
    }

    /**
     * Check whether this service supports the given data key (for {@link AdditionalData usage}.
     *
     * @param dataKey The data key to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsDataKey(String dataKey) {
        return supportedDataKeys.size() > 0 && ComparisonUtil.stringCollectionContainsIgnoreCase(supportedDataKeys, dataKey);
    }

    /**
     * Get the additional info for this service.
     *
     * @return The additional info
     */
    public AdditionalData getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Get the package name for this service.
     *
     * @return The service package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * For internal use.
     *
     * @param stages Stages
     */
    public void setStages(Set<String> stages) {
        this.stages = stages;
    }

    /**
     * For internal use.
     *
     * @param stages Stages
     */
    public void setStages(String... stages) {
        this.stages = new HashSet<>(Arrays.asList(stages));
    }

    /**
     * Retrieve information on what flows this service is used in and for what stages.
     *
     * The map key is the name of the flow and the values is an array of the stages the service is defined in for that flow
     *
     * Example: "aeviSale" maps to ["PAYMENT_CARD_READING", "TRANSACTION_PROCESSING"]
     *
     * @return The map of flows and stages this service is defined in
     */
    @NonNull
    public Map<String, String[]> getFlowAndStagesDefinitions() {
        return flowAndStagesDefinitions;
    }

    /**
     * For internal use.
     *
     * @param flowName The flow name
     * @param stages   The stages
     */
    public void addFlowAndStagesDefinition(String flowName, String[] stages) {
        flowAndStagesDefinitions.put(flowName, stages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BaseServiceInfo that = (BaseServiceInfo) o;
        return hasAccessibilityMode == that.hasAccessibilityMode &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(vendor, that.vendor) &&
                Objects.equals(logicalDeviceId, that.logicalDeviceId) &&
                Objects.equals(serviceVersion, that.serviceVersion) &&
                Objects.equals(apiVersion, that.apiVersion) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(supportedFlowTypes, that.supportedFlowTypes) &&
                Objects.equals(customRequestTypes, that.customRequestTypes) &&
                Objects.equals(supportedDataKeys, that.supportedDataKeys) &&
                Objects.equals(additionalInfo, that.additionalInfo) &&
                Objects.equals(stages, that.stages) &&
                Objects.equals(flowAndStagesDefinitions, that.flowAndStagesDefinitions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), packageName, vendor, logicalDeviceId, serviceVersion, apiVersion, displayName, hasAccessibilityMode,
                            supportedFlowTypes, customRequestTypes, supportedDataKeys, additionalInfo, stages, flowAndStagesDefinitions);
    }
}
