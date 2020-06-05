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
