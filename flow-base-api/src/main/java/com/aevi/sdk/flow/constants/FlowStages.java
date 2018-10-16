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

public interface FlowStages {

    /**
     * First stage of a payment transaction, after the Request has been received but before the transaction flow is started.
     */
    String PRE_FLOW = "PRE_FLOW";

    /**
     * In the transaction flow, before any other flow applications are executed. Split apps are essentially PRE_TRANSACTION apps with special
     * privilege to execute before any other apps in the flow, and are allowed to cancel transactions.
     */
    String SPLIT = "SPLIT";

    /**
     * In the transaction flow, after any split modifications but before any card has been read or any transaction has been processed.
     */
    String PRE_TRANSACTION = "PRE_TRANSACTION";

    /**
     * In the transaction flow, and a payment service is in the process of detecting and reading a payment card from the customer.
     *
     * This step is optional for a payment service to support and hence may never be executed.
     */
    String PAYMENT_CARD_READING = "PAYMENT_CARD_READING";

    /**
     * In the transaction flow, and a payment card has been read by the payment app. At this stage, card data may be available.
     *
     * Note that this if the optional PAYMENT_CARD_READING step was not executed, there won't be any card data available.
     */
    String POST_CARD_READING = "POST_CARD_READING";

    /**
     * The payment service is now processing the transaction, which typically involves communication with a bank/acquirer host.
     *
     * After this step, the outcome of this transaction will be known and available via a TransactionResponse.
     */
    String TRANSACTION_PROCESSING = "TRANSACTION_PROCESSING";

    /**
     * In the transaction flow, after the transaction has been processed and one or more TransactionResponses available.
     */
    String POST_TRANSACTION = "POST_TRANSACTION";

    /**
     * After all transactions have been processed and we now have a final Response.
     */
    String POST_FLOW = "POST_FLOW";

    /**
     * Generic stage that is called with a simple request with bespoke data.
     */
    String GENERIC = "GENERIC";

    /**
     * All stages for a payment flow
     */
    String[] ALL_PAYMENT_STAGES = new String[]{PRE_FLOW, SPLIT, PRE_TRANSACTION, PAYMENT_CARD_READING, POST_CARD_READING,
            TRANSACTION_PROCESSING, POST_TRANSACTION, POST_FLOW};

    /**
     * All defined stages
     */
    String[] ALL_STAGES = new String[]{GENERIC, PRE_FLOW, SPLIT, PRE_TRANSACTION, PAYMENT_CARD_READING, POST_CARD_READING,
            TRANSACTION_PROCESSING, POST_TRANSACTION, POST_FLOW};
}
