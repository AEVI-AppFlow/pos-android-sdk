/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.pos.flow.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.flow.service.ActivityHelper;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;

import java.util.List;

import static com.aevi.sdk.flow.constants.IntentActions.*;

/**
 * This service allows an application to proxy a request for any stage to an activity of their choice, without having to implement a custom service.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/implementing-flow-services" target="_blank">Implementing Flow Services</a>
 */
public class ActivityProxyService extends BaseApiService {

    private static final String TAG = ActivityProxyService.class.getSimpleName();

    public ActivityProxyService() {
        super(PaymentFlowServiceApi.getApiVersion());
    }

    @Override
    protected void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request, @NonNull String flowStage) {
        if (flowStage.equals(FlowStages.STATUS_UPDATE)) {
            Log.e(TAG, "Status update stage must be handled in a service context only - ignoring stage for: " + getPackageName());
            clientCommunicator.finishWithNoResponse();
            return;
        }
        Intent activityIntent = getActivityIntent(flowStage);
        if (!isActivityDefined(activityIntent)) {
            Log.e(TAG,
                  "No activity defined to handle: " + activityIntent.getAction() + " in app: " + getPackageName() + "! Finishing with no response");
            clientCommunicator.finishWithNoResponse();
            return;
        }
        ActivityHelper activityHelper = new ActivityHelper(getBaseContext(), activityIntent, clientCommunicator, request, null);
        clientCommunicator.addActivityHelper(activityHelper);
        activityHelper.launchActivity();
    }

    private Intent getActivityIntent(String flowStage) {
        String intentAction = BASE_INTENT_ACTION + ACTIVITY_PROXY_ACTION_PREFIX + flowStage + ACTIVITY_PROXY_ACTION_POSTFIX;
        Intent intent = new Intent(intentAction);
        intent.setPackage(this.getPackageName());
        return intent;
    }

    private boolean isActivityDefined(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        if (resolveInfos.isEmpty()) {
            return false;
        }
        intent.setComponent(new ComponentName(resolveInfos.get(0).activityInfo.packageName, resolveInfos.get(0).activityInfo.name));
        return true;
    }

}
