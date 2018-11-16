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

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Objects;

/**
 * Represents FPS (Flow Processing Service) settings that can be configured via a configuration provider.
 */
public class FpsSettings implements Jsonable {

    public static final boolean MULTI_DEVICE_ENABLED_DEFAULT = false;
    public static final boolean CURRENCY_CHANGE_ALLOWED_DEFAULT = false;
    public static final boolean ALLOW_ACCESS_STATUS_BAR_DEFAULT = false;
    public static final boolean ALWAYS_ALLOW_DYNAMIC_SELECT_DEFAULT = false;
    public static final boolean ABORT_ON_FLOW_APP_ERROR_DEFAULT = false;
    public static final boolean ABORT_ON_PAYMENT_APP_ERROR_DEFAULT = false;
    public static final boolean FILTER_FLOW_SERVICES_BY_FLOW_TYPE_DEFAULT = true;
    public static final boolean LEGACY_PAYMENT_APPS_ENABLED_DEFAULT = false;

    public static final int SPLIT_RESPONSE_TIMEOUT_SECONDS_DEFAULT = 1200;
    public static final int FLOW_RESPONSE_TIMEOUT_SECONDS_DEFAULT = 120;
    public static final int PAYMENT_RESPONSE_TIMEOUT_SECONDS_DEFAULT = 120;
    public static final int STATUS_UPDATE_TIMEOUT_SECONDS_DEFAULT = 10;
    public static final int USER_SELECTION_TIMEOUT_SECONDS_DEFAULT = 60;

    private boolean isMultiDevice = MULTI_DEVICE_ENABLED_DEFAULT;
    private boolean isCurrencyChangeAllowed = CURRENCY_CHANGE_ALLOWED_DEFAULT;

    private int splitResponseTimeoutSeconds = SPLIT_RESPONSE_TIMEOUT_SECONDS_DEFAULT;
    private int flowResponseTimeoutSeconds = FLOW_RESPONSE_TIMEOUT_SECONDS_DEFAULT;
    private int paymentResponseTimeoutSeconds = PAYMENT_RESPONSE_TIMEOUT_SECONDS_DEFAULT;
    private int statusUpdateTimeoutSeconds = STATUS_UPDATE_TIMEOUT_SECONDS_DEFAULT;
    private int appOrDeviceSelectionTimeoutSeconds = USER_SELECTION_TIMEOUT_SECONDS_DEFAULT;

    private boolean shouldAbortOnFlowAppError = ABORT_ON_FLOW_APP_ERROR_DEFAULT;
    private boolean shouldAbortOnPaymentError = ABORT_ON_PAYMENT_APP_ERROR_DEFAULT;

    private boolean allowAccessViaStatusBar = ALLOW_ACCESS_STATUS_BAR_DEFAULT;
    private boolean alwaysAllowDynamicSelect = ALWAYS_ALLOW_DYNAMIC_SELECT_DEFAULT;

    private boolean filterServicesByFlowType = FILTER_FLOW_SERVICES_BY_FLOW_TYPE_DEFAULT;

    private boolean legacyPaymentAppsEnabled = LEGACY_PAYMENT_APPS_ENABLED_DEFAULT;

    /**
     * Check whether multi-device support is enabled.
     *
     * This flag indicates whether FPS allows additional devices to connect to it and execute apps as part of the flow.
     *
     * See {@link #MULTI_DEVICE_ENABLED_DEFAULT} for default.
     *
     * @return True if multi-device is enabled, false otherwise
     */
    public boolean isMultiDeviceEnabled() {
        return isMultiDevice;
    }

    /**
     * Set whether multi-device support is enabled.
     *
     * This flag indicates whether FPS allows additional devices to connect to it and execute apps as part of the flow.
     *
     * See {@link #MULTI_DEVICE_ENABLED_DEFAULT} for default.
     *
     * @param isMultiDevice True if multi-device is enabled, false otherwise
     */
    public void setMultiDeviceEnabled(boolean isMultiDevice) {
        this.isMultiDevice = isMultiDevice;
    }

