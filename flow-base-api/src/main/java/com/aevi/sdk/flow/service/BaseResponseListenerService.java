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
package com.aevi.sdk.flow.service;

import android.support.annotation.NonNull;
import com.aevi.sdk.flow.FlowBaseConfig;
import com.aevi.sdk.flow.model.Response;

/**
 * Extend this service in your application to listen to responses for "generic" requests and status updates initiated by your application.
 *
 * You must extend this service in your application and register it correctly in your manifest. The service must
 * be exported and must include the intent-filter "com.aevi.sdk.flow.action.PROCESS_RESPONSE"
 *
 * {@code
 *
 * <service
 * android:name=".ResponseListenerService"
 * android:exported="true">
 * <intent-filter>
 * <action android:name="com.aevi.sdk.flow.action.PROCESS_RESPONSE"/>
 * </intent-filter>
 * </service>
 * }
 */
public abstract class BaseResponseListenerService extends BaseListenerService<Response> {

    protected BaseResponseListenerService() {
        super(Response.class, FlowBaseConfig.VERSION);
    }

    @Override
    protected void notifyResponse(@NonNull Response response) {
        if (response.wasProcessedInBackground()) {
            notifyStatusUpdateResponse(response);
        } else {
            notifyGenericResponse(response);
        }
    }

    /**
     * Called for responses to generic flows that are processed in the foreground, such as for tokenisation or a custom request type.
     *
     * @param response The generic flow response
     */
    protected abstract void notifyGenericResponse(@NonNull Response response);

    /**
     * Called for responses to status update flows which are processed in the background.
     *
     * If you do not wish to handle such responses, you can just leave the implementation of this empty.
     *
     * @param response The status update response
     */
    protected abstract void notifyStatusUpdateResponse(@NonNull Response response);
}
