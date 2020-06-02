package com.aevi.sdk.pos.flow.model.events;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * A simple message event
 */
public class ProgressMessage {

    @NonNull
    private final String messageText;

    public ProgressMessage(@NonNull String messageText) {
        this.messageText = messageText;
    }

    /**
     * @return The message
     */
    @NonNull
    public String getMessageText() {
        return messageText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgressMessage that = (ProgressMessage) o;
        return messageText.equals(that.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageText);
    }

    @Override
    public String toString() {
        return "ProgressMessage{" +
                "messageText='" + messageText + '\'' +
                '}';
    }
}
