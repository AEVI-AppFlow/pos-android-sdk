package com.aevi.sdk.flow;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.*;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public abstract class ApiBase {

    protected static final String FLOW_PROCESSING_SERVICE = "com.aevi.sdk.fps";
    protected static final ComponentName FLOW_PROCESSING_SERVICE_COMPONENT = new ComponentName(FLOW_PROCESSING_SERVICE, "com.aevi.sdk.fps.FlowProcessingService");
    protected static final ComponentName PAYMENT_SERVICE_INFO_COMPONENT = new ComponentName(FLOW_PROCESSING_SERVICE, "com.aevi.sdk.fps.PaymentServiceInfoProvider");
    protected static final ComponentName FLOW_SERVICE_INFO_COMPONENT = new ComponentName(FLOW_PROCESSING_SERVICE, "com.aevi.sdk.fps.FlowServiceInfoProvider");
    protected static final ComponentName DEVICE_LIST_SERVICE_COMPONENT = new ComponentName(FLOW_PROCESSING_SERVICE, "com.aevi.sdk.fps.ConnectedDevicesProvider");
    protected static final ComponentName REQUEST_STATUS_SERVICE_COMPONENT = new ComponentName(FLOW_PROCESSING_SERVICE, "com.aevi.sdk.fps.RequestStatusService");

    private final InternalData internalData;
    protected final Context context;

    protected ApiBase(String apiVersion, Context context) {
        internalData = new InternalData(apiVersion);
        this.context = context;
    }

    protected InternalData getInternalData() {
        return internalData;
    }

    protected Single<Response> sendGenericRequest(Request request) {
        final ObservableMessengerClient requestMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson(), getInternalData());
        return requestMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String json) throws Exception {
                        return Response.fromJson(json);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        requestMessenger.closeConnection();
                    }
                });
    }

    protected ObservableMessengerClient getMessengerClient(ComponentName componentName) {
        return new ObservableMessengerClient(context, componentName);
    }

    protected static void startFps(Context context) {
        // Start service explicitly to keep it running
        Intent intent = getIntent(FLOW_PROCESSING_SERVICE_COMPONENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    protected static Intent getIntent(ComponentName componentName) {
        Intent intent = new Intent();
        intent.setComponent(componentName);
        return intent;
    }

    public static boolean isProcessingServiceInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager
                .queryIntentServices(getIntent(FLOW_PROCESSING_SERVICE_COMPONENT), PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() == 1 && resolveInfo.get(0).serviceInfo != null;
    }

    public static String getProcessingServiceVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(FLOW_PROCESSING_SERVICE, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }
}
