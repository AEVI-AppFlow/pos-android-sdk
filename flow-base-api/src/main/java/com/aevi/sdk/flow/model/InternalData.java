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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wrapper for any internal data (as in data not passed to external apps) that may need to be passed.
 */
public class InternalData implements Jsonable {

    private final String senderApiVersion;
    private String senderPackageName;
    private final Map<String, String> additionalData = new ConcurrentHashMap<>();

    public InternalData(String senderApiVersion) {
        this.senderApiVersion = senderApiVersion;
    }

    @NonNull
    public String getSenderApiVersion() {
        return senderApiVersion;
    }

    public String getSenderPackageName() {
        return senderPackageName;
    }

    public void setSenderPackageName(String senderPackageName) {
        this.senderPackageName = senderPackageName;
    }

    public void addAdditionalData(String key, String value) {
        additionalData.put(key, value);
    }

    @NonNull
    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    @Nullable
    public String getAdditionalDataValue(String key, String fallback) {
        String value = additionalData.get(key);
        return value != null ? value : fallback;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static InternalData fromJson(String json) {
        return JsonConverter.deserialize(json, InternalData.class);
    }
}