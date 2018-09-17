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

package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.util.ComparisonUtil;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Exposes payment and flow services information with helper methods to retrieve collated data across all services.
 */
public class PaymentFlowServices implements Jsonable {

    private final List<PaymentFlowServiceInfo> paymentFlowServiceInfoList;
    private final Set<String> supportedRequestTypes;
    private final Set<String> supportedTransactionTypes;
    private final Set<String> supportedCurrencies;
    private final Set<String> supportedPaymentMethods;
    private final Set<String> supportedDataKeys;

    public PaymentFlowServices(List<PaymentFlowServiceInfo> paymentFlowServiceInfoList) {
        this.paymentFlowServiceInfoList = Collections.unmodifiableList(paymentFlowServiceInfoList);
        supportedRequestTypes = new HashSet<>();
        supportedTransactionTypes = new HashSet<>();
        supportedCurrencies = new HashSet<>();
        supportedPaymentMethods = new HashSet<>();
        supportedDataKeys = new HashSet<>();
        for (PaymentFlowServiceInfo paymentFlowServiceInfo : paymentFlowServiceInfoList) {
            supportedRequestTypes.addAll(paymentFlowServiceInfo.getSupportedRequestTypes());
            supportedTransactionTypes.addAll(paymentFlowServiceInfo.getSupportedTransactionTypes());
            supportedCurrencies.addAll(paymentFlowServiceInfo.getSupportedCurrencies());
            supportedPaymentMethods.addAll(paymentFlowServiceInfo.getPaymentMethods());
            supportedDataKeys.addAll(paymentFlowServiceInfo.getSupportedDataKeys());
        }
    }

    /**
     * Get the number of payment flow services.
     *
     * @return The number of payment flow services
     */
    public int getNumberOfFlowServices() {
        return paymentFlowServiceInfoList.size();
    }

    /**
     * Get the list of flow services this object was instantiated with.
     *
     * @return The list of flow services
     */
    public List<PaymentFlowServiceInfo> getFlowServices() {
        return paymentFlowServiceInfoList;
    }

    /**
     * Check whether a particular request type is supported by at least one of the services.
     *
     * @param requestType The request type to check if supported
     * @return True if at least one service support it, false otherwise
     */
    public boolean isRequestTypeSupported(String requestType) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(supportedRequestTypes, requestType);
    }

    /**
     * Retrieve a consolidated set of all the supported request types across all the services.
     *
     * @return A consolidated set of all the supported request types across all the services
     */
    public Set<String> getAllSupportedRequestTypes() {
        return supportedRequestTypes;
    }

    /**
     * Check whether a particular transaction type is supported by at least one of the services.
     *
     * @param transactionType The transaction type to check if supported
     * @return True if at least one service support it, false otherwise
     */
    public boolean isTransactionTypeSupported(String transactionType) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(supportedTransactionTypes, transactionType);
    }

    /**
     * Retrieve a consolidated set of supported transaction types across all the services.
     *
     * @return A consolidated set of supported transaction types across all the services
     */
    public Set<String> getAllSupportedTransactionTypes() {
        return supportedTransactionTypes;
    }

    /**
     * Check whether a particular currency is supported by at least one of the services.
     *
     * @param currency The currency to check if supported
     * @return True if at least one service support it, false otherwise
     */
    public boolean isCurrencySupported(String currency) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(supportedCurrencies, currency);
    }

    /**
     * Retrieve a consolidated set of supported currencies across all the services.
     *
     * @return A consolidated set of supported currencies across all the services
     */
    public Set<String> getAllSupportedCurrencies() {
        return supportedCurrencies;
    }

    /**
     * Retrieve a consolidated set of supported payment methods across all the services.
     *
     * @return A consolidated set of supported payment methods across all the services
     */
    public Set<String> getAllSupportedPaymentMethods() {
        return supportedPaymentMethods;
    }

    /**
     * Check whether a particular data key (as used with {@link com.aevi.sdk.flow.model.AdditionalData}) is supported by at least one of the services.
     *
     * @param dataKey The data key to check if supported
     * @return True if at least one service support it, false otherwise
     */
    public boolean isDataKeySupported(String dataKey) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(supportedDataKeys, dataKey);
    }

    /**
     * Retrieve a consolidated set of supported data keys ((as used with {@link com.aevi.sdk.flow.model.AdditionalData}) across all the services.
     *
     * @return A consolidated set of supported data keys across all the services
     */
    public Set<String> getAllSupportedDataKeys() {
        return supportedDataKeys;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentFlowServices fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentFlowServices.class);
    }
}
