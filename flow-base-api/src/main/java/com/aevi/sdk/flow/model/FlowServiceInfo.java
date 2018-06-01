package com.aevi.sdk.flow.model;


import com.aevi.sdk.flow.util.ComparisonUtil;
import com.aevi.util.json.JsonConverter;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the capabilities of a flow service.
 *
 * See {@link BaseServiceInfo} for inherited information.
 *
 * Use flow-service-api FlowServiceInfoBuilder to construct an instance.
 */
public class FlowServiceInfo extends BaseServiceInfo {

    private final boolean requiresCardToken;
    private final boolean canAdjustAmounts;
    private final boolean canPayAmounts;
    private String[] stages;

    /**
     * Use flow-service-api FlowServiceInfoBuilder for construction.
     */
    public FlowServiceInfo(String id, String vendor, String version, String apiVersion, String appName, boolean supportsAccessibility,
                           String[] paymentMethods, String[] supportedCurrencies, String[] supportedRequestTypes, String[] supportedTransactionTypes, boolean requiresCardToken,
                           String[] supportedDataKeys, boolean canAdjustAmounts, boolean canPayAmounts) {
        super(id, vendor, version, apiVersion, appName, supportsAccessibility, paymentMethods, supportedCurrencies, supportedRequestTypes, supportedTransactionTypes, supportedDataKeys);
        this.requiresCardToken = requiresCardToken;
        this.canAdjustAmounts = canAdjustAmounts;
        this.canPayAmounts = canPayAmounts;
    }

    /**
     * Get the stages this service operates in.
     *
     * For POS flow, the possible stages are defined in the PaymentStage model and PaymentStage.valueOf(xxx) can be used to map these values.
     *
     * @return The set of stages the service operates in.
     */
    public String[] getStages() {
        return stages;
    }

    /**
     * Check whether the flow services operates in the given stage.
     *
     * @param stage The stage to check against
     * @return True if the flow service operates in the given stage, false otherwise
     */
    public boolean containsStage(String stage) {
        return stages.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(stages, stage);
    }

    /**
     * Check whether this services requires a card token to operate.
     *
     * If set, the service will not be able to perform its tasks without a card token being passed.
     *
     * @return True if card token is required, false otherwise
     */
    public boolean requiresCardToken() {
        return requiresCardToken;
    }

    /**
     * Check whether this service can adjust the request amounts.
     *
     * This is typically used to add a charge/fee, or for charity, or to split a request.
     *
     * @return True if the service can adjust request amounts, false otherwise
     */
    public boolean canAdjustAmounts() {
        return canAdjustAmounts;
    }

    /**
     * Check whether this service can pay amounts via non-payment card means, such as loyalty points.
     *
     * @return True if the service can pay amounts, false otherwise.
     */
    public boolean canPayAmounts() {
        return canPayAmounts;
    }

    /**
     * For internal use.
     *
     * @param stages Stages
     */
    public void setStages(String[] stages) {
        this.stages = stages;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowServiceInfo fromJson(String json) {
        return JsonConverter.deserialize(json, FlowServiceInfo.class);
    }

    @Override
    public String toString() {
        return "FlowServiceInfo{" +
                "stages=" + Arrays.toString(stages) +
                ", requiresCardToken=" + requiresCardToken +
                ", canAdjustAmounts=" + canAdjustAmounts +
                ", canPayAmounts=" + canPayAmounts +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlowServiceInfo that = (FlowServiceInfo) o;
        return requiresCardToken == that.requiresCardToken &&
                canAdjustAmounts == that.canAdjustAmounts &&
                canPayAmounts == that.canPayAmounts &&
                Arrays.equals(stages, that.stages);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), requiresCardToken, canAdjustAmounts, canPayAmounts);
        result = 31 * result + Arrays.hashCode(stages);
        return result;
    }
}
