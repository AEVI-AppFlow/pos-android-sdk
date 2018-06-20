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

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.*;

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
    public Single<List<Device>> getDevices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(DEVICE_LIST_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData());
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
    public Single<FlowServices> getFlowServices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient flowInfoMessenger = getMessengerClient(FLOW_SERVICE_INFO_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData());
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
    public Single<Response> processRequest(Request request) {
        return sendGenericRequest(request);
    }

    @Override
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
    public Single<AdditionalData> getSystemSettings() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(SYSTEM_SETTINGS_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, AdditionalData>() {
                    @Override
                    public AdditionalData apply(String json) throws Exception {
                        return AdditionalData.fromJson(json);
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
