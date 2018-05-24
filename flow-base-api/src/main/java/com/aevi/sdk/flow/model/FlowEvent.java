package com.aevi.sdk.flow.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Objects;

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

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public AdditionalData getData() {
        return data;
    }

    public void setEventTrigger(String eventTrigger) {
        this.eventTrigger = eventTrigger;
    }

    @Nullable
    public String getEventTrigger() {
        return eventTrigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
