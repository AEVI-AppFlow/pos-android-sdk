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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aevi.sdk.flow.util.ComparisonUtil;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;
import io.reactivex.Observable;

import java.util.*;


/**
 * Exposes payment and flow services information with helper methods to retrieve collated data across all services.
 */
public class PaymentFlowServices implements Jsonable {

    private final List<PaymentFlowServiceInfo> paymentFlowServiceInfoList;
    private final Set<String> supportedRequestTypes;
    private final Set<String> supportedCurrencies;
    private final Set<String> supportedPaymentMethods;
    private final Set<String> supportedDataKeys;

    public PaymentFlowServices(Collection<PaymentFlowServiceInfo> paymentFlowServiceInfoList) {
        this.paymentFlowServiceInfoList = new ArrayList<>(paymentFlowServiceInfoList);
        supportedRequestTypes = new HashSet<>();
        supportedCurrencies = new HashSet<>();
        supportedPaymentMethods = new HashSet<>();
        supportedDataKeys = new HashSet<>();
        for (PaymentFlowServiceInfo paymentFlowServiceInfo : paymentFlowServiceInfoList) {
            supportedRequestTypes.addAll(paymentFlowServiceInfo.getCustomRequestTypes());
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
     * Get the list of available flow services.
     *
     * @return The list of {@link PaymentFlowServiceInfo} models
     */
    @NonNull
    public List<PaymentFlowServiceInfo> getAll() {
        return paymentFlowServiceInfoList;
    }

    /**
     * Get a stream of the available flow services, allowing for simple filtering, conversions etc.
     *
     * @return An Observable stream of {@link PaymentFlowServiceInfo} models
     */
    @NonNull
    public Observable<PaymentFlowServiceInfo> stream() {
        return Observable.fromIterable(paymentFlowServiceInfoList);
    }

    /**
     * Get the flow service with the provided id.
     *
     * @param id The flow service id
     * @return An instance of PaymentFlowServiceInfo if a match was found, or null otherwise
     */
    @Nullable
    public PaymentFlowServiceInfo getFlowServiceFromId(String id) {
        for (PaymentFlowServiceInfo serviceInfo : paymentFlowServiceInfoList) {
            if (serviceInfo.getId().equals(id)) {
                return serviceInfo;
            }
        }
        return null;
    }

    /**
     * Check whether a particular custom request type is supported by at least one of the services.
     *
     * @param requestType The custom request type to check if supported
     * @return True if at least one service support it, false otherwise
     */
    public boolean isCustomRequestTypeSupported(String requestType) {
        return ComparisonUtil.stringCollectionContainsIgnoreCase(supportedRequestTypes, requestType);
    }

    /**
     * Retrieve a consolidated set of all the custom request types across all the services.
     *
     * @return A consolidated set of all the custom request types across all the services
     */
    @NonNull
    public Set<String> getAllCustomRequestTypes() {
        return supportedRequestTypes;
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
    @NonNull
    public Set<String> getAllSupportedCurrencies() {
        return supportedCurrencies;
    }

    /**
     * Retrieve a consolidated set of supported payment methods across all the services.
     *
     * @return A consolidated set of supported payment methods across all the services
     */
    @NonNull
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
    @NonNull
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
