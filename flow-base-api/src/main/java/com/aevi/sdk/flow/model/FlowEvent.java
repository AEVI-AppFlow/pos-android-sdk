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

package com.aevi.sdk.flow.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Objects;

/**
 * Represents an event in the flow.
 */
public class FlowEvent implements Jsonable {

    private final String type;
    private final AdditionalData data;

    private String eventTrigger;
    private String originatingRequestId;
    private String target;

    public FlowEvent(String type) {
        this.type = type;
        this.data = new AdditionalData();
    }

    public FlowEvent(String type, AdditionalData data) {
        this.type = type;
        this.data = data;
    }

    public FlowEvent(String type, AdditionalData data, String eventTrigger) {
        this.type = type;
        this.data = data;
        this.eventTrigger = eventTrigger;
    }

    /**
     * Creates an event that contains an arbitrary event data model
     *
     * @param type      The type of event
     * @param eventData The data object
     */
    @SuppressWarnings("unchecked")
    public <T> FlowEvent(String type, T eventData) {
        this.type = type;
        this.data = new AdditionalData();
        this.data.addData(type, eventData);
    }

    /**
     * Get the type of the event.
     *
     * @return The event type
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * Get the data for the event.
     *
     * @return The event data
     */
    @NonNull
    public AdditionalData getData() {
        return data;
    }

    /**
     * If this event has an event data object this method can be used as a simple way to get it
     *
     * @param classType The type of class you are expecting in this event type
     * @return The data object if present in the event data
     */
    public <T> T getEventData(Class<T> classType) {
        return this.data.getValue(type, classType);
    }

    /**
     * @return True if this event had some data object assoicated with it
     */
    public boolean hasEventData() {
        return this.data.hasData(type);
    }

    /**
     * Set the event trigger.
     *
     * @param eventTrigger The event trigger
     */
    public void setEventTrigger(String eventTrigger) {
        this.eventTrigger = eventTrigger;
    }

    /**
     * Get the event trigger.
     *
     * @return The event trigger
     */
    @Nullable
    public String getEventTrigger() {
        return eventTrigger;
    }

    /**
     * Get the id of the request that initiated this flow
     *
     * @return The id of the request
     */
    @Nullable
    public String getOriginatingRequestId() {
        return originatingRequestId;
    }

    /**
     * Set the id originating request that initiated this flow
     *
     * @param originatingRequestId The id
     */
    public void setOriginatingRequestId(String originatingRequestId) {
        this.originatingRequestId = originatingRequestId;
    }

    /**
     * Get the target. i.e. where this event should be sent
     *
     * @return The target
     */
    @Nullable
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target i.e. where this event should be sent to
     *
     * @param target The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowEvent flowEvent = (FlowEvent) o;
        return type.equals(flowEvent.type) &&
                data.equals(flowEvent.data) &&
                Objects.equals(eventTrigger, flowEvent.eventTrigger) &&
                Objects.equals(originatingRequestId, flowEvent.originatingRequestId) &&
                Objects.equals(target, flowEvent.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data, eventTrigger, originatingRequestId, target);
    }

    @Override
    public String toString() {
        return "FlowEvent{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", eventTrigger='" + eventTrigger + '\'' +
                ", originatingRequestId='" + originatingRequestId + '\'' +
                ", target='" + target + '\'' +
                '}';
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowEvent fromJson(String json) {
        return JsonConverter.deserialize(json, FlowEvent.class);
    }
}
