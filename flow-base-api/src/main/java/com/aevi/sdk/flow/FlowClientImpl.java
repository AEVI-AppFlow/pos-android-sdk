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

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.*;
import com.aevi.sdk.flow.model.config.SystemSettings;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class FlowClientImpl extends ApiBase implements FlowClient {

    protected FlowClientImpl(String apiVersion, Context context) {
        super(apiVersion, context);
    }

    @Override
    @NonNull
    public Single<List<Device>> getDevices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
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

    @Override
    @NonNull
    public Single<FlowServices> getFlowServices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient flowInfoMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.FLOW_SERVICE_INFO_REQUEST, getInternalData());
        return flowInfoMessenger
                .sendMessage(appMessage.toJson())
                .map(new Function<String, FlowServiceInfo>() {
                    @Override
                    public FlowServiceInfo apply(String json) throws Exception {
                        return FlowServiceInfo.fromJson(json);
                    }
                })
                .toList()
                .map(new Function<List<FlowServiceInfo>, FlowServices>() {
                    @Override
                    public FlowServices apply(List<FlowServiceInfo> flowServiceInfos) throws Exception {
                        return new FlowServices(flowServiceInfos);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        flowInfoMessenger.closeConnection();
                    }
                });
    }

    @Override
    @NonNull
    public Single<List<String>> getSupportedRequestTypes() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.SUPPORTED_REQUEST_TYPES_REQUEST, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .toList()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        deviceMessenger.closeConnection();
                    }
                });
    }

    @Override
    @NonNull
    public Single<Response> processRequest(Request request) {
        return sendGenericRequest(request);
    }

    @Override
    @NonNull
    public Observable<FlowEvent> subscribeToSystemEvents() {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(SYSTEM_EVENT_SERVICE_COMPONENT);
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

    @Override
    @NonNull
    public Single<SystemSettings> getSystemSettings() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.SYSTEM_SETTINGS_REQUEST, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, SystemSettings>() {
                    @Override
                    public SystemSettings apply(String json) throws Exception {
                        return SystemSettings.fromJson(json);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        deviceMessenger.closeConnection();
                    }
                });
    }
}
