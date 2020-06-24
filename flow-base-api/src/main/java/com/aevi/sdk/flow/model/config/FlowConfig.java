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
import android.text.TextUtils;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.JsonPostProcessing;
import com.aevi.util.json.Jsonable;

import java.util.*;

/**
 * Represents a flow configuration consisting of a series of stages with associated stage rules and applications.
 *
 * Use {@link FlowConfigBuilder} to instantiate programmatically.
 */
public class FlowConfig implements Jsonable, JsonPostProcessing {

    public static final String REQUEST_CLASS_GENERIC = "generic";
    public static final String REQUEST_CLASS_PAYMENT = "payment";

    private final String name;
    private final String type;
    private final int version;
    private final int apiMajorVersion;
    private final String description;
    private final String restrictedToApp;
    private final List<FlowStage> stages;
    private final boolean processInBackground;
    private final boolean allowZeroAmounts;
    private boolean generatedFromCustomType;

    private transient List<FlowStage> allStagesFlattened;
    private transient Map<String, FlowStage> allStagesMap;

    FlowConfig() {
        this("N/A", "N/A", 0, 0, null, null, null, false, false);
    }

    public FlowConfig(String name, String type, int version, int apiMajorVersion, String description, String restrictedToApp,
                      List<FlowStage> stages, boolean processInBackground, boolean allowZeroAmounts) {
        this.name = name;
        this.type = type;
        this.version = version;
        this.apiMajorVersion = apiMajorVersion;
        this.description = description;
        this.restrictedToApp = restrictedToApp;
        this.stages = stages != null ? stages : new ArrayList<>();
        this.processInBackground = processInBackground;
        this.allowZeroAmounts = allowZeroAmounts;
        parseStageHierarchy();
    }

    private synchronized void parseStageHierarchy() {
        this.allStagesFlattened = new ArrayList<>();
        this.allStagesMap = new HashMap<>();
        getDeepStages(allStagesFlattened, allStagesMap, stages);
    }

    /**
     * Get the name of this flow.
     *
     * The name is a unique identifier for a flow and is used to indicate what flow should be used when initiating a request.
     *
     * @return The name of the flow
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Get the type of this flow.
     *
     * The flow type represents what function the flow fills - such as a sale/purchase or tokenisation.
     *
     * There is a set of defined types via the documentation. In addition, flow services can support custom types.
     *
     * @return The type of this flow
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * Get the version counter of this flow.
     *
     * This is a simple counter that gets bumped every time the flow is modified.
     *
     * @return The version counter
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get the API major version this flow is compatible with.
     *
     * Flow configs may be incompatible between major versions, so this is used as a mechanism to ensure compatibility.
     *
     * @return The API major version this flow is compatible with
     */
    public int getApiMajorVersion() {
        return apiMajorVersion;
    }

    /**
     * Get the description of this flow.
     *
     * A flow may optionally contain a description for the flow.
     *
     * @return The description, or null if none set
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Check whether this flow has an app restriction defined or not.
     *
     * If there is an app restriction defined, this can be used to filter out configurations that should only be allowed for a certain client application.
     *
     * @return True if there is a filter, false otherwise
     */
    public boolean hasAppRestriction() {
        return !TextUtils.isEmpty(restrictedToApp);
    }

    /**
     * Verify whether a client app is allowed to read/initiate this flow configuration.
     *
     * @param clientPackageName The package name of the client application
     * @return True if the client app is allowed, false otherwise
     */
    public boolean isClientAppAllowed(String clientPackageName) {
        return !hasAppRestriction() || restrictedToApp.equals(clientPackageName);
    }

    /**
     * Get the stages for this flow.
     *
     * As stages can be nested (a stage can have an inner flow), the returned list can either be top level only or all stages flattened.
     *
     * @param flattened Set to true to get all the stages (top level and nested) in the returned list, or false if just to get top level
     * @return The stages for this flow
     */
    public List<FlowStage> getStages(boolean flattened) {
        return flattened ? allStagesFlattened : stages;
    }

    /**
     * Get the request class for this flow, which indicates what type of request to use with it.
     *
     * If this flow is to be used by a {@link com.aevi.sdk.flow.model.Request}, then the return value will be {@link #REQUEST_CLASS_GENERIC}
     *
     * If this flow is to be used for a payment initiation, then the return value will be {@link #REQUEST_CLASS_PAYMENT}
     *
     * @return The request class for this flow
     */
    public String getRequestClass() {
        return allStagesMap.keySet().contains(normaliseStageName(FlowStages.TRANSACTION_PROCESSING)) ? REQUEST_CLASS_PAYMENT : REQUEST_CLASS_GENERIC;
    }

    /**
     * Determines whether this flow will be processed as a background flow (invisible) or foreground flow (with UI)
     *
     * Default is false - foreground flow.
     *
     * @return True if background flow, false if foreground flow
     */
    public boolean shouldProcessInBackground() {
        return processInBackground;
    }

    /**
     * Determines whether zero amounts are allowed to be passed to flow services in a flow.
     *
     * Specifically this is usually sending a transaction request to the card reading and transaction
     * processing flow service when the amount remaining is zero or the requested amount was zero at initiation.
     *
     * Default is false which indicates the flow services will be skipped when all amounts are fulfilled or zero
     *
     * @return True if zero amounts should be passed to flow services. Otherwise the service will be skipped when amount left to process is zero
     */
    public boolean shouldAllowZeroAmounts() {
        return allowZeroAmounts;
    }

    /**
     * Check whether this flow config was generated from a custom type as defined by a flow service.
     *
     * @return True if generated from custom type, false otherwise
     */
    public boolean isGeneratedFromCustomType() {
        return generatedFromCustomType;
    }

    /**
     * For internal use
     *
     * @param generatedFromCustomType Custom type indicator
     */
    public void setGeneratedFromCustomType(boolean generatedFromCustomType) {
        this.generatedFromCustomType = generatedFromCustomType;
    }

    public synchronized Set<String> getAllStageNames() {
        return allStagesMap.keySet();
    }

    private synchronized void getDeepStages(List<FlowStage> allStages, Map<String, FlowStage> allStagesMap, List<FlowStage> toAdd) {
        if (toAdd != null) {
            for (FlowStage stage : toAdd) {
                allStages.add(stage);
                allStagesMap.put(normaliseStageName(stage.getName()), stage);
                if (stage.hasInnerFlow()) {
                    getDeepStages(allStages, allStagesMap, stage.getInnerFlow().getStages(false));
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
        for (FlowStage stage : getStages(true)) {
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
            return stage.toUpperCase();
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

    @Override
    public void onJsonDeserialisationCompleted() {
        parseStageHierarchy();
    }

    public static FlowConfig fromJson(String json) {
        return JsonConverter.deserialize(json, FlowConfig.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowConfig that = (FlowConfig) o;
        return version == that.version &&
                apiMajorVersion == that.apiMajorVersion &&
                processInBackground == that.processInBackground &&
                generatedFromCustomType == that.generatedFromCustomType &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description) &&
                Objects.equals(restrictedToApp, that.restrictedToApp) &&
                Objects.equals(stages, that.stages) &&
                Objects.equals(allStagesFlattened, that.allStagesFlattened) &&
                Objects.equals(allStagesMap, that.allStagesMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, version, apiMajorVersion, description, restrictedToApp, stages, processInBackground, generatedFromCustomType,
                            allStagesFlattened, allStagesMap);
    }
}
