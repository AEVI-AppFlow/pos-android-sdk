package com.aevi.sdk.flow.stage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aevi.android.rxmessenger.ChannelClient;
import com.aevi.android.rxmessenger.Channels;
import com.aevi.sdk.flow.constants.IntentActions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;

public class BackupClientCommunicatorHelper {

    private static final String TAG = BackupClientCommunicatorHelper.class.getSimpleName();

    private static final int LISTENER_TIMEOUT_SECONDS = 5;

    private final Context context;
    private final String packageName;

    public BackupClientCommunicatorHelper(Context context, String clientPackageName) {
        this.packageName = clientPackageName;
        this.context = context;
    }

    public boolean clientAppHasResponseListener() {
        Intent intent = getIntent();
        intent.setPackage(packageName);
        return isServiceAvailable(intent) != null;
    }

    public void notifyListenersWithResponse(String response) {
        Log.d(TAG, "Notifying processing service listener with response for request");
        Log.d(TAG, "Response: " + response);
        Intent intent = getIntent();
        notifyResponse(response, intent);
    }

    private Intent getIntent() {
        return new Intent(IntentActions.STAGE_MODEL_RESPONSE_ACTION);
    }

    private void notifyResponse(String response, Intent intent) {
        intent.setPackage(packageName);
        ComponentName componentName = isServiceAvailable(intent);
        if (componentName != null) {
            Log.d(TAG, "Sending response to listener in package: " + componentName.getPackageName());
            final ChannelClient omc = getChannelClient(componentName);
            omc.sendMessage(response)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .timeout(LISTENER_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .subscribe(s -> {
                                   Log.d(TAG, String.format("%s notified FPS response via backup response listener", packageName));
                                   omc.closeConnection();
                               },
                               throwable -> {
                                   Log.e(TAG, "Failed to send backup response to FPS via listener", throwable);
                                   omc.closeConnection();
                               });
        }
    }

    @NonNull
    private ChannelClient getChannelClient(ComponentName componentName) {
        return Channels.messenger(context, componentName);
    }

    @Nullable
    private ComponentName isServiceAvailable(Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 0);
        if (resolveInfos != null && !resolveInfos.isEmpty()) {
            ServiceInfo serviceResolveInfo = resolveInfos.get(0).serviceInfo;
            return getComponentName(serviceResolveInfo);
        }
        return null;
    }

    @NonNull
    private ComponentName getComponentName(ServiceInfo serviceResolveInfo) {
        return new ComponentName(serviceResolveInfo.packageName, serviceResolveInfo.name);
    }
}
