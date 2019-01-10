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


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a flow stage in a flow configuration.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/flow-stages" target="_blank">Flow Stages Docs</a>
 */
public class FlowStage {

    private final String name;
    private final AppExecutionType appExecutionType;
    private List<FlowApp> flowApps;
    private FlowConfig innerFlow;

    /**
     * Initialise with default values.
     */
    public FlowStage() {
        this("", AppExecutionType.NONE);
    }

    /**
     * Initialise with name and execution type.
     *
     * @param name             The stage name
     * @param appExecutionType The stage app execution type
     */
    public FlowStage(String name, AppExecutionType appExecutionType) {
        this(name, appExecutionType, null);
    }

    /**
     * Initialise with name and execution type.
     *
     * @param name             The stage name
     * @param appExecutionType The stage app execution type
     * @param flowApps         The stage applications
     */
    public FlowStage(String name, AppExecutionType appExecutionType, List<FlowApp> flowApps) {
        this.name = name;
        this.appExecutionType = appExecutionType;
        this.flowApps = flowApps != null ? flowApps : new ArrayList<>();
    }

    /**
     * Get the name of the stage
     *
     * @return The name of the stage
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Get the app execution type for the stage
     *
     * @return The app execution type for the stage
     */
    @NonNull
    public AppExecutionType getAppExecutionType() {
        return appExecutionType;
    }

    /**
     * Check whether the stage has an inner flow.
     *
     * @return True if inner flow, false otherwise
     */
    public boolean hasInnerFlow() {
        return innerFlow != null;
    }

    /**
     * Get the inner flow for this stage.
     *
     * @return The inner flow
     */
    @Nullable
    public FlowConfig getInnerFlow() {
        return innerFlow;
    }

    /**
     * Set the inner flow for this stage.
     *
     * @param innerFlow The inner flow
     */
    public void setInnerFlow(FlowConfig innerFlow) {
        this.innerFlow = innerFlow;
    }

    /**
     * Get the flow apps defined for this stage.
     *
     * @return The flow apps defined for this stage
     */
    @NonNull
    public List<FlowApp> getFlowApps() {
        return flowApps;
    }

    /**
     * Set the flow apps defined for this stage.
     *
     * @param flowApps The flow apps defined for this stage
     */
    public void setFlowApps(List<FlowApp> flowApps) {
        if (flowApps != null) {
            this.flowApps = flowApps;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowStage flowStage = (FlowStage) o;
        return Objects.equals(name, flowStage.name) &&
                appExecutionType == flowStage.appExecutionType &&
                Objects.equals(flowApps, flowStage.flowApps) &&
                Objects.equals(innerFlow, flowStage.innerFlow);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, appExecutionType, flowApps, innerFlow);
    }
}
