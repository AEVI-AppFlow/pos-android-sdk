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

/**
 * Represents FPS (Flow Processing Service) settings.
 */
public class FpsSettings implements Jsonable {

    private boolean isMultiDevice = false;
    private boolean isCurrencyChangeAllowed = false;

    private int splitResponseTimeoutSeconds = 1200;
    private int flowResponseTimeoutSeconds = 120;
    private int paymentResponseTimeoutSeconds = 120;
    private int statusTimeoutSeconds = 20;
    private int appOrDeviceSelectionTimeoutSeconds = 60;

    private boolean shouldAbortOnFlowAppError = false;
    private boolean shouldAbortOnCancelled = true;
    private boolean shouldAbortOnPaymentError = false;
    private boolean allowAccessViaStatusBar = false;

    public boolean isMultiDevice() {
        return isMultiDevice;
    }

    public boolean isCurrencyChangeAllowed() {
        return isCurrencyChangeAllowed;
    }

    public void setIsMultiDevice(boolean isMultiDevice) {
        this.isMultiDevice = isMultiDevice;
    }

    public void setCurrencyChangeAllowed(boolean currencyChangeAllowed) {
        isCurrencyChangeAllowed = currencyChangeAllowed;
    }

    public int getSplitResponseTimeoutSeconds() {
        return splitResponseTimeoutSeconds;
    }

    public int getFlowResponseTimeoutSeconds() {
        return flowResponseTimeoutSeconds;
    }

    public int getPaymentResponseTimeoutSeconds() {
        return paymentResponseTimeoutSeconds;
    }

    public int getStatusTimeoutSeconds() {
        return statusTimeoutSeconds;
    }

    public int getAppOrDeviceSelectionTimeoutSeconds() {
        return appOrDeviceSelectionTimeoutSeconds;
    }

    public boolean shouldAbortOnFlowAppError() {
        return shouldAbortOnFlowAppError;
    }

    public boolean shouldAbortOnPaymentAppError() {
        return shouldAbortOnPaymentError;
    }

    public boolean allowAccessViaStatusBar() {
        return allowAccessViaStatusBar;
    }

    public void setAllowAccessViaStatusBar(boolean allowAccessViaStatusBar) {
        this.allowAccessViaStatusBar = allowAccessViaStatusBar;
    }

    public boolean shouldAbortFlowOnCancelled() {
        return shouldAbortOnCancelled;
    }

    public void setSplitResponseTimeoutSeconds(int splitTimeout) {
        splitResponseTimeoutSeconds = splitTimeout;
    }

    public void setFlowResponseTimeoutSeconds(int responseTimeout) {
        flowResponseTimeoutSeconds = responseTimeout;
    }

    public void setPaymentResponseTimeoutSeconds(int responseTimeout) {
        paymentResponseTimeoutSeconds = responseTimeout;
    }

    public void setStatusTimeoutSeconds(int statusTimeout) {
        statusTimeoutSeconds = statusTimeout;
    }

    public void setAppOrDeviceSelectionTimeoutSeconds(Integer selectTimeout) {
        appOrDeviceSelectionTimeoutSeconds = selectTimeout;
    }

    public void abortOnPaymentError(boolean abort) {
        shouldAbortOnPaymentError = abort;
    }

    public void abortOnFlowError(boolean abort) {
        shouldAbortOnFlowAppError = abort;
    }

    public void abortOnFlowOnCancelled(boolean abort) {
        shouldAbortOnCancelled = abort;
    }

    public static FpsSettings fromJson(String json) {
        return JsonConverter.deserialize(json, FpsSettings.class);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }
}