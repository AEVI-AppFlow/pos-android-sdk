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

public interface ErrorConstants {
    String PROCESSING_SERVICE_NOT_INSTALLED = "notInstalled";
    String PROCESSING_SERVICE_BUSY = "busy";
    String PROCESSING_SERVICE_CANCEL_FAILED = "cancelFailed";
    String PROCESSING_SERVICE_RESUME_FAILED = "resumeFailed";
    String UNSUPPORTED_OPERATION = "unsupportedOperation";
    String INVALID_MESSAGE_TYPE = "invalidMessageType";
    String INVALID_REQUEST = "invalidRequest";
    String FLOW_SERVICE_ERROR = "flowServiceError";
    String FLOW_SERVICE_ACTIVITY_ERROR = "flowServiceActivityError";
    String STAGE_NOT_SUPPORTED = "stageNotSupported";
    String CONFIG_ERROR = "configError";
    String INCOMPATIBLE_API_VERSION = "incompatibleApiVersion";
    String UNEXPECTED_ERROR = "unexpectedError";
}
