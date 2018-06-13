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

// General data keys
public interface AdditionalDataKeys {

    String DATA_KEY_BASKET = "basket";
    String DATA_KEY_CUSTOMER = "customer";
    String DATA_KEY_TOKEN = "token";

    String DATA_KEY_TRANSACTION_ID = "transactionId";
    String DATA_KEY_TRANSACTION_RESPONSE = "transactionResponse";

    String DATA_KEY_TRANSACTION_LANGUAGE = "transactionLanguage";

    String DATA_KEY_AMOUNT = "amount";
    String DATA_KEY_MERCHANT_ID = "merchantId";
    String DATA_KEY_ACCESSIBLE_MODE = "accessibleMode";

    String DATA_KEY_PREV_TXN_REFERENCES = "prevTxnReferences"; // Transaction references returned via the response of a previous txn
    String DATA_KEY_PREV_TXN_REQUEST_ID = "prevTxnRequestId"; // The Request id of a previous txn, useful for reversals/completions

    String LEGACY_POST_AUTH_TIPPING = "postAuthTipping";
    String LEGACY_PRINT_DATA = "legacyPrintData";
}
