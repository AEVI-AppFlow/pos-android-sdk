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

import static com.aevi.sdk.flow.constants.ResponseMechanisms.MESSENGER_CONNECTION;

/**
 * Application message data for use between FPS and applications it calls.
 */
public class AppMessage implements Jsonable {

    public static final String EMPTY_DATA = "{}";

    private final String messageType; // See AppMessageTypes
    private final String messageData; // The message data in JSON
    private String responseMechanism; // See ResponseMechanisms
    private String internalData; // Data that may be useful for internal use, such as API version, etc

    public AppMessage(String messageType, String messageData, InternalData internalData) {
        this.messageType = messageType != null ? messageType : "N/A";
        this.messageData = messageData != null ? messageData : EMPTY_DATA;
        this.responseMechanism = MESSENGER_CONNECTION;
        setInternalData(internalData);
    }

    public AppMessage(String messageType, String messageData) {
        this(messageType, messageData, null);
    }

    public AppMessage(String messageType, InternalData internalData) {
        this(messageType, null, internalData);
    }

    public AppMessage(String messageType) {
        this(messageType, null, null);
    }

    public void setResponseMechanism(String responseMechanism) {
        this.responseMechanism = responseMechanism;
    }

    public String getResponseMechanism() {
        return responseMechanism;
    }

    /**
     * Get the message type.
     *
     * @return Message type
     */
    @NonNull
    public String getMessageType() {
        return messageType;
    }

    /**
     * Get the message data/
     *
     * @return The message data
     */
    @NonNull
    public String getMessageData() {
        return messageData;
    }

    private void setInternalData(InternalData internalData) {
        this.internalData = internalData != null ? internalData.toJson() : null;
    }

    /**
     * Update internal data.
     *
     * @param internalData Internal data
     */
    public void updateInternalData(InternalData internalData) {
        setInternalData(internalData);
    }

    /**
     * Get the internal data for this message.
     *
     * @return Internal data or null
     */
    @Nullable
    public InternalData getInternalData() {
        return internalData != null ? InternalData.fromJson(internalData) : null;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static AppMessage fromJson(String json) {
        return JsonConverter.deserialize(json, AppMessage.class);
    }
}
