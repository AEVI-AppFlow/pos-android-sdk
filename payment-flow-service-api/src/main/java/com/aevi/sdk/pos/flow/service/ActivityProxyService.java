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

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.ServiceComponentDelegate;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;

import java.util.List;

import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.FINISH_IMMEDIATELY;
import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.RESUME_USER_INTERFACE;
import static com.aevi.sdk.flow.constants.IntentActions.*;
import static com.aevi.sdk.flow.constants.InternalDataKeys.FLOW_INITIATOR;
import static com.aevi.sdk.flow.constants.InternalDataKeys.FLOW_STAGE;

/**
 * This service allows an application to proxy a request for any stage to an activity of their choice, without having to implement a custom service.
 *
 * Some of the functions in this class can be overridden by subclasses to alter the default behaviour.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/implementing-flow-services" target="_blank">Implementing Flow Services</a>
 */
@SuppressLint("Registered")
public class ActivityProxyService extends BaseApiService {

    private static final String TAG = ActivityProxyService.class.getSimpleName();
    public static final String KEY_IS_RESUMED = "isResumed";

    public ActivityProxyService() {
        super(PaymentFlowServiceApi.getApiVersion());
    }

    @Override
    protected final void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request,
                                        @Nullable InternalData senderInternalData) {
        launchActivityForStage(senderInternalData, request, clientCommunicator, false);
    }

    /*
     * Launches the activity for a stage. A subclass can override this to implement custom/conditional activity launching.
     */
    protected void launchActivityForStage(InternalData senderInternalData, String request, ClientCommunicator clientCommunicator,
                                          boolean isResume) {
        String flowStage = getInternalData(senderInternalData, FLOW_STAGE);
        if (flowStage.equals(FlowStages.STATUS_UPDATE)) {
            Log.e(TAG, "Status update stage must be handled in a service context only - ignoring stage for: " + getPackageName());
            clientCommunicator.finishWithNoResponse();
            return;
        }
        Intent activityIntent = getActivityIntent(flowStage);
        activityIntent.putExtra(KEY_IS_RESUMED, isResume);
        if (!isActivityDefined(activityIntent)) {
            Log.e(TAG, "No activity defined to handle: " + activityIntent.getAction()
                    + " in app: " + getPackageName() + "! Finishing with no response");
            clientCommunicator.finishWithNoResponse();
            return;
        }
        ServiceComponentDelegate serviceComponentDelegate = new ServiceComponentDelegate(clientCommunicator, senderInternalData);
        serviceComponentDelegate.processInActivity(getBaseContext(), activityIntent, request);
        serviceComponentDelegate.getFlowServiceEvents().subscribe(event -> {
            switch (event.getType()) {
                case FINISH_IMMEDIATELY:
                    // No-op, the delegate will proxy to activity
                    break;
                case RESUME_USER_INTERFACE:
                    resumeActivity(senderInternalData, request, clientCommunicator);
                    break;
                default:
                    // Anything else, we just proxy through to the activity
                    serviceComponentDelegate.sendEventToActivity(event);
                    break;
            }
        });

    }

    /*
     * The default method for "resuming" the activity is simply restarting it, as we can not rely on any particular manifest flags, activity lifecycle
     * implementations, etc. Subclasses can override this to implement more sophisticated behaviour.
     */
    protected void resumeActivity(InternalData senderInternalData, String request, ClientCommunicator clientCommunicator) {
        launchActivityForStage(senderInternalData, request, clientCommunicator, true);
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
