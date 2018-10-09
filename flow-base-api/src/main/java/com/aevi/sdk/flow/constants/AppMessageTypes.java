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

    String REQUEST_MESSAGE = "request";
    String PAYMENT_MESSAGE = "payment";
    String RESPONSE_MESSAGE = "response";
    String FLOW_SERVICE_INFO_REQUEST = "flowServiceInfoRequest";
    String PAYMENT_FLOW_CONFIG_REQUEST = "paymentFlowConfigRequest";
    String DEVICE_INFO_REQUEST = "deviceInfoRequest";
    String FAILURE_MESSAGE = "failure";
    String FORCE_FINISH_MESSAGE = "forceFinish";
    String REQUEST_ACK_MESSAGE = "requestAck";
}