    /**
     * Check whether currency changes (aka dynamic currency conversion) is allowed in the flow.
     *
     * If allowed, this enables flow services to change the currency before transaction processing.
     *
     * See {@link #CURRENCY_CHANGE_ALLOWED_DEFAULT} for default.
     *
     * @return True if currency change is allowed, false otherwise
     */
    public boolean isCurrencyChangeAllowed() {
        return isCurrencyChangeAllowed;
    }

    /**
     * Set whether currency changes (aka dynamic currency conversion) is allowed in the flow.
     *
     * If allowed, this enables flow services to change the currency before transaction processing.
     *
     * See {@link #CURRENCY_CHANGE_ALLOWED_DEFAULT} for default.
     *
     * @param currencyChangeAllowed True if currency change is allowed, false otherwise
     */
    public void setCurrencyChangeAllowed(boolean currencyChangeAllowed) {
        isCurrencyChangeAllowed = currencyChangeAllowed;
    }

    /**
     * Get the split service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the split stage.
     *
     * See {@link #SPLIT_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @return The split response timeout in seconds
     */
    public int getSplitResponseTimeoutSeconds() {
        return splitResponseTimeoutSeconds;
    }

    /**
     * Set the split service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the split stage.
     *
     * See {@link #SPLIT_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @param splitTimeout The split response timeout in seconds
     */
    public void setSplitResponseTimeoutSeconds(int splitTimeout) {
        splitResponseTimeoutSeconds = splitTimeout;
    }

    /**
     * Get the general flow service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the general flow stages.
     *
     * This applies to all stages besides split and transaction processing which have their own timeouts.
     *
     * See {@link #FLOW_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @return The flow service response timeout in seconds
     */
    public int getFlowResponseTimeoutSeconds() {
        return flowResponseTimeoutSeconds;
    }

    /**
     * Set the general flow service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the general flow stages.
     *
     * This applies to all stages besides split and transaction processing which have their own timeouts.
     *
     * See {@link #FLOW_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @param responseTimeout The flow service response timeout in seconds
     */
    public void setFlowResponseTimeoutSeconds(int responseTimeout) {
        flowResponseTimeoutSeconds = responseTimeout;
    }

    /**
     * Get the transaction processing service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the transaction processing stage.
     *
     * See {@link #PAYMENT_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @return The transaction processing service response timeout in seconds
     */
    public int getPaymentResponseTimeoutSeconds() {
        return paymentResponseTimeoutSeconds;
    }

    /**
     * Set the transaction processing service response timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in the transaction processing stage.
     *
     * See {@link #PAYMENT_RESPONSE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @param responseTimeout The transaction processing service response timeout in seconds
     */
    public void setPaymentResponseTimeoutSeconds(int responseTimeout) {
        paymentResponseTimeoutSeconds = responseTimeout;
    }

    /**
     * Get the status update timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out for status updates.
     *
     * See {@link #STATUS_UPDATE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @return The status update timeout in seconds
     */
    public int getStatusUpdateTimeoutSeconds() {
        return statusUpdateTimeoutSeconds;
    }

    /**
     * Set the status update timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out for status updates.
     *
     * See {@link #STATUS_UPDATE_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @param statusUpdateTimeoutSeconds The status update timeout in seconds
     */
    public void setStatusUpdateTimeoutSeconds(int statusUpdateTimeoutSeconds) {
        this.statusUpdateTimeoutSeconds = statusUpdateTimeoutSeconds;
    }

    /**
     * Get the user selection timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in cases where the user is asked to make a selection / choice.
     *
     * See {@link #USER_SELECTION_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @return The user selection timeout in seconds
     */
    public int getUserSelectionTimeoutSeconds() {
        return appOrDeviceSelectionTimeoutSeconds;
    }

