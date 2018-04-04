package com.aevi.sdk.pos.flow.flowservicesample.settings;


import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.aevi.sdk.pos.flow.flowservicesample.FlowServiceInfoProvider;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.flowservicesample.service.*;
import com.aevi.sdk.pos.flow.model.PaymentStage;

import static android.content.pm.PackageManager.*;

public class ServiceStateHandler {

    public static void enableDisableService(Context context, String preferenceKey, boolean enable) {
        PackageManager packageManager = context.getPackageManager();
        int enableDisableFlag = enable ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(getComponentFromPreferenceKey(context, preferenceKey), enableDisableFlag, DONT_KILL_APP);

        FlowServiceInfoProvider.notifyServiceInfoChange(context);
    }

    public static boolean isStageEnabled(Context context, PaymentStage paymentStage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (paymentStage) {
            case PRE_FLOW:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_preflow), context.getResources().getBoolean(R.bool.pre_flow_default));
            case SPLIT:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_split), context.getResources().getBoolean(R.bool.split_default));
            case PRE_TRANSACTION:
            case POST_CARD_READING:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_prepayment), context.getResources().getBoolean(R.bool.pre_payment_default));
            case POST_TRANSACTION:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_postpayment), context.getResources().getBoolean(R.bool.post_payment_default));
            case POST_FLOW:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_postflow), context.getResources().getBoolean(R.bool.post_flow_default));
            default:
                return false;
        }
    }

    private static ComponentName getComponentFromPreferenceKey(Context context, String preferenceKey) {

        String service = null;
        if (preferenceKey.equals(context.getString(R.string.pref_preflow))) {
            service = PreFlowService.class.getName();
        } else if (preferenceKey.equals(context.getString(R.string.pref_split))) {
            service = SplitService.class.getName();
        } else if (preferenceKey.equals(context.getString(R.string.pref_prepayment))) {
            service = PrePaymentService.class.getName();
        } else if (preferenceKey.equals(context.getString(R.string.pref_postpayment))) {
            service = PostPaymentService.class.getName();
        } else if (preferenceKey.equals(context.getString(R.string.pref_postflow))) {
            service = PostFlowService.class.getName();
        }

        return new ComponentName(context.getPackageName(), service);
    }
}
