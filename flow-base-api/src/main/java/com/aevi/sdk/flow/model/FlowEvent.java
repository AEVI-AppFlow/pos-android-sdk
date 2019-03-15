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


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowEvent flowEvent = (FlowEvent) o;
        return Objects.equals(type, flowEvent.type) &&
                Objects.equals(data, flowEvent.data) &&
                Objects.equals(eventTrigger, flowEvent.eventTrigger);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, data, eventTrigger);
    }

    @Override
    public String toString() {
        return "FlowEvent{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", eventTrigger='" + eventTrigger + '\'' +
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
