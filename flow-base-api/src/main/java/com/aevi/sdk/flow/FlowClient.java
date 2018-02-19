package com.aevi.sdk.flow;


import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.Event;
import com.aevi.sdk.flow.model.FlowServiceInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Client for general functions not directly related to any particular domain (such as payments).
 */
public interface FlowClient {

    /**
     * Query for devices connected to the processing service.
     *
     * Returns a single that emits a list of currently connected devices.
     *
     * This should be queried each time a selection is required to ensure an up-to-date list.
     *
     * You can subscribe to {@link #subscribeToEventStream()} for updates on changes to the available devices.
     *
     * @return Single emitting a list of {@link Device} objects containing basic device info
     */
    Single<List<Device>> getDevices();

    /**
     * Query for installed flow services on this and connected devices.
     *
     * Returns a single that emits a list of currently available flow services.
     *
     * This should be queried each time an up-to-date list is required.
     *
     * You can subscribe to {@link #subscribeToEventStream()} for updates on changes to the available flow services.
     *
     * @return Single emitting a list of {@link FlowServiceInfo} objects containing flow service info
     */
    Single<List<FlowServiceInfo>> getFlowServices();

    /**
     * Subscribe to general system events.
     *
     * Examples are when devices, payment services or flow services have been added or removed.
     *
     * @return A stream that will emit {@link Event} items
     */
    Observable<Event> subscribeToEventStream();
}
