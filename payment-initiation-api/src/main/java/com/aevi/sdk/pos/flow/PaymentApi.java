/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.pos.flow;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aevi.sdk.flow.BaseApiClient;

/**
 * Main entry point to obtain references to the payment related clients.
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
    @NonNull
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
        return BaseApiClient.isProcessingServiceInstalled(context);
    }

    /**
     * Get the processing service version installed on this device.
     *
     * @param context The Android context
     * @return The processing service version (semver format)
     */
    @NonNull
    public static String getProcessingServiceVersion(Context context) {
        return BaseApiClient.getProcessingServiceVersion(context);
    }

    /**
     * Get a new instance of a {@link PaymentClient} to initiate payments.
     *
     * @param context The Android context
     * @return An instance of {@link PaymentClient}
     */
    @NonNull
    public static PaymentClient getPaymentClient(Context context) {
        return new PaymentClientImpl(context);
    }
}
