package com.aevi.sdk.pos.flow.config.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aevi.sdk.pos.flow.config.sample.model.Channels;
import com.aevi.sdk.flow.model.config.FpsSettings;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.PublishSubject;

@Singleton
public class SettingsProvider {

    private final SharedPreferences preferences;

    private static final String KEY_FPS_SETTINGS = "fpsSettings";
    private static final String KEY_AUTO_GENERATE_CONFIGS = "autoGenerateConfigs";
    private static final String KEY_COMMS_CHANNEL = "commsChannel";

    private PublishSubject<SettingsProvider> settingsChangeSubject = PublishSubject.create();

    private FpsSettings fpsSettings;

    @Inject
    public SettingsProvider(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // settings are not optional and have defaults
        if (hasFpsSettings()) {
            fpsSettings = deserialiseFpsSettings();
        } else {
            setupDefaultFpsSettings();
        }
    }

    public boolean shouldAutoGenerateConfigs() {
        return preferences.getBoolean(KEY_AUTO_GENERATE_CONFIGS, true);
    }

    public void updateAutoGenerateConfig(boolean autoUpdate) {
        preferences.edit().putBoolean(KEY_AUTO_GENERATE_CONFIGS, autoUpdate).apply();
    }

    private void setupDefaultFpsSettings() {
        fpsSettings = new FpsSettings();
        saveFpsSettings();
    }

    private boolean hasFpsSettings() {
        return preferences.contains(KEY_FPS_SETTINGS);
    }

    public FpsSettings getFpsSettings() {
        return fpsSettings;
    }

    public int getSplitResponseTimeoutSeconds() {
        return fpsSettings.getSplitResponseTimeoutSeconds();
    }

    public void setSplitResponseTimeoutSeconds(int timeout) {
        fpsSettings.setSplitResponseTimeoutSeconds(timeout);
        saveFpsSettings();
    }

    public int getFlowResponseTimeoutSeconds() {
        return fpsSettings.getFlowResponseTimeoutSeconds();
    }

    public void setFlowResponseTimeoutSeconds(int timeout) {
        fpsSettings.setFlowResponseTimeoutSeconds(timeout);
        saveFpsSettings();
    }

    public int getPaymentResponseTimeoutSeconds() {
        return fpsSettings.getPaymentResponseTimeoutSeconds();
    }

    public void setPaymentResponseTimeoutSeconds(int timeout) {
        fpsSettings.setPaymentResponseTimeoutSeconds(timeout);
        saveFpsSettings();
    }

    public int getAppOrDeviceSelectionTimeoutSeconds() {
        return fpsSettings.getUserSelectionTimeoutSeconds();
    }

    public void setAppOrDeviceSelectionTimeoutSeconds(int timeout) {
        fpsSettings.setUserSelectionTimeoutSeconds(timeout);
        saveFpsSettings();
    }

    public void allowStatusBarAccess(Boolean allowAccess) {
        fpsSettings.setAllowAccessViaStatusBar(allowAccess);
    }

    public boolean allowAccessViaStatusBar() {
        return fpsSettings.isAccessViaStatusBarAllowed();
    }

    public boolean shouldAbortOnFlowAppError() {
        return fpsSettings.shouldAbortOnFlowAppError();
    }

    public boolean shouldAbortOnPaymentAppError() {
        return fpsSettings.shouldAbortOnPaymentAppError();
    }

    public void abortOnPaymentError(boolean abort) {
        fpsSettings.setAbortOnPaymentError(abort);
        saveFpsSettings();
    }

    public void abortOnFlowError(boolean abort) {
        fpsSettings.setAbortOnFlowError(abort);
        saveFpsSettings();
    }

    private void saveFpsSettings() {
        setString(KEY_FPS_SETTINGS, fpsSettings.toJson());
    }

    private FpsSettings deserialiseFpsSettings() {
        String val = getString(KEY_FPS_SETTINGS, "");
        if (val.isEmpty()) {
            return new FpsSettings();
        }
        return FpsSettings.fromJson(val);
    }

    public String getCommsChannel() {
        return preferences.getString(KEY_COMMS_CHANNEL, Channels.MESSENGER);
    }

    public void setCommsChannel(String channel) {
        preferences.edit().putString(KEY_COMMS_CHANNEL, channel).apply();
    }

    private String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    private void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private void notifyChange() {
        settingsChangeSubject.onNext(this);
    }

}