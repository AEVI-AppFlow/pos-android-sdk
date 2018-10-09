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

import com.aevi.sdk.flow.FlowBaseConfig;
import com.aevi.sdk.flow.model.Response;

/**
 * Extend this service in your application if you want to listen to responses for requests initiated by your application.
 *
 * OR If you are implementing a flow service then extend this service to listen to the final `Response` when the flow is complete.
 *
 * In either case you must extend this service in your own application and register it correctly in your manifest. The service must
 * be exported and must include the intent-filter "com.aevi.sdk.flow.listener.RESPONSE"
 *
 * {@code
 *
 * <service
 * android:name=".ResponseListenerService"
 * android:exported="true">
 * <intent-filter>
 * <action android:name="com.aevi.sdk.flow.listener.RESPONSE"/>
 * </intent-filter>
 * </service>
 * }
 *
 * The service will be called asynchronously by the flow processing service. Therefore, the original request may have completed before this is called.
 *
 * This service can be used to verify a response and/or recover from crashes or issues in your application that may have prevented the response being
 * received in the original initiation call.
 */
public abstract class BaseResponseListenerService extends BaseListenerService<Response> {

    protected BaseResponseListenerService() {
        super(Response.class, FlowBaseConfig.VERSION);
    }
}
