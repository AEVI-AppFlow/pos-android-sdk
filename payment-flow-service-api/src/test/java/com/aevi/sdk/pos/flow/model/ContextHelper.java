package com.aevi.sdk.pos.flow.model;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextHelper {

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
}
