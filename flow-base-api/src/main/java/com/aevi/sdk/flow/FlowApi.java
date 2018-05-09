package com.aevi.sdk.flow;


import android.content.Context;

public final class FlowApi {

    private FlowApi() {
    }

    /**
     * Get the API version.
     *
     * The API versioning follows semver rules with major.minor.patch versions.
     *
     * @return The API version
     */
    public static String getApiVersion() {
        return FlowBaseConfig.VERSION;
    }

    /**
     * Returns true if the processing service that handles API requests is installed.
     *
     * If not installed, none of the API calls will function.
     *
     * @param context Android context
     * @return True if API processing service is installed, false otherwise
     */
    public static boolean isProcessingServiceInstalled(Context context) {
        return ApiBase.isProcessingServiceInstalled(context);
    }

    /**
     * Get a new instance of a {@link FlowClient} for flow related functions.
     *
     * @param context The Android context
     * @return An instance of {@link FlowClient}
     */
    public static FlowClient getFlowClient(Context context) {
        return new FlowClientImpl(context);
    }
}
