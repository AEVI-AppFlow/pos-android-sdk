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

/**
 * Collection of message types used for communication between FPS and various apps.
 */
public interface AppMessageTypes {

    // Initiated by client apps
    String REQUEST_MESSAGE = "request";
    String PAYMENT_MESSAGE = "payment";
    String PAYMENT_FLOW_CONFIG_REQUEST = "paymentFlowConfigRequest";
    String DEVICE_INFO_REQUEST = "deviceInfoRequest";

    // Sent from flow services
    String RESPONSE_MESSAGE = "response";
    String AUDIT_ENTRY = "auditEntry";
    String FAILURE_MESSAGE = "failure";
    String REQUEST_ACK_MESSAGE = "requestAck";

    // Sent by FPS
    String FORCE_FINISH_MESSAGE = "forceFinish";
    String RESPONSE_OUTCOME = "responseOutcome";
    String RESTART_UI = "restartUserInterface";
}
