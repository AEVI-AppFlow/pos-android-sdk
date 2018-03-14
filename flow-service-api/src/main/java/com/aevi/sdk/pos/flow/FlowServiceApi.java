package com.aevi.sdk.pos.flow;


/**
 * Provides general API information.
 */
public final class FlowServiceApi {

    private FlowServiceApi() {
    }

    /**
     * Get the API version.
     *
     * The API versioning follows semver rules with major.minor.patch versions.
     *
     * @return The API version
     */
    public static String getApiVersion() {
        return FlowServiceConfig.VERSION;
    }
}
