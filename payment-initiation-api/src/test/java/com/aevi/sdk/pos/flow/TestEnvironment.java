package com.aevi.sdk.pos.flow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.*;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowPackageManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class TestEnvironment {

    private static final ComponentName FLOW_PROCESSING_SERVICE_COMPONENT =
            new ComponentName("com.aevi.sdk.fps", "com.aevi.sdk.fps.FlowProcessingService");
    private static final String BASE_TEST_DIR = "src/test/resources/";
    private static final String VALID_RESPONSE = "valid_response.json";
    private static final String ERROR_RESPONSE = "error_response.json";

    private Reader doGetInputStream(String configFile, String basePath) throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
        // Android studio work around
        if (inputStream == null) {
            inputStream = new FileInputStream(new File(basePath, configFile));
        }
        return new InputStreamReader(inputStream);
    }

    String getValidResponseJson() throws IOException {
        return TestEnvironment.readFile(BASE_TEST_DIR + VALID_RESPONSE);
    }

    String getErrorResponseJson() throws IOException {
        return TestEnvironment.readFile(BASE_TEST_DIR + ERROR_RESPONSE);
    }

    private static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static Context mockContext(String packageName, String version) throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn(packageName);
        when(context.getPackageManager()).thenReturn(packageManager);
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionName = version;
        when(packageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo);
        return context;
    }

    public static void pretendFpsIsInstalled() {
        ComponentName componentName = FLOW_PROCESSING_SERVICE_COMPONENT;
        ShadowPackageManager packageManager = shadowOf(RuntimeEnvironment.application.getPackageManager());
        Intent intent = new Intent();
        intent.setComponent(componentName);

        ResolveInfo resolveInfo = new ResolveInfo();
        resolveInfo.isDefault = true;

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.packageName = componentName.getPackageName();
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = serviceInfo.packageName;
        serviceInfo.applicationInfo = applicationInfo;

        resolveInfo.serviceInfo = serviceInfo;

        packageManager.addResolveInfoForIntent(intent, resolveInfo);
    }

}
