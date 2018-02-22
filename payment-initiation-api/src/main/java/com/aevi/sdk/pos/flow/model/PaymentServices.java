package com.aevi.sdk.pos.flow.model;


import java.util.*;

/**
 * Represents the available payment services and exposes a range of helper functions to retrieve relevant information from the payment services
 * as a whole.
 */
public class PaymentServices {

    private final List<PaymentServiceInfo> paymentServiceInfoList;

    public PaymentServices(List<PaymentServiceInfo> paymentServiceInfoList) {
        this.paymentServiceInfoList = paymentServiceInfoList;
    }

    /**
     * Retrieve the full list of available payment services.
     *
     * @return The full list of available payment services
     */
    public List<PaymentServiceInfo> getAllPaymentServices() {
        return paymentServiceInfoList;
    }

    /**
     * Check whether a particular currency is supported by at least one of the payment services.
     *
     * @param currency The currency to check if supported
     * @return True if at least one payment service support it, false otherwise
     */
    public boolean isCurrencySupported(String currency) {
        return getAllSupportedCurrencies().contains(currency);
    }

    /**
     * Retrieve a consolidated set of supported currencies across all the payment services.
     *
     * @return A consolidated set of supported currencies across all the payment services
     */
    public Set<String> getAllSupportedCurrencies() {
        Set<String> currencies = new HashSet<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            currencies.addAll(Arrays.asList(serviceInfo.getSupportedCurrencies()));
        }
        return currencies;
    }

    /**
     * Check whether a particular transaction type is supported by at least one of the payment services.
     *
     * @param transactionType The transaction type to check if supported
     * @return True if at least one payment service support it, false otherwise
     */
    public boolean isTransactionTypeSupported(String transactionType) {
        return getAllSupportedTransactionTypes().contains(transactionType);
    }

    /**
     * Retrieve a consolidated set of supported transaction types across all the payment services.
     *
     * @return A consolidated set of supported transaction types across all the payment services
     */
    public Set<String> getAllSupportedTransactionTypes() {
        Set<String> txnTypes = new HashSet<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            txnTypes.addAll(Arrays.asList(serviceInfo.getSupportedTransactionTypes()));
        }
        return txnTypes;
    }

    /**
     * Retrieve a consolidated set of supported payment methods across all the payment services.
     *
     * @return A consolidated set of supported payment methods across all the payment services
     */
    public Set<String> getAllSupportedPaymentMethods() {
        Set<String> paymentMethods = new HashSet<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            paymentMethods.addAll(Arrays.asList(serviceInfo.getPaymentMethods()));
        }
        return paymentMethods;
    }

    /**
     * Check whether a particular data key (as used with {@link com.aevi.sdk.flow.model.AdditionalData}) is supported by at least one of the payment services.
     *
     * @param dataKey The data key to check if supported
     * @return True if at least one payment service support it, false otherwise
     */
    public boolean isDataKeySupported(String dataKey) {
        return getAllSupportedDataKeys().contains(dataKey);
    }

    /**
     * Retrieve a consolidated set of supported data keys ((as used with {@link com.aevi.sdk.flow.model.AdditionalData}) across all the payment services.
     *
     * @return A consolidated set of supported data keys across all the payment services
     */
    public Set<String> getAllSupportedDataKeys() {
        Set<String> dataKeys = new HashSet<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            dataKeys.addAll(Arrays.asList(serviceInfo.getSupportedDataKeys()));
        }
        return dataKeys;
    }

    /**
     * Retrieve a list of all the payment services that support tokenisation.
     *
     * @return A list of all payment services that supports tokenisation
     */
    public List<PaymentServiceInfo> getPaymentServicesSupportingTokenisation() {
        List<PaymentServiceInfo> paymentServices = new ArrayList<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            if (serviceInfo.supportsTokenization()) {
                paymentServices.add(serviceInfo);
            }
        }
        return paymentServices;
    }

    /**
     * Retrieve a list of all the payment services that support the separate card reading flow step.
     *
     * @return A list of all payment services that supports the separate card reading flow step
     */
    public List<PaymentServiceInfo> getPaymentServicesSupportingCardReadingStep() {
        List<PaymentServiceInfo> paymentServices = new ArrayList<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            if (serviceInfo.supportsFlowCardReading()) {
                paymentServices.add(serviceInfo);
            }
        }
        return paymentServices;
    }

    /**
     * Retrieve a list of all the payment services that support accessibility mode for visually impaired users.
     *
     * @return A list of all payment services that supports accessibility mode for visually impaired users
     */
    public List<PaymentServiceInfo> getPaymentServicesSupportingAccessibilityMode() {
        List<PaymentServiceInfo> paymentServices = new ArrayList<>();
        for (PaymentServiceInfo serviceInfo : paymentServiceInfoList) {
            if (serviceInfo.supportsAccessibilityMode()) {
                paymentServices.add(serviceInfo);
            }
        }
        return paymentServices;
    }

}
