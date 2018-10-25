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


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import com.aevi.android.rxmessenger.ChannelClient;
import com.aevi.android.rxmessenger.Channels;
import com.aevi.sdk.config.ConfigApi;
import com.aevi.sdk.config.ConfigClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import static com.aevi.android.rxmessenger.MessageConstants.CHANNEL_WEBSOCKET;

/**
 * Base client for all API domain implementations.
 */
public abstract class BaseApiClient {

    private static final String APPFLOW_COMMS_CHANNEL = "appFlowCommsChannel";

    public static final String FLOW_PROCESSING_SERVICE = "com.aevi.sdk.fps";
    protected static final ComponentName FLOW_PROCESSING_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".FlowProcessingService");
    protected static final ComponentName REQUEST_STATUS_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".RequestStatusService");
    protected static final ComponentName SYSTEM_EVENT_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".SystemEventService");
    protected static final ComponentName INFO_PROVIDER_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".InfoProviderService");
    protected static final IllegalStateException NO_FPS_EXCEPTION = new IllegalStateException("Processing service is not installed");

    private final InternalData internalData;
    protected final Context context;
    private boolean useWebsocket = false;

    protected BaseApiClient(String apiVersion, Context context) {
        internalData = new InternalData(apiVersion);
        this.context = context;
        checkCommsChannel(context);
    }

    private void checkCommsChannel(Context context) {
        // here we only check the channel once and once only for each instance of this client
        // once setup the client will use the same comm channel until it is disposed of and re-created
        ConfigClient configClient = ConfigApi.getConfigClient(context);
        String channel = configClient.getConfigValue(APPFLOW_COMMS_CHANNEL);
        if(CHANNEL_WEBSOCKET.equals(channel)) {
            useWebsocket = true;
        }
    }

    protected InternalData getInternalData() {
        return internalData;
    }

    @NonNull
    public Single<Response> initiateRequest(final Request request) {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient requestMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson(), getInternalData());
        return requestMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String json) throws Exception {
                        Response response = Response.fromJson(json);
                        response.setOriginatingRequest(request);
                        return response;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        requestMessenger.closeConnection();
                    }
                });
    }

    @NonNull
    public Single<List<Device>> getDevices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.DEVICE_INFO_REQUEST, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .map(new Function<String, Device>() {
                    @Override
                    public Device apply(String json) throws Exception {
                        return Device.fromJson(json);
                    }
                })
                .toList()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        deviceMessenger.closeConnection();
                    }
                });
    }

    @NonNull
    public Observable<FlowEvent> subscribeToSystemEvents() {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient deviceMessenger = getMessengerClient(SYSTEM_EVENT_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .map(new Function<String, FlowEvent>() {
                    @Override
                    public FlowEvent apply(String json) throws Exception {
                        return FlowEvent.fromJson(json);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        deviceMessenger.closeConnection();
                    }
                });
    }

    protected ChannelClient getMessengerClient(ComponentName componentName) {
        if (useWebsocket) {
            return Channels.webSocket(context, componentName);
        } else {
            return Channels.messenger(context, componentName);
        }
    }

    protected static void startFps(Context context) {
        // Start service explicitly to keep it running
        Intent intent = getIntent(FLOW_PROCESSING_SERVICE_COMPONENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    protected static Intent getIntent(ComponentName componentName) {
        Intent intent = new Intent();
        intent.setComponent(componentName);
        return intent;
    }

    public static boolean isProcessingServiceInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager
                .queryIntentServices(getIntent(FLOW_PROCESSING_SERVICE_COMPONENT), PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() == 1 && resolveInfo.get(0).serviceInfo != null;
    }

    @NonNull
    public static String getProcessingServiceVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(FLOW_PROCESSING_SERVICE, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }
}
