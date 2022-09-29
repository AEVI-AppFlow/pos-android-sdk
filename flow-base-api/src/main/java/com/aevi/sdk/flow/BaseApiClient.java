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
import androidx.annotation.NonNull;

import com.aevi.android.rxmessenger.ChannelClient;
import com.aevi.android.rxmessenger.Channels;
import com.aevi.android.rxmessenger.MessageException;
import com.aevi.sdk.config.ConfigApi;
import com.aevi.sdk.config.ConfigClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.ErrorConstants;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.model.ResponseQuery;
import com.aevi.sdk.flow.model.config.AppFlowSettings;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import static com.aevi.android.rxmessenger.MessageConstants.*;
import static com.aevi.sdk.flow.constants.AppMessageTypes.*;
import static com.aevi.sdk.flow.constants.ResponseMechanisms.*;

/**
 * Internal base client for all API domain implementations.
 *
 * This is an internal class not intended to be used directly by external applications. No guarantees are made of backwards compatibility and the
 * class may be removed without any warning.
 */
public abstract class BaseApiClient {

    private static final String KEY_APPFLOW_SETTINGS = "appflowSettings";
    public static final String FLOW_PROCESSING_SERVICE = "com.aevi.sdk.fps";
    protected static final ComponentName FLOW_PROCESSING_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".FlowProcessingService");
    protected static final ComponentName SYSTEM_EVENT_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".SystemEventService");
    protected static final ComponentName INFO_PROVIDER_SERVICE_COMPONENT =
            new ComponentName(FLOW_PROCESSING_SERVICE, FLOW_PROCESSING_SERVICE + ".InfoProviderService");
    protected static final FlowException NO_FPS_EXCEPTION =
            new FlowException(ErrorConstants.PROCESSING_SERVICE_NOT_INSTALLED, "Processing service is not installed");

    private final InternalData internalData;
    protected final Context context;
    private String commsChannel;

    protected BaseApiClient(String apiVersion, Context context) {
        internalData = new InternalData(apiVersion);
        internalData.setSenderPackageName(context.getPackageName());
        this.context = context;
        checkCommsChannel(context);
    }

    private void checkCommsChannel(Context context) {
        // here we only check the channel once and once only for each instance of this client
        // once setup the client will use the same comm channel until it is disposed of and re-created
        ConfigClient configClient = ConfigApi.getConfigClient(context);
        AppFlowSettings appFlowSettings = AppFlowSettings.fromJson(configClient.getConfigValue(KEY_APPFLOW_SETTINGS));
        if (appFlowSettings == null) {
            appFlowSettings = new AppFlowSettings();
        }
        commsChannel = appFlowSettings.getCommsChannel();
    }

    protected InternalData getInternalData() {
        return internalData;
    }

    @NonNull
    public Completable initiateRequest(final Request request) {
        return doSendRequest(request, REQUEST_MESSAGE);
    }

    public Completable sendEvent(final FlowEvent flowEvent) {
        Request request = new Request(FLOW_EVENT);
        request.getRequestData().addData(FLOW_EVENT, flowEvent);
        request.setProcessInBackground(true); // events always processed in background (service receiving event may still be in the foreground)
        return doSendRequest(request, FLOW_EVENT);
    }

    private Completable doSendRequest(final Request request, String appMessageType) {
        if (!isProcessingServiceInstalled(context)) {
            return Completable.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient requestMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(appMessageType, request.toJson(), getInternalData());
        appMessage.setResponseMechanism(RESPONSE_SERVICE);
        return requestMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .ignoreElement()
                .doFinally(requestMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Completable.error(createFlowException(throwable)));
    }

    @NonNull
    public Observable<Response> queryResponses(@NonNull ResponseQuery responseQuery) {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }

        responseQuery.setResponseType(Response.class.getName());

        final ChannelClient paymentInfoMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.RESPONSES_REQUEST, responseQuery.toJson(), getInternalData());
        return paymentInfoMessenger
                .sendMessage(appMessage.toJson())
                .map(Response::fromJson)
                .doFinally(paymentInfoMessenger::closeConnection)
                .onErrorResumeNext((Function<Throwable, ObservableSource<? extends Response>>) throwable -> Observable
                        .error(createFlowException(throwable)));
    }

    protected Single<Response> initiateRequestDirect(final Request request) {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient requestMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(REQUEST_MESSAGE, request.toJson(), getInternalData());
        appMessage.setResponseMechanism(MESSENGER_CONNECTION);
        return requestMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(json -> {
                    Response response = Response.fromJson(json);
                    response.setOriginatingRequest(request);
                    return response;
                })
                .doFinally(requestMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Single.error(createFlowException(throwable)));
    }

    @NonNull
    public Single<List<Device>> getDevices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(DEVICE_INFO_REQUEST, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .map(Device::fromJson)
                .toList()
                .doFinally(deviceMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Single.error(createFlowException(throwable)));
    }

    @NonNull
    public Observable<FlowEvent> subscribeToSystemEvents() {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient deviceMessenger = getMessengerClient(SYSTEM_EVENT_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(REQUEST_MESSAGE, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .map(FlowEvent::fromJson)
                .doFinally(deviceMessenger::closeConnection)
                .onErrorResumeNext(throwable -> {
                    return Observable.error(createFlowException(throwable));
                });
    }

    protected ChannelClient getMessengerClient(ComponentName componentName) {
        switch (commsChannel) {
            case CHANNEL_WEBSOCKET:
                return Channels.webSocket(context, componentName);
            case CHANNEL_MESSENGER:
            default:
                return Channels.messenger(context, componentName);
        }
    }

    protected Throwable createFlowException(Throwable throwable) {
        if (throwable instanceof MessageException) {
            MessageException e = (MessageException) throwable;
            return new FlowException(e.getCode(), e.getMessage());
        }
        return throwable;
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
