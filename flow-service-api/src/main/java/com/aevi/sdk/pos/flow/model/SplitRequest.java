package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.util.json.JsonConverter;

import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class SplitRequest extends BaseModel {

    private final Payment sourcePayment;
    private final List<Transaction> transactions;
    private DeviceAudience deviceAudience;

    public SplitRequest(Payment sourcePayment, List<Transaction> transactions) {
        super(UUID.randomUUID().toString());
        this.sourcePayment = sourcePayment;
        this.transactions = transactions;
    }

    /**
     * Get the source Request, as provide by the calling VAA.
     *
     * @return The source {@link Payment}
     */
    @NonNull
    public Payment getSourcePayment() {
        return sourcePayment;
    }

    /**
     * Get the list of transactions (consists of TransactionRequest and TransactionResponse pairs)
     *
     * Note that this list will be empty before any transactions have taken place.
     *
     * @return The list of {@link Transaction} objects
     */
    @NonNull
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Convenience method to get the current transaction.
     *
     * @return The current (or last completed) {@link Transaction}
     */
    @Nullable
    public Transaction getActiveTransaction() {
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.get(transactions.size() - 1);
    }

    /**
     * Get the amounts requested for processing.
     *
     * @return The requested {@link Amounts} for processing
     */
    @Nullable
    public Amounts getRequestedAmounts() {
        return sourcePayment.getAmounts();
    }

    /**
     * Get the remaining amounts to process for this {@link Payment} in order to meet the {@link #getRequestedAmounts()}.
     *
     * @return The remaining {@link Amounts} to process
     */
    @Nullable
    public Amounts getRemainingAmounts() {
        if (getRequestedAmounts() == null) {
            return null;
        }
        if (transactions.isEmpty()) {
            return getRequestedAmounts();
        }
        Amounts processed = getProcessedAmounts();
        return Amounts.subtractAmounts(getRequestedAmounts(), processed);
    }

    /**
     * Get the processed amounts at this point in time.
     *
     * This will be zero-based if no amounts have been processed yet.
     *
     * Note that it is possible in some cases that this will be larger than {@link #getRequestedAmounts()}.
     *
     * @return The processed {@link Amounts} at this point in time
     */
    @Nullable
    public Amounts getProcessedAmounts() {
        if (getRequestedAmounts() == null) {
            return null;
        }
        Amounts processed = new Amounts(0, getRequestedAmounts().getCurrency());
        if (transactions.isEmpty()) {
            return processed;
        }
        for (Transaction transaction : transactions) {
            processed = Amounts.addAmounts(processed, transaction.getProcessedAmounts());
        }
        return processed;
    }

    public void setDeviceAudience(DeviceAudience deviceAudience) {
        this.deviceAudience = deviceAudience;
    }

    /**
     * Get the type of audience (merchant or customer) that will be interacting with the device your app is running on.
     *
     * This information can be used to customise your application for the audience appropriately.
     *
     * @return The type of audience (merchant or customer) of the device this app is running on
     */
    @NonNull
    public DeviceAudience getDeviceAudience() {
        return deviceAudience;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static SplitRequest fromJson(String json) {
        return JsonConverter.deserialize(json, SplitRequest.class);
    }
}
