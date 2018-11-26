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
 * Defined set of error constants that may be passed back to the client.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/handling-errors" target="_blank">Handling Errors</a>
 */
public interface ErrorConstants {

    /**
     * A general purpose error that will be sent for fatal failures in a flow service
     */
    String FLOW_SERVICE_ERROR = "flowServiceError";

    /**
     * Sent if the processing service if not installed
     */
    String PROCESSING_SERVICE_NOT_INSTALLED = "notInstalled";

    /**
     * Sent if the processing service rejects a request because it is busy processing another
     */
    String PROCESSING_SERVICE_BUSY = "busy";

    /**
     * Sent if the processing service fails to cancel a flow
     */
    String PROCESSING_SERVICE_CANCEL_FAILED = "cancelFailed";

    /**
     * Sent if the processing service fails to resume a flow
     */
    String PROCESSING_SERVICE_RESUME_FAILED = "resumeFailed";

    /**
     * Sent if an unknown/unsupported request type is sent to the processing service
     */
    String UNSUPPORTED_OPERATION = "unsupportedOperation";

    /**
     * Sent if the processing service has received an unexpected message type
     */
    String INVALID_MESSAGE_TYPE = "invalidMessageType";

    /**
     * The processing service has received an invalid or unreadable message. This usually indicates a message has been corrupted.
     */
    String INVALID_REQUEST = "invalidRequest";

    /**
     * Sent if the client application is missing a response listener service implementation
     */
    String MISSING_RESPONSE_LISTENER = "missingResponseListener";

    /**
     * Sent if a flow service does not support the stage it has just been called for
     */
    String STAGE_NOT_SUPPORTED = "stageNotSupported";

    /**
     * Sent if the processing service cannot find a handler(s) for a stage
     */
    String CONFIG_ERROR = "configError";

    /**
     * Sent if the processing service detects there is more than one config provider installed that provides flow configs
     */
    String MULTIPLE_CONFIG_PROVIDERS = "multipleConfigProviders";

    /**
     * Sent if the processing service detects there is no config provider installed that provides flow configs
     */
    String NO_CONFIG_PROVIDER = "noConfigProvider";

    /**
     * Sent if the client is using an API that is not the same major version as the one implemented by the processing service
     */
    String INCOMPATIBLE_API_VERSION = "incompatibleApiVersion";

    /**
     * Something unexpected happened
     */
    String UNEXPECTED_ERROR = "unexpectedError";
}
