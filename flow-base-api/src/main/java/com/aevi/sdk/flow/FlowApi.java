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

package com.aevi.sdk.flow;


import android.content.Context;
import android.support.annotation.NonNull;

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
    @NonNull
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
    @NonNull
    public static FlowClient getFlowClient(Context context) {
        return new FlowClientImpl(FlowBaseConfig.VERSION, context);
    }
}
