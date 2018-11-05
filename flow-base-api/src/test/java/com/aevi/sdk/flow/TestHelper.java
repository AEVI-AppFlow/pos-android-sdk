package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.res.builder.RobolectricPackageManager;

import static org.robolectric.Shadows.shadowOf;

public class TestHelper {

    public static void pretendServiceIsInstalled(ComponentName componentName) {
        RobolectricPackageManager packageManager = shadowOf(RuntimeEnvironment.application.getPackageManager());
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
