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

public interface IntentActions {
    String BASE_INTENT_ACTION = "com.aevi.sdk.flow.action.";

    String GENERIC_REQUEST_ACTION = BASE_INTENT_ACTION + "PROCESS_REQUEST";
    String GENERIC_RESPONSE_ACTION = BASE_INTENT_ACTION + "PROCESS_RESPONSE";

    String PAYMENT_CARD_READING_STAGE_ACTION = BASE_INTENT_ACTION + "READ_PAYMENT_CARD";
    String TRANSACTION_PROCESSING_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_TRANSACTION";
    String PRE_FLOW_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_PRE_FLOW";
    String SPLIT_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_SPLIT";
    String PRE_TRANSACTION_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_PRE_TRANSACTION";
    String POST_CARD_READING_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_POST_CARD_READING";
    String POST_TRANSACTION_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_POST_TRANSACTION";
    String POST_FLOW_STAGE_ACTION = BASE_INTENT_ACTION + "PROCESS_POST_FLOW";
    String PAYMENT_RESPONSE_ACTION = BASE_INTENT_ACTION + "PROCESS_PAYMENT_RESPONSE";

    String SERVICE_INFO_PROVIDER_ACTION = BASE_INTENT_ACTION + "PROVIDE_SERVICE_INFO";
    String SERVICE_INFO_CHANGE_ACTION = BASE_INTENT_ACTION + "PROCESS_SERVICE_INFO_CHANGE";

    String ACTIVITY_PROXY_ACTION_PREFIX = "PROCESS_";
    String ACTIVITY_PROXY_ACTION_POSTFIX = "_IN_ACTIVITY";

}