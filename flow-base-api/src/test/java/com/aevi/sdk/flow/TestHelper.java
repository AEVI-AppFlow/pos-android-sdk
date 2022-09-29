package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;

import androidx.test.core.app.ApplicationProvider;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowPackageManager;

public class TestHelper {

    public static void pretendServiceIsInstalled(ComponentName componentName) {
        ShadowPackageManager packageManager = Shadows.shadowOf(ApplicationProvider.getApplicationContext().getPackageManager());
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

        try {
            packageManager.addServiceIfNotPresent(componentName);
            packageManager.addIntentFilterForService(componentName, new IntentFilter());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