    /**
     * Set the user selection timeout in seconds.
     *
     * This instructs FPS how long to wait before timing out in cases where the user is asked to make a selection / choice.
     *
     * See {@link #USER_SELECTION_TIMEOUT_SECONDS_DEFAULT} for default.
     *
     * @param selectTimeout The user selection timeout in seconds
     */
    public void setUserSelectionTimeoutSeconds(int selectTimeout) {
        appOrDeviceSelectionTimeoutSeconds = selectTimeout;
    }

    /**
     * Check whether FPS should abort the flow on unexpected errors whilst executing a flow service.
     *
     * If true, FPS will cancel the flow. If false, the flow will continue and the outcome of the flow service is ignored.
     *
     * See {@link #ABORT_ON_FLOW_APP_ERROR_DEFAULT} for default.
     *
     * @return True if FPS should abort flow on flow service errors, false otherwise
     */
    public boolean shouldAbortOnFlowAppError() {
        return shouldAbortOnFlowAppError;
    }

    /**
     * Set whether FPS should abort the flow on unexpected errors whilst executing a flow service.
     *
     * If true, FPS will cancel the flow. If false, the flow will continue and the outcome of the flow service is ignored.
     *
     * See {@link #ABORT_ON_FLOW_APP_ERROR_DEFAULT} for default.
     *
     * @param abort True if FPS should abort flow on flow service errors, false otherwise
     */
    public void setAbortOnFlowError(boolean abort) {
        shouldAbortOnFlowAppError = abort;
    }

    /**
     * Set whether FPS should abort the flow on unexpected errors whilst executing a flow service.
     *
     * If true, FPS will cancel the flow. If false, the flow will continue and the outcome of the flow service is ignored.
     *
     * See {@link #ABORT_ON_PAYMENT_APP_ERROR_DEFAULT} for default.
     *
     * @param abort True if FPS should abort flow on flow service errors, false otherwise
     */
    public void setAbortOnPaymentError(boolean abort) {
        shouldAbortOnPaymentError = abort;
    }

    /**
     * Check whether FPS should abort the flow on unexpected errors whilst executing a flow service.
     *
     * If true, FPS will cancel the flow. If false, the flow will continue and the outcome of the flow service is ignored.
     *
     * See {@link #ABORT_ON_PAYMENT_APP_ERROR_DEFAULT} for default.
     *
     * @return True if FPS should abort flow on flow service errors, false otherwise
     */
    public boolean shouldAbortOnPaymentAppError() {
        return shouldAbortOnPaymentError;
    }

    /**
     * Check whether FPS is allowed to expose the flow notification controls in the status bar or not.
     *
     * See {@link #ALLOW_ACCESS_STATUS_BAR_DEFAULT} for default.
     *
     * @return True if allowed, false otherwise
     */
    public boolean isAccessViaStatusBarAllowed() {
        return allowAccessViaStatusBar;
    }

    /**
     * Set whether FPS is allowed to expose the flow notification controls in the status bar or not.
     *
     * See {@link #ALLOW_ACCESS_STATUS_BAR_DEFAULT} for default.
     *
     * @param allowAccessViaStatusBar True if allowed, false otherwise
     */
    public void setAllowAccessViaStatusBar(boolean allowAccessViaStatusBar) {
        this.allowAccessViaStatusBar = allowAccessViaStatusBar;
    }

    /**
     * Check whether FPS should always allow {@link AppExecutionType#DYNAMIC_SELECT} regardless of other conditions and criteria.
     *
     * See {@link #ALWAYS_ALLOW_DYNAMIC_SELECT_DEFAULT} for default.
     *
     * @return True if FPS always allows it, false otherwise
     */
    public boolean shouldAlwaysAllowDynamicSelect() {
        return alwaysAllowDynamicSelect;
    }

