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

public interface EventDataKeys {

    /**
     * For {@link EventTypes#EVENT_EXTERNAL_STATE_CHANGED} event.
     *
     * Flow services have changed, meaning apps should re-query for available flow services.
     */
    String EVENT_KEY_FLOW_SERVICES_CHANGED = "eventKeyFlowServicesChanged";
    /**
     * For {@link EventTypes#EVENT_EXTERNAL_STATE_CHANGED} event.
     *
     * Payment services have changed, meaning apps should re-query for available flow services.
     */
    String EVENT_KEY_PAYMENT_SERVICES_CHANGED = "eventKeyPaymentServicesChanged";
    /**
     * For {@link EventTypes#EVENT_EXTERNAL_STATE_CHANGED} event.
     *
     * Devices have changed, meaning apps should re-query for available devices.
     */
    String EVENT_KEY_DEVICES_CHANGED = "eventKeyDevicesChanged";

    /**
     * For {@link EventTypes#EVENT_INTERNAL_STATE_CHANGED} event.
     *
     * FPS settings have changed.
     */
    String EVENT_KEY_SETTINGS_CHANGED = "eventKeySettingsChanged";

    /**
     * For {@link EventTypes#EVENT_INTERNAL_STATE_CHANGED} event.
     *
     * Flow configs have been updated.
     */
    String EVENT_KEY_FLOW_CONFIGS_CHANGED = "eventKeyFlowConfigsChanged";

}
