package com.aevi.sdk.flow.model;


import java.util.ArrayList;
import java.util.List;

public class LegacyPrintData {

    public static final String POSITION_BASKET = "BASKET";
    public static final String POSITION_HEADER = "HEADER";
    public static final String POSITION_FOOTER = "FOOTER";

    public static final String RECEIPT_TYPE_ALL = "ALL";
    public static final String RECEIPT_TYPE_CUSTOMER = "CUSTOMER";
    public static final String RECEIPT_TYPE_MERCHANT = "MERCHANT";

    public static final String PAYMENT_STATUS_ALL = "ALL";
    public static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";
    public static final String PAYMENT_STATUS_FAILURE = "FAILURE";

    private List<LegacyPayload> payloadList = new ArrayList<>();

    public void addPayload(LegacyPayload payload) {
        payloadList.add(payload);
    }

    public List<LegacyPayload> getPayloadList() {
        return payloadList;
    }

    public static class LegacyPayload {

        private String position;
        private String receiptType;
        private String paymentStatus;
        private String printPayload;

        public LegacyPayload(String position, String receiptType, String paymentStatus, String printPayload) {
            this.receiptType = receiptType;
            this.paymentStatus = paymentStatus;
            this.position = position;
            this.printPayload = printPayload;
        }

        public String getReceiptType() {
            return receiptType;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getPosition() {
            return position;
        }

        public String getPrintPayload() {
            return printPayload;
        }
    }
}
