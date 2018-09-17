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


import com.aevi.util.json.JsonConverter;

/**
 * Represents a device connected to the merchant device.
 */
public class Device extends BaseIdNameEntity {

    public Device(String id, String name) {
        super(id, name);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Device fromJson(String json) {
        return JsonConverter.deserialize(json, Device.class);
    }
}
