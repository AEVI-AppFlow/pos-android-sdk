package com.aevi.sdk.flow.constants;

public interface EventTypes {

    /**
     * When anything has changed in FPS related to settings, configurations, etc, this event will be triggered.
     * The data for the event will be populated with the relevant FPS state.
     * See {@link EventDataKeys} for breakdown of what has changed
     */
    String EVENT_INTERNAL_STATE_CHANGED = "flowStateChanged";

    /**
     * When something external has changed, like devices connected/disconnected or apps added/removed/enabled/disabled, etc
     * See {@link EventDataKeys} for breakdown of what has changed
     */
    String EVENT_EXTERNAL_STATE_CHANGED = "externalStateChanged";
}
