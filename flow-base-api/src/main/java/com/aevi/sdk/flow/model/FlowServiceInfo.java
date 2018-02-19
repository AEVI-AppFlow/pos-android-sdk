package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;

// TODO JAvadocs when finalised
public class FlowServiceInfo extends BaseModel {

    private final String vendor;
    private final String version;
    private final String appName;
    private final String[] stages;
    private final String[] capabilities;
    private final String[] supportedDataKeys;
    private final boolean backgroundOnly;

    // Payment specific stuff
    private final String[] supportedCurrencies;
    private final String[] supportedTransactionTypes;
    private final boolean requiresCardToken;
    private final boolean canAdjustAmounts;
    private final boolean canPayAmounts;

    FlowServiceInfo(String id, String vendor, String version, String appName, String[] stages, String[] capabilities,
                    String[] supportedCurrencies, String[] supportedTransactionTypes, boolean requiresCardToken,
                    String[] supportedDataKeys, boolean backgroundOnly, boolean canAdjustAmounts, boolean canPayAmounts) {
        super(id);
        this.vendor = vendor;
        this.version = version;
        this.appName = appName;
        this.stages = stages;
        this.capabilities = capabilities;
        this.supportedCurrencies = supportedCurrencies;
        this.supportedTransactionTypes = supportedTransactionTypes;
        this.requiresCardToken = requiresCardToken;
        this.supportedDataKeys = supportedDataKeys;
        this.backgroundOnly = backgroundOnly;
        this.canAdjustAmounts = canAdjustAmounts;
        this.canPayAmounts = canPayAmounts;
    }

    public String getVendor() {
        return vendor;
    }

    public String getVersion() {
        return version;
    }

    public String getAppName() {
        return appName;
    }

    public String[] getStages() {
        return stages;
    }

    public String[] getCapabilities() {
        return capabilities;
    }

    public String[] getSupportedDataKeys() {
        return supportedDataKeys;
    }

    public boolean isBackgroundOnly() {
        return backgroundOnly;
    }

    public String[] getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public String[] getSupportedTransactionTypes() {
        return supportedTransactionTypes;
    }

    public boolean isRequiresCardToken() {
        return requiresCardToken;
    }

    public boolean isCanAdjustAmounts() {
        return canAdjustAmounts;
    }

    public boolean isCanPayAmounts() {
        return canPayAmounts;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowServiceInfo fromJson(String json) {
        return JsonConverter.deserialize(json, FlowServiceInfo.class);
    }
}
