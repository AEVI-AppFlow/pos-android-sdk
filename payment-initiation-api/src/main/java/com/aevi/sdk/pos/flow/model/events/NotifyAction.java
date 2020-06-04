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
