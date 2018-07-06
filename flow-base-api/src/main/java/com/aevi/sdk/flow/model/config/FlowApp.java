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


package com.aevi.sdk.flow.model.config;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * Represents a flow application in a flow configuration.
 */
public class FlowApp implements Jsonable {

    private final String id;

    public FlowApp(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlowApp flowApp = (FlowApp) o;

        return id != null ? id.equals(flowApp.id) : flowApp.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
