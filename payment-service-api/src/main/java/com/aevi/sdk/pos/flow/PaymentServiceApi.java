package com.aevi.sdk.pos.flow;


/**
 * Provides general API information.
 */
public final class PaymentServiceApi {

    private PaymentServiceApi() {
    }

    /**
     * Get the API version.
     *
     * The API versioning follows semver rules with major.minor.patch versions.
     *
     * @return The API version
     */
    public static String getApiVersion() {
        return PaymentServiceConfig.VERSION;
    }
}
