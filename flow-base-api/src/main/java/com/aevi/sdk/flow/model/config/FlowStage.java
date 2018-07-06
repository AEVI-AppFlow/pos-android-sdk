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


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a flow stage in a flow configuration.
 */
public class FlowStage {

    private final String name;
    private final FlowAppType flowAppType;
    private List<FlowApp> flowApps;

    private FlowConfig innerFlow;

    public FlowStage() {
        this("", FlowAppType.NONE);
    }

    public FlowStage(String name, FlowAppType flowAppType) {
        this(name, flowAppType, null);
    }

    public FlowStage(String name, FlowAppType flowAppType, List<FlowApp> flowApps) {
        this.name = name;
        this.flowAppType = flowAppType;
        this.flowApps = flowApps != null ? flowApps : new ArrayList<FlowApp>();
    }

    public String getName() {
        return name;
    }

    public FlowAppType getFlowAppType() {
        return flowAppType;
    }

    public boolean hasInnerFlow() {
        return innerFlow != null;
    }

    public FlowConfig getInnerFlow() {
        return innerFlow;
    }

    public void setInnerRequestType(FlowConfig innerFlow) {
        this.innerFlow = innerFlow;
    }

    public List<FlowApp> getFlowApps() {
        return flowApps;
    }

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

        FlowStage that = (FlowStage) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
