package com.aevi.sdk.flow;


import android.content.Context;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.*;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public final class FlowClientImpl extends ApiBase implements FlowClient {

    private static final String API_PROPS_FILE = "flow-api.properties";
    private final Context context;

    FlowClientImpl(Context context) {
        super(API_PROPS_FILE);
        this.context = context;
    }

    @Override
    public Single<List<Device>> getDevices() {
        final ObservableMessengerClient deviceMessenger = new ObservableMessengerClient(context, DEVICE_LIST_SERVICE_COMPONENT);
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
    public Single<List<FlowServiceInfo>> getFlowServices() {
        return Single.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @Override
    public Observable<Event> subscribeToEventStream() {
        return Observable.error(new UnsupportedOperationException("Not yet implemented"));
    }
}
