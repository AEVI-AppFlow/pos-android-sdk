package com.aevi.sdk.pos.flow;

import android.content.Context;

import com.aevi.sdk.flow.ApiBase;

/**
 * Main entry point to obtain references to the payment related clients.
 *
 * Please see {@link com.aevi.sdk.flow.FlowApi} for details on general flow functionality.
 */
public final class PaymentApi {

    private PaymentApi() {
    }

    /**
     * Get the API version.
     *
     * The API versioning follows semver rules with major.minor.patch versions.
     *
     * @return The API version
     */
    public static String getApiVersion() {
        return PaymentInitiationConfig.VERSION;
    }

    /**
     * Returns true if the processing service that handles API requests is installed.
     *
     * If not installed, none of the API calls will function.
     *
     * @param context The Android context
     * @return True if API processing service is installed, false otherwise
     */
    public static boolean isProcessingServiceInstalled(Context context) {
        return ApiBase.isProcessingServiceInstalled(context);
    }

    /**
     * Get the processing service version installed on this device.
     *
     * @param context The Android context
     * @return The processing service version (semver format)
     */
    public static String getProcessingServiceVersion(Context context) {
        return ApiBase.getProcessingServiceVersion(context);
    }

    /**
     * Get a new instance of a {@link PaymentClient} to initiate payments.
     *
     * @param context The Android context
     * @return An instance of {@link PaymentClient}
     */
    public static PaymentClient getPaymentClient(Context context) {
        return new PaymentClientImpl(context);
    }
}
