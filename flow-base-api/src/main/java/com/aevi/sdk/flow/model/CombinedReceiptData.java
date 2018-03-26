package com.aevi.sdk.flow.model;


import java.util.ArrayList;
import java.util.List;

/**
 * A utility class specifically to deal with combined receipt printing data that may be passed beteeen applications.
 *
 * This class helps with legacy support for the AEVI V2 simple payment API i.e. request classes that extend the class com.aevi.payment.AeviRequest.
 */
public class CombinedReceiptData {

    public static final String POSITION_BASKET = "BASKET";
    public static final String POSITION_HEADER = "HEADER";
    public static final String POSITION_FOOTER = "FOOTER";

    public static final String RECEIPT_TYPE_ALL = "ALL";
    public static final String RECEIPT_TYPE_CUSTOMER = "CUSTOMER";
    public static final String RECEIPT_TYPE_MERCHANT = "MERCHANT";

    public static final String PAYMENT_STATUS_ALL = "ALL";
    public static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";
    public static final String PAYMENT_STATUS_FAILURE = "FAILURE";

    private List<ReceiptPayload> payloadList = new ArrayList<>();

    public void addPayload(ReceiptPayload payload) {
        payloadList.add(payload);
    }

    /**
     * Returns all the payloads that were setup in the original AeviRequest send via the V2 simple payment API
     *
     * @return List of {@link ReceiptPayload} objects
     */
    public List<ReceiptPayload> getPayloadList() {
        return payloadList;
    }

    /**
     * Each print payload from the V2 SDK is mapped to a single instance of this class
     *
     * The position, receiptType and paymentStatus can be used to determine which receipt to use the printData for and where the data corresponds to
     *
     * These are mapped directly from there corresponding V2 com.aevi.payment.AeviRequest equivalents for assistance with legacy support
     */
    public static class ReceiptPayload {

        private String position;
        private String receiptType;
        private String paymentStatus;
        private String printData;

        public ReceiptPayload(String position, String receiptType, String paymentStatus, String printData) {
            this.receiptType = receiptType;
            this.paymentStatus = paymentStatus;
            this.position = position;
            this.printData = printData;
        }

        /**
         * @return The receipt type this print payload should be used for e.g. CUSTOMER or MERCHANT
         */
        public String getReceiptType() {
            return receiptType;
        }

        /**
         * @return The payment status this print payload should be used for e.g. SUCCESS or FAILURE
         */
        public String getPaymentStatus() {
            return paymentStatus;
        }

        /**
         * @return The position this print payload should appear in e.g. BASKET, HEADER or FOOTER
         */
        public String getPosition() {
            return position;
        }

        /**
         * @return The print payload in JSON format use the AEVI print-api to deserialize
         */
        public String getPrintData() {
            return printData;
        }
    }
}