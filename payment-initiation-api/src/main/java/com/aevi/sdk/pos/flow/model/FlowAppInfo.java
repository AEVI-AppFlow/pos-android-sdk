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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about a "flow app" that was executed as part of a transaction.
 */
public class FlowAppInfo {

    private final String flowAppId;
    private final String stage;
    private final List<String> augmentedData;

    /**
     * Create a new FlowAppInfo instance with augmented data information.
     *
     * @param flowAppId     The flow application id (as per {@link PaymentFlowServiceInfo})
     * @param stage         The stage at which the flow app is called
     * @param augmentedData The list of augmented data
     */
    public FlowAppInfo(String flowAppId, String stage, List<String> augmentedData) {
        this.flowAppId = flowAppId;
        this.stage = stage;
        this.augmentedData = augmentedData;
    }

    /**
     * Create a new FlowAppInfo instance without any augmented data.
     *
     * @param flowAppId The flow application name
     * @param stage     The stage at which the flow app is called
     */
    public FlowAppInfo(String flowAppId, String stage) {
        this(flowAppId, stage, new ArrayList<>());
    }

    /**
     * Get the flow application id, as per {@link PaymentFlowServiceInfo}.
     *
     * @return The flow application id
     */
    @NonNull
    public String getFlowAppId() {
        return flowAppId;
    }

    /**
     * Get the stage for which this flow application executed in.
     *
     * @return The flow stage
     */
    @NonNull
    public String getStage() {
        return stage;
    }

    /**
     * Get the list of data this flow app augmented as part of its execution.
     *
     * Note that this simply indicates what was augmented - not to what. The {@link PaymentResponse} can be parsed to review that information.
     *
     * @return The list of data that was augmented.
     */
    @NonNull
    public List<String> getAugmentedData() {
        return augmentedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlowAppInfo that = (FlowAppInfo) o;

        if (flowAppId != null ? !flowAppId.equals(that.flowAppId) : that.flowAppId != null) {
            return false;
        }
        if (stage != null ? !stage.equals(that.stage) : that.stage != null) {
            return false;
        }
        return augmentedData != null ? augmentedData.equals(that.augmentedData) : that.augmentedData == null;
    }

    @Override
    public int hashCode() {
        int result = flowAppId != null ? flowAppId.hashCode() : 0;
        result = 31 * result + (stage != null ? stage.hashCode() : 0);
        result = 31 * result + (augmentedData != null ? augmentedData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FlowAppInfo{" +
                "flowAppId='" + flowAppId + '\'' +
                ", stage=" + stage +
                ", augmentedData=" + augmentedData +
                '}';
    }
}
