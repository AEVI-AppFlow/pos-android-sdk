package com.aevi.sdk.flow.model.config;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FlowConfigTest {

    private FlowConfig flowConfig;

    @Before
    public void setup() {
        flowConfig = new FlowConfig("blarp", "river", 1, 2, "yes", null, null, false, false);
    }

    @Test
    public void canGetAppsForAnyStage() {
        List<FlowApp> apps = flowConfig.getAppsForStage("sausage");
        assertThat(apps).isNotNull();
        assertThat(apps).hasSize(0);
    }

    @Test
    public void canAddStages() {
        flowConfig.setApps("cauliflower", new ArrayList<FlowApp>());
        flowConfig.setApps("carrot", new ArrayList<FlowApp>());

        Set<String> stages = flowConfig.getAllStageNames();
        assertThat(stages).isNotNull();
        assertThat(stages).hasSize(2);
        assertThat(stages).contains("CAULIFLOWER", "CARROT");
    }

    @Test
    public void canAddAppsForStage() {
        List<FlowApp> apps = setupDefaultFlowApps();

        List<FlowApp> result = flowConfig.getAppsForStage("cauliflower");
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsAll(apps);
        assertThat(flowConfig.containsApp("hydrogen")).isTrue();
        assertThat(flowConfig.containsApp("oxygen")).isTrue();
        assertThat(flowConfig.containsApp("helium")).isFalse();

        List<FlowApp> result2 = flowConfig.getAppsForStage("raddish");
        assertThat(result2).isNotNull();
        assertThat(result2).hasSize(0);
    }

    @Test
    public void checkCanGetAppForStageAndWillReturnFirst() {
        setupDefaultFlowApps();

        FlowApp app = flowConfig.getFirstAppForStage("cauliflower");

        assertThat(app).isNotNull();
        assertThat(app.getId()).isEqualTo("hydrogen");
    }

    @Test
    public void checkCanGetAppForSpecifiedStage() {
        setupDefaultFlowApps();

        FlowApp app = flowConfig.getFlowApp("cauliflower", "hydrogen");

        assertThat(app).isNotNull();
        assertThat(app.getId()).isEqualTo("hydrogen");
    }

    @Test
    public void checkWontGetAppForSpecifiedStageIfInvalid() {
        setupDefaultFlowApps();

        checkAppNotReturnedFromStage("raddish");
        checkAppNotReturnedFromStage("");
        checkAppNotReturnedFromStage(null);
    }

    @Test
    public void checkWontGetAppForSpecifiedStageIfNotPresent() {
        setupDefaultFlowApps();

        checkAppNotReturned("cauliflower", "irridium");
        checkAppNotReturned("raddish", "hydrogen");
        checkAppNotReturned(null, "hydrogen");
        checkAppNotReturned("cauliflower", null);
        checkAppNotReturned("", "hydrogen");
        checkAppNotReturned("cauliflower", "");
    }

    @Test
    public void checkHasAppForSpecifiedStage() {
        setupDefaultFlowApps();

        checkHasAppForStage("cauliflower", true);
        checkHasAppForStage("burps", true);
        checkHasAppForStage("", false);
        checkHasAppForStage(null, false);
    }

    @Test
    public void checkFlags() {
        FlowConfig flowConfig1 = new FlowConfig("blarp", "river", 1, 2, "yes", null, null, true, false);
        FlowConfig flowConfig2 = new FlowConfig("blarp", "river", 1, 2, "yes", null, null, false, true);

        flowConfig1 = FlowConfig.fromJson(flowConfig1.toJson());
        flowConfig2 = FlowConfig.fromJson(flowConfig2.toJson());

        assertThat(flowConfig.shouldAllowZeroAmounts()).isFalse();
        assertThat(flowConfig.shouldProcessInBackground()).isFalse();
        assertThat(flowConfig1.shouldAllowZeroAmounts()).isFalse();
        assertThat(flowConfig1.shouldProcessInBackground()).isTrue();
        assertThat(flowConfig2.shouldAllowZeroAmounts()).isTrue();
        assertThat(flowConfig2.shouldProcessInBackground()).isFalse();
    }

    private void checkHasAppForStage(String stage, boolean expected) {
        assertThat(flowConfig.hasAppForStage(stage)).isEqualTo(expected);
    }

    @Test
    public void checkWillRemoveFlowApp() {
        setupDefaultFlowApps();

        flowConfig.setApps("cauliflower", new ArrayList<FlowApp>());

        List<FlowApp> result = flowConfig.getAppsForStage("cauliflower");
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    private void checkAppNotReturned(String stage, String appId) {
        FlowApp app = flowConfig.getFlowApp(stage, appId);
        assertThat(app).isNull();
    }

    private void checkAppNotReturnedFromStage(String stage) {
        FlowApp app = flowConfig.getFirstAppForStage(stage);
        assertThat(app).isNull();
    }

    @NonNull
    private List<FlowApp> setupDefaultFlowApps() {
        List<FlowApp> apps = new ArrayList<>();
        FlowApp app1 = getFlowApp("hydrogen");
        FlowApp app2 = getFlowApp("oxygen");
        apps.add(app1);
        apps.add(app2);

        flowConfig.setApps("cauliflower", apps);
        flowConfig.setApps("burps", apps);
        return apps;
    }

    @NonNull
    private FlowApp getFlowApp(String id) {
        return new FlowApp(id);
    }
}
