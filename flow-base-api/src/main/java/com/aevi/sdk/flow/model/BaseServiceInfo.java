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

import java.util.Arrays;
import java.util.Objects;

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
    private final String[] supportedRequestTypes;
    private final String[] supportedDataKeys;
    private final AdditionalData additionalInfo;
    private boolean enabled;
    private String[] stages;

    // Default constructor for deserialisation
    protected BaseServiceInfo() {
        this("", "", "", "", "", "", "", false,
                null, null, null);
    }

    protected BaseServiceInfo(String id, String packageName, String vendor, String logicalDeviceId, String serviceVersion, String apiVersion,
                              String displayName, boolean hasAccessibilityMode, String[] supportedRequestTypes,
                              String[] supportedDataKeys, AdditionalData additionalInfo) {
        super(id);
        this.packageName = packageName;
        this.vendor = vendor;
        this.logicalDeviceId = logicalDeviceId;
        this.serviceVersion = serviceVersion;
        this.apiVersion = apiVersion;
        this.displayName = displayName;
        this.hasAccessibilityMode = hasAccessibilityMode;
        this.supportedRequestTypes = supportedRequestTypes != null ? supportedRequestTypes : new String[0];
        this.supportedDataKeys = supportedDataKeys != null ? supportedDataKeys : new String[0];
        this.additionalInfo = additionalInfo != null ? additionalInfo : new AdditionalData();
        this.enabled = true;
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
     * Gets an array of the supported request types, indicating what type of requests it can handle.
     *
     * See reference values in the documentation for possible values.
     *
     * @return array of request types supported by the service
     */
    @NonNull
    public String[] getSupportedRequestTypes() {
        return supportedRequestTypes;
    }

    /**
     * Check whether this service supports the given request type.
     *
     * @param requestType The request type to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsRequestType(String requestType) {
        return supportedRequestTypes.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedRequestTypes, requestType);
    }

    /**
     * Get the stages this service operates in.
     *
     * For POS flow, the possible stages are defined in the PaymentStage model and PaymentStage.valueOf(xxx) can be used to map these values.
     *
     * @return The set of stages the service operates in.
     */
    public String[] getStages() {
        return stages;
    }

    /**
     * Check whether the flow services operates in the given stage.
     *
     * @param stage The stage to check against
     * @return True if the flow service operates in the given stage, false otherwise
     */
    public boolean containsStage(String stage) {
        return stages.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(stages, stage);
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
    public String[] getSupportedDataKeys() {
        return supportedDataKeys;
    }

    /**
     * Check whether this service supports the given data key (for {@link AdditionalData usage}.
     *
     * @param dataKey The data key to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsDataKey(String dataKey) {
        return supportedDataKeys.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedDataKeys, dataKey);
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
     * Check whether this service is enabled or not.
     *
     * Disabled services are never eligible to be called.
     *
     * @return True if enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * For internal use.
     *
     * @param enabled Enabled/disabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    public void setStages(String[] stages) {
        this.stages = stages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseServiceInfo that = (BaseServiceInfo) o;
        return hasAccessibilityMode == that.hasAccessibilityMode &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(vendor, that.vendor) &&
                Objects.equals(logicalDeviceId, that.logicalDeviceId) &&
                Objects.equals(serviceVersion, that.serviceVersion) &&
                Objects.equals(apiVersion, that.apiVersion) &&
                Objects.equals(displayName, that.displayName) &&
                Arrays.equals(supportedRequestTypes, that.supportedRequestTypes) &&
                Arrays.equals(supportedDataKeys, that.supportedDataKeys) &&
                Objects.equals(additionalInfo, that.additionalInfo) &&
                Arrays.equals(stages, that.stages);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), packageName, vendor, logicalDeviceId, serviceVersion, apiVersion, displayName, hasAccessibilityMode, additionalInfo);
        result = 31 * result + Arrays.hashCode(supportedRequestTypes);
        result = 31 * result + Arrays.hashCode(supportedDataKeys);
        result = 31 * result + Arrays.hashCode(stages);
        return result;
    }
}
