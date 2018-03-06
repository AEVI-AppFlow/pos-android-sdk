package com.aevi.sdk.pos.flow.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents information about a "flow app" that was executed as part of a transaction.
 */
public class FlowAppInfo {

    public static final String AUGMENTED_DATA_AMOUNTS = "amounts";
    public static final String AUGMENTED_DATA_OPTIONS = "options";
    public static final String AUGMENTED_DATA_SPLIT = "split";
    public static final String AUGMENTED_DATA_CANCELLED = "cancelled";

    private final String appName;
    private final String[] capabilities;
    private final String stage;
    private final List<String> augmentedData;

    /**
     * Create a new FlowAppInfo instance with augmented data information.
     *
     * @param appName       The flow application name
     * @param capabilities  The flow capabilities
     * @param stage         The stage at which the flow app is called
     * @param augmentedData The list of augmented data
     */
    public FlowAppInfo(String appName, String[] capabilities, String stage, List<String> augmentedData) {
        this.appName = appName;
        this.capabilities = capabilities;
        this.stage = stage;
        this.augmentedData = augmentedData;
    }

    /**
     * Create a new FlowAppInfo instance without any augmented data.
     *
     * @param appName      The flow application name
     * @param capabilities The flow capabilities
     * @param stage        The stage at which the flow app is called
     */
    public FlowAppInfo(String appName, String[] capabilities, String stage) {
        this(appName, capabilities, stage, new ArrayList<String>());
    }

    /**
     * Get the flow application name.
     *
     * @return The flow application name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Get the flow application capabilities (such as loyalty, dcc, etc).
     *
     * This will help determining for what purposes the transaction was augmented.
     *
     * @return The capabilities
     */
    public String[] getCapabilities() {
        return capabilities;
    }

    /**
     * Get the list of data this flow app augmented as part of its execution.
     *
     * Note that this simply indicates what was augmented - not to what. The {@link PaymentResponse} can be parsed to review that information.
     *
     * See {@link #AUGMENTED_DATA_AMOUNTS}, {@link #AUGMENTED_DATA_OPTIONS}, {@link #AUGMENTED_DATA_CANCELLED} and {@link #AUGMENTED_DATA_SPLIT} for
     * the possible options.
     *
     * @return The list of data that was augmented.
     */
    public List<String> getAugmentedData() {
        return augmentedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlowAppInfo that = (FlowAppInfo) o;

        if (appName != null ? !appName.equals(that.appName) : that.appName != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(capabilities, that.capabilities)) {
            return false;
        }
        if (stage != null ? !stage.equals(that.stage) : that.stage != null) {
            return false;
        }
        return augmentedData != null ? augmentedData.equals(that.augmentedData) : that.augmentedData == null;
    }

    @Override
    public int hashCode() {
        int result = appName != null ? appName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(capabilities);
        result = 31 * result + (stage != null ? stage.hashCode() : 0);
        result = 31 * result + (augmentedData != null ? augmentedData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FlowAppInfo{" +
                "appName='" + appName + '\'' +
                ", capabilities=" + Arrays.toString(capabilities) +
                ", stage=" + stage +
                ", augmentedData=" + augmentedData +
                '}';
    }
}
