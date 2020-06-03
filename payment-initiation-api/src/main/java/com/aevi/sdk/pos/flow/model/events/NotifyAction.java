package com.aevi.sdk.pos.flow.model.events;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * A general purpose event indicating something has happened or the notification of some event
 */
public class NotifyAction {

    @NonNull
    private final String type;

    public NotifyAction(@NonNull String type) {
        this.type = type;
    }

    /**
     * @return What type of notification event this is
     */
    @NonNull
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotifyAction that = (NotifyAction) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "NotifyAction{" +
                "type='" + type + '\'' +
                '}';
    }
}
