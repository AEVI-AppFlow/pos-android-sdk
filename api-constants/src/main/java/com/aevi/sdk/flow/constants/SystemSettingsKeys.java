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

package com.aevi.sdk.flow.constants;

public interface SystemSettingsKeys {

    String SYSTEM_SETTINGS_KEY_MULTI_DEVICE_ENABLED = "systemSettingsKeyMultiDeviceEnabled";
    String SYSTEM_SETTINGS_KEY_SPLIT_ENABLED = "systemSettingsKeySplitEnabled";
    String SYSTEM_SETTINGS_KEY_CURRENCY_CHANGE_ENABLED = "systemSettingsKeyCurrencyChangeEnabled";
    String SYSTEM_SETTINGS_KEY_SPLIT_RESPONSE_TIMEOUT_SECONDS = "systemSettingsKeySplitRespTimeout";
    String SYSTEM_SETTINGS_KEY_FLOW_SERVICE_RESPONSE_TIMEOUT_SECONDS = "systemSettingsKeyFlowServiceRespTimeout";
    String SYSTEM_SETTINGS_KEY_PAYMENT_SERVICE_RESPONSE_TIMEOUT_SECONDS = "systemSettingsKeyPaymentServiceRespTimeout";
    String SYSTEM_SETTINGS_KEY_MERCHANT_SELECTION_TIMEOUT_SECONDS = "systemSettingsKeyMerchantSelectionTimeout";
    String SYSTEM_SETTINGS_KEY_ABORTS_ON_FLOW_SERVICE_ERROR = "systemSettingsKeyAbortOnFlowServiceError";
    String SYSTEM_SETTINGS_KEY_ABORTS_ON_PAYMENT_SERVICE_ERROR = "systemSettingsKeyAbortOnPaymentServiceError";

    String SYSTEM_SETTINGS_KEY_FLOW_CONFIGS = "systemSettingsKeyFlowConfigs";
    String SYSTEM_SETTINGS_KEY_FLOW_CONFIG_JSON = "systemSettingsKeyFlowConfigJson";
}
