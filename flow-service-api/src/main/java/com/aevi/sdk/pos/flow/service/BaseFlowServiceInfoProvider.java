package com.aevi.sdk.pos.flow.service;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.flow.service.BaseServiceInfoProvider;

/**
 * Base class for flow services to provide {@link FlowServiceInfo} information.
 */
public abstract class BaseFlowServiceInfoProvider extends BaseServiceInfoProvider {

    public static final String ACTION_BROADCAST_FLOW_INFO_CHANGE = "com.aevi.intent.action.FLOW_SERVICE_INFO_CHANGE";

    protected BaseFlowServiceInfoProvider() {
        super(ACTION_BROADCAST_FLOW_INFO_CHANGE);
    }

    @Override
    protected String getServiceInfo() {
        return getFlowServiceInfo().toJson();
    }

    protected abstract FlowServiceInfo getFlowServiceInfo();

    /**
     * Notify the system that the configuration has changed.
     *
     * @param context The Android context
     */
    public static void notifyServiceInfoChange(Context context) {
        String pkg = "package:" + context.getPackageName();
        Uri pkgUri = Uri.parse(pkg);
        context.sendBroadcast(new Intent(ACTION_BROADCAST_FLOW_INFO_CHANGE).setData(pkgUri));
    }
}