    /**
     * Set whether FPS should always allow {@link AppExecutionType#DYNAMIC_SELECT} regardless of other conditions and criteria.
     *
     * See {@link #ALWAYS_ALLOW_DYNAMIC_SELECT_DEFAULT} for default.
     *
     * @param alwaysAllowDynamicSelect True if FPS always allows it, false otherwise
     */
    public void setAlwaysAllowDynamicSelect(boolean alwaysAllowDynamicSelect) {
        this.alwaysAllowDynamicSelect = alwaysAllowDynamicSelect;
    }

    /**
     * Check whether or not FPS should filter flow services based on their reported type and the type of the flow.
     *
     * If true, services that do not explicitly report a type as supported will not be called for flows with that type.
     *
     * If false, FPS will always call flow services even if they don't report types, or the flow type is not supported by that service.
     *
     * @return True to enable FPS filtering, false otherwise
     */
    public boolean shouldFilterServicesByFlowType() {
        return filterServicesByFlowType;
    }

    /**
     * Set whether or not FPS should filter flow services based on their reported type and the type of the flow.
     *
     * If true, services that do not explicitly report a type as supported will not be called for flows with that type.
     *
     * If false, FPS will always call flow services even if they don't report types, or the flow type is not supported by that service.
     *
     * @param filterServicesByFlowType True to enable FPS filtering, false otherwise
     */
    public void setFilterServicesByFlowType(boolean filterServicesByFlowType) {
        this.filterServicesByFlowType = filterServicesByFlowType;
    }

    /**
     * Check to set if FPS should scan and support legacy payment applications.
     *
     * If true, legacy AEVI Public SDK implementations will be reported as a flow service application that can be used in a payment flow to
     * process supported legacy transaction types.
     *
     * If false, legacy payment applications will be ignored.
     *
     * @return True if legacy payment applications are supported
     */
    public boolean legacyPaymentAppsEnabled() { return legacyPaymentAppsEnabled; }

    /**
     * Set whether or not FPS should scan and support legacy payment applications.
     *
     * If true, legacy AEVI Public SDK implementations will be reported as a flow service application that can be used in a payment flow to
     * process supported legacy transaction types.
     *
     * If false, legacy payment applications will be ignored.
     *
     * @param legacyPaymentAppsEnabled True to enable legacy payment application support
     */
    public void setLegacyPaymentAppsEnabled(boolean legacyPaymentAppsEnabled) {
        this.legacyPaymentAppsEnabled = legacyPaymentAppsEnabled;
    }

    public static FpsSettings fromJson(String json) {
        return JsonConverter.deserialize(json, FpsSettings.class);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FpsSettings that = (FpsSettings) o;
        return isMultiDevice == that.isMultiDevice &&
                isCurrencyChangeAllowed == that.isCurrencyChangeAllowed &&
                splitResponseTimeoutSeconds == that.splitResponseTimeoutSeconds &&
                flowResponseTimeoutSeconds == that.flowResponseTimeoutSeconds &&
                paymentResponseTimeoutSeconds == that.paymentResponseTimeoutSeconds &&
                appOrDeviceSelectionTimeoutSeconds == that.appOrDeviceSelectionTimeoutSeconds &&
                shouldAbortOnFlowAppError == that.shouldAbortOnFlowAppError &&
                shouldAbortOnPaymentError == that.shouldAbortOnPaymentError &&
                allowAccessViaStatusBar == that.allowAccessViaStatusBar &&
                alwaysAllowDynamicSelect == that.alwaysAllowDynamicSelect &&
                filterServicesByFlowType == that.filterServicesByFlowType;
    }

    @Override
    public int hashCode() {

        return Objects
                .hash(isMultiDevice, isCurrencyChangeAllowed, splitResponseTimeoutSeconds, flowResponseTimeoutSeconds, paymentResponseTimeoutSeconds,
                      appOrDeviceSelectionTimeoutSeconds, shouldAbortOnFlowAppError, shouldAbortOnPaymentError, allowAccessViaStatusBar,
                      alwaysAllowDynamicSelect, filterServicesByFlowType);
    }
}