package com.aevi.sdk.flow;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.util.ApiVersionProvider;

import java.util.List;

public abstract class ApiBase {

    protected static final String PAYMENT_CONTROL_SERVICE_PACKAGE = "com.aevi.payment.pcs";
    protected static final ComponentName FLOW_PROCESSING_SERVICE_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.FlowProcessingService");
    protected static final ComponentName PAYMENT_SERVICE_INFO_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.PaymentServiceInfoProvider");
    protected static final ComponentName FLOW_SERVICE_INFO_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.FlowServiceInfoProvider");
    protected static final ComponentName DEVICE_LIST_SERVICE_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.ConnectedDevicesProvider");
    protected static final ComponentName TOKENIZE_SERVICE_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.TokenizeService");
    protected static final ComponentName REQUEST_STATUS_SERVICE_COMPONENT = new ComponentName(PAYMENT_CONTROL_SERVICE_PACKAGE, "com.aevi.payment.pcs.RequestStatusService");

    private final InternalData internalData;

    protected ApiBase(String propsFile) {
        String apiVersion = ApiVersionProvider.getApiVersion(propsFile);
        internalData = new InternalData(apiVersion);
    }

    protected InternalData getInternalData() {
        return internalData;
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
}
