package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;

import java.util.Arrays;

/**
 * Represents the capabilities of a flow service.
 *
 * See {@link BaseServiceInfo} for inherited information.
 *
 * Use flow-service-api FlowServiceInfoBuilder to construct an instance.
 */
public class FlowServiceInfo extends BaseServiceInfo {

    private final String[] stages;
    private final String[] capabilities;
    private final boolean backgroundOnly;
    private final boolean requiresCardToken;
    private final boolean canAdjustAmounts;
    private final boolean canPayAmounts;

    /**
     * Use flow-service-api FlowServiceInfoBuilder for construction.
     */
    public FlowServiceInfo(String id, String vendor, String version, String apiVersion, String appName, boolean supportsAccessibility, String[] stages, String[] capabilities,
                           String[] paymentMethods, String[] supportedCurrencies, String[] supportedRequestTypes, String[] supportedTransactionTypes, boolean requiresCardToken,
                           String[] supportedDataKeys, boolean backgroundOnly, boolean canAdjustAmounts, boolean canPayAmounts) {
        super(id, vendor, version, apiVersion, appName, supportsAccessibility, paymentMethods, supportedCurrencies, supportedRequestTypes, supportedTransactionTypes, supportedDataKeys);
        this.stages = stages;
        this.capabilities = capabilities;
        this.requiresCardToken = requiresCardToken;
        this.backgroundOnly = backgroundOnly;
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
        return stages.length > 0 && Arrays.asList(stages).contains(stage);
    }

    /**
     * Get the capabilities of this service. Examples may be "loyalty", "currencyConversion", "split", etc.
     *
     * See reference values in the documentation for possible values.
     *
     * @return The set of capabilities this service offers
     */
    public String[] getCapabilities() {
        return capabilities;
    }

    /**
     * Check whether the flow services provides the given capability.
     *
     * @param capability The capability to check against
     * @return True if the flow service provides the given capability, false otherwise
     */
    public boolean providesCapability(String capability) {
        return capabilities.length > 0 && Arrays.asList(capabilities).contains(capability);
    }

    /**
     * Check whether this service operates solely in the background. If set, the service will not launch any user interface.
     *
     * @return True if background only, false otherwise
     */
    public boolean isBackgroundOnly() {
        return backgroundOnly;
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
                ", capabilities=" + Arrays.toString(capabilities) +
                ", backgroundOnly=" + backgroundOnly +
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

        if (backgroundOnly != that.backgroundOnly) return false;
        if (requiresCardToken != that.requiresCardToken) return false;
        if (canAdjustAmounts != that.canAdjustAmounts) return false;
        if (canPayAmounts != that.canPayAmounts) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(stages, that.stages)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(capabilities, that.capabilities);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(stages);
        result = 31 * result + Arrays.hashCode(capabilities);
        result = 31 * result + (backgroundOnly ? 1 : 0);
        result = 31 * result + (requiresCardToken ? 1 : 0);
        result = 31 * result + (canAdjustAmounts ? 1 : 0);
        result = 31 * result + (canPayAmounts ? 1 : 0);
        return result;
    }
}
