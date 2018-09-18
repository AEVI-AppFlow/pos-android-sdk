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

import android.text.TextUtils;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.*;

/**
 * Represents a flow configuration consisting of a series of stages with associated stage rules and applications.
 */
public class FlowConfig implements Jsonable {

    public static final String TYPE_GENERIC = "generic";
    public static final String TYPE_GENERIC_ADHOC = "genericAdhoc";

    private final String name;
    private final String type;
    private final String description;
    private final String appFilter;
    private final List<FlowStage> stages;

    private transient List<FlowStage> allStagesFlattened;
    private transient Map<String, FlowStage> allStagesMap;

    public FlowConfig() {
        this("N/A", "N/A", "N/A", null, new ArrayList<FlowStage>());
    }

    public FlowConfig(String name, String type) {
        this(name, type, "N/A", null, new ArrayList<FlowStage>());
    }

    public FlowConfig(String name, String type, List<FlowStage> stages) {
        this(name, type, "N/A", null, stages);
    }

    public FlowConfig(String name, String type, String description, String appFilter, List<FlowStage> stages) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.appFilter = appFilter;
        this.stages = stages != null ? stages : new ArrayList<FlowStage>();
        parseStageHierarchy();
    }

    private synchronized void parseStageHierarchy() {
        this.allStagesFlattened = new ArrayList<>();
        this.allStagesMap = new HashMap<>();
        getDeepStages(allStagesFlattened, allStagesMap, stages);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasAppFilter() {
        return !TextUtils.isEmpty(appFilter);
    }

    public String getAppFilter() {
        return appFilter;
    }

    public List<FlowStage> getStages() {
        return stages;
    }

    public synchronized Set<String> getAllStageNames() {
        return allStagesMap.keySet();
    }

    public synchronized List<FlowStage> getAllStages() {
        return allStagesFlattened;
    }

    private synchronized void getDeepStages(List<FlowStage> allStages, Map<String, FlowStage> allStagesMap, List<FlowStage> toAdd) {
        if (toAdd != null) {
            for (FlowStage stage : toAdd) {
                allStages.add(stage);
                allStagesMap.put(normaliseStageName(stage.getName()), stage);
                if (stage.hasInnerFlow()) {
                    getDeepStages(allStages, allStagesMap, stage.getInnerFlow().getStages());
                }
            }
        }
    }

    public synchronized FlowStage getStage(String stageName) {
        return allStagesMap.get(normaliseStageName(stageName));
    }

    public synchronized boolean hasStage(String stage) {
        return allStagesMap.containsKey(normaliseStageName(stage));
    }

    public synchronized boolean hasAppForStage(String stage) {
        stage = normaliseStageName(stage);
        return allStagesMap.containsKey(stage) && allStagesMap.get(stage).getFlowApps().size() > 0;
    }

    public synchronized boolean hasAppForStage(String appId, String stage) {
        stage = normaliseStageName(stage);
        return allStagesMap.containsKey(stage) && scanAppList(allStagesMap.get(stage).getFlowApps(), appId);
    }

    public List<FlowApp> getAppsForStage(String stageName) {
        if (hasStage(stageName)) {
            return getStage(stageName).getFlowApps();
        }
        return new ArrayList<>();
    }

    public FlowApp getFirstAppForStage(String stageName) {
        if (hasStage(stageName)) {
            List<FlowApp> apps = getAppsForStage(stageName);
            if (apps.size() > 0) {
                return apps.get(0);
            }
        }
        return null;
    }

    public boolean containsApp(String flowAppId) {
        boolean found = false;
        for (FlowStage stage : getAllStages()) {
            found |= scanAppList(stage.getFlowApps(), flowAppId);
        }
        return found;
    }

    public synchronized FlowApp getFlowApp(String stage, String appId) {
        stage = normaliseStageName(stage);
        if (allStagesMap.containsKey(stage)) {
            return findAppInList(allStagesMap.get(stage).getFlowApps(), appId);
        }
        return null;
    }

    private FlowApp findAppInList(List<FlowApp> apps, String appId) {
        if (apps != null) {
            for (FlowApp app : apps) {
                if (app.getId().equals(appId)) {
                    return app;
                }
            }
        }
        return null;
    }

    private boolean scanAppList(List<FlowApp> apps, String appId) {
        if (apps != null) {
            for (FlowApp app : apps) {
                if (app.getId().equals(appId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String normaliseStageName(String stage) {
        if (stage != null) {
            return stage.toLowerCase();
        }
        return null;
    }

    public void setApps(String stage, List<FlowApp> flowApps) {
        stage = normaliseStageName(stage);
        FlowStage flowStage = getStage(stage);
        if (flowStage == null) {
            flowStage = new FlowStage(stage, AppExecutionType.MULTIPLE);
            flowStage.setFlowApps(flowApps);
            stages.add(flowStage);
            allStagesFlattened = null;
            allStagesMap = null;
            parseStageHierarchy();
        } else {
            flowStage.setFlowApps(flowApps);
        }
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowConfig fromJson(String json) {
        FlowConfig flowConfig = JsonConverter.deserialize(json, FlowConfig.class);
        flowConfig.parseStageHierarchy();
        return flowConfig;
    }
}
