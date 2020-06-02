package com.aevi.sdk.pos.flow.model.events;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * A print receipt event
 */
public class Receipt {

    @NonNull
    private final String receiptType;

    @Nullable
    private String receiptText;

    @Nullable
    private String receiptData;

    @Nullable
    private String receiptDataFormat;


    public Receipt(@NonNull String receiptType) {
        this(receiptType, null);
    }

    /**
     * Creates a receipt object
     * @param receiptType The type of receipt this is e.g. customer, merchant
     * @param receiptText The actual formatted text of the receipt
     */
    public Receipt(@NonNull String receiptType, @Nullable String receiptText) {
        this.receiptType = receiptType;
        this.receiptText = receiptText;
    }

    /**
     * Sets the receipt data
     * @param receiptDataFormat The format of the data set here e.g. json
     * @param receiptData The actual (escaped) data
     */
    public void setReceiptData(@NonNull String receiptDataFormat, @NonNull String receiptData) {
        this.receiptDataFormat = receiptDataFormat;
        this.receiptData = receiptData;
    }

    /**
     * @return The type of receipt this is e.g. customer, merchant
     */
    @NonNull
    public String getReceiptType() {
        return receiptType;
    }

    /**
     * @return The formatted text of the receipt
     */
    @Nullable
    public String getReceiptText() {
        return receiptText;
    }

    /**
     * @return An optional data format that can be set if unformatted receipt data is included e.g. json, xml
     */
    @Nullable
    public String getReceiptDataFormat() {
        return receiptDataFormat;
    }

    /**
     * @return Optional unformatted but structured print data e.g. json, xml
     */
    @Nullable
    public String getReceiptData() {
        return receiptData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return receiptType.equals(receipt.receiptType) &&
                Objects.equals(receiptText, receipt.receiptText) &&
                Objects.equals(receiptData, receipt.receiptData) &&
                Objects.equals(receiptDataFormat, receipt.receiptDataFormat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptType, receiptText, receiptData, receiptDataFormat);
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptType='" + receiptType + '\'' +
                ", receiptText='" + receiptText + '\'' +
                ", receiptData='" + receiptData + '\'' +
                ", receiptDataFormat='" + receiptDataFormat + '\'' +
                '}';
    }
}
