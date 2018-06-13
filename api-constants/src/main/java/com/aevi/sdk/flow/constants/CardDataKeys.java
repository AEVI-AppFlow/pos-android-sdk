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


public interface CardDataKeys {

    String CARD_ENTRY_METHODS = "cardEntryMethods"; // insert, tap, swipe, manual
    String CARD_NETWORKS = "cardNetworks"; // VISA, AMEX, MAESTRO, etc
    String ACCOUNT_TYPES = "accountTypes"; // credit, debit, etc

    String NETWORK = "network";
    String ENTRY_METHOD = "entryMethod";
    String ACCOUNT_TYPE = "accountType";
    String AID = "aid";
    String SERVICE_CODE = "serviceCode";
    String CVV = "cvv";
    String LANGUAGES = "languages";

    String CURRENCY_CHANGE_ALLOWED = "currencyChangeAllowed";
    String AMOUNT_CHANGE_ALLOWED = "amountChangeAllowed";
}
