package com.aevi.sdk.flow.model;

// TODO JAvadocs when finalised
public class FlowServiceInfoBuilder {

    private String id;
    private String vendor;
    private String version;
    private String appName;
    private String[] stages;
    private String[] capabilities;
    private String[] supportedCurrencies;
    private String[] supportedTransactionTypes;
    private boolean requiresCardToken;
    private String[] supportedDataKeys;
    private boolean backgroundOnly;
    private boolean canAdjustAmounts;
    private boolean canPayAmounts;

    public FlowServiceInfoBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public FlowServiceInfoBuilder withVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public FlowServiceInfoBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public FlowServiceInfoBuilder withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public FlowServiceInfoBuilder withStages(String[] stages) {
        this.stages = stages;
        return this;
    }

    public FlowServiceInfoBuilder withCapabilities(String[] capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public FlowServiceInfoBuilder withSupportedCurrencies(String[] supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
        return this;
    }

    public FlowServiceInfoBuilder withSupportedTransactionTypes(String[] supportedTransactionTypes) {
        this.supportedTransactionTypes = supportedTransactionTypes;
        return this;
    }

    public FlowServiceInfoBuilder withRequiresCardToken(boolean requiresCardToken) {
        this.requiresCardToken = requiresCardToken;
        return this;
    }

    public FlowServiceInfoBuilder withSupportedDataKeys(String[] supportedDataKeys) {
        this.supportedDataKeys = supportedDataKeys;
        return this;
    }

    public FlowServiceInfoBuilder withBackgroundOnly(boolean backgroundOnly) {
        this.backgroundOnly = backgroundOnly;
        return this;
    }

    public FlowServiceInfoBuilder withCanAdjustAmounts(boolean canAdjustAmounts) {
        this.canAdjustAmounts = canAdjustAmounts;
        return this;
    }

    public FlowServiceInfoBuilder withCanPayAmounts(boolean canPayAmounts) {
        this.canPayAmounts = canPayAmounts;
        return this;
    }

    public FlowServiceInfo build() {
        return new FlowServiceInfo(id, vendor, version, appName, stages, capabilities, supportedCurrencies, supportedTransactionTypes, requiresCardToken, supportedDataKeys, backgroundOnly, canAdjustAmounts, canPayAmounts);
    }
}