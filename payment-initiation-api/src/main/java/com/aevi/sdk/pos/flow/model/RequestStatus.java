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

package com.aevi.sdk.pos.flow.model;

import android.support.annotation.NonNull;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.SendableId;

/**
 * This object represents the current request status of a request object currently being processed by the flow processing service
 */
public class RequestStatus extends SendableId {

    private final String status;

    public RequestStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieve the current request status in raw string form.
     *
     * @return String representation of the current status of the request
     */
    @NonNull
    public String getStatus() {
        return status;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static RequestStatus fromJson(String json) {
        return JsonConverter.deserialize(json, RequestStatus.class);
    }
}
