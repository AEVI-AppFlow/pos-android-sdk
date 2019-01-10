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

package com.aevi.sdk.flow.stage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.ActivityEvents;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AuditEntry;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ActivityHelper;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.util.Preconditions;

import java.lang.ref.WeakReference;

import static com.aevi.sdk.flow.constants.AppMessageTypes.AUDIT_ENTRY;
import static com.aevi.sdk.flow.constants.AppMessageTypes.RESPONSE_MESSAGE;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;
import static com.aevi.sdk.flow.service.ActivityHelper.EXTRAS_INTERNAL_DATA_KEY;

/**
 * Base model for all stage models that provide the stage specific data functions.
 */
public abstract class BaseStageModel {

    private static final int MAX_AUDIT_ENTRIES = 5;

    private final WeakReference<Activity> activityReference;
    protected final ClientCommunicator clientCommunicator;
    private final InternalData responseInternalData;
    private int auditEntryCount;

    /**
     * Initialise the stage model from an activity.
     *
     * @param activity The flow service activity
     */
    protected BaseStageModel(@Nullable Activity activity) {
        Preconditions.checkNotNull(activity, "Activity can not be null");
        this.activityReference = new WeakReference<>(activity);
        if (activity instanceof LifecycleOwner) {
            registerForFinishRequest(activity.getIntent(), ((LifecycleOwner) activity).getLifecycle());
        }
        this.responseInternalData = InternalData.fromJson(activity.getIntent().getExtras().getString(ActivityHelper.EXTRAS_INTERNAL_DATA_KEY));
        this.clientCommunicator = null;
    }

    /**
     * Initialise the stage model from a service.
     *
     * @param clientCommunicator The client communication channel for this model
     */
    protected BaseStageModel(@NonNull ClientCommunicator clientCommunicator) {
        Preconditions.checkNotNull(clientCommunicator, "clientCommunicator can not be null");
        this.clientCommunicator = clientCommunicator;
        this.responseInternalData = clientCommunicator.getResponseInternalData();
        this.activityReference = null;
    }

    /*
    This will register the activity to receive finish requests from the service, typically when the request has timed out.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void registerForFinishRequest(Intent intent, Lifecycle lifecycle) {
        try {
            if (intent != null) {
                ObservableActivityHelper<String> helper = ObservableActivityHelper.getInstance(intent);
                helper.registerForEvents(lifecycle)
                        .subscribe(event -> {
                            if (event.equals(ActivityEvents.FINISH)) {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.finish();
                                }
                            }
                        });
            }
        } catch (NoSuchInstanceException e) {
            Log.e(getClass().getSimpleName(), "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
        }
    }

    /**
     * Send the response back to the calling client.
     *
     * @param response The response
     */
    protected void doSendResponse(String response) {
        sendMessage(RESPONSE_MESSAGE, response);
    }

    private void sendMessage(String messageType, String messageData) {
        AppMessage appMessage = new AppMessage(messageType, messageData, responseInternalData);
        Activity activity = getActivity();
        if (activity != null) {
            try {
                ObservableActivityHelper<AppMessage> helper = ObservableActivityHelper.getInstance(activity.getIntent());
                helper.sendMessageToClient(appMessage);
                if (messageType.equals(RESPONSE_MESSAGE)) {
                    helper.completeStream();
                }
            } catch (NoSuchInstanceException e) {
                Log.e(getClass().getSimpleName(), "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
            }
        } else if (clientCommunicator != null) {
            clientCommunicator.sendMessage(appMessage);
        } else {
            Log.e(getClass().getSimpleName(), "Failed to find a mechanism to send back response via");
        }
    }

    protected void sendEmptyResponse() {
        doSendResponse(EMPTY_DATA);
    }

    @NonNull
    public abstract String getRequestJson();

    @Nullable
    protected Activity getActivity() {
        return activityReference != null ? activityReference.get() : null;
    }

    /**
     * Send this model and its associated request to be processed by an activity.
     *
     * @param context       The Android context
     * @param activityClass The class of the activity to send it to
     * @return An Observable stream of lifecycle events for the activity
     */
    @NonNull
    public ObservableActivityHelper<AppMessage> processInActivity(Context context, Class<? extends Activity> activityClass) {
        return processInActivity(context, new Intent(context, activityClass), getRequestJson());
    }

    /**
     * Send this model with the provided request data to the activity specified by the intent.
     *
     * For the majority of cases, use {@link #processInActivity(Context, Class)}. If you have a use case where you need to build the intent yourself,
     * or need to modify the request data before passing it on, this method can be used.
     *
     * @param context        The Android context
     * @param activityIntent The activity intent
     * @param requestJson    The request json (see {@link #getRequestJson()} to retrieve the input request)
     * @return An Observable stream of lifecycle events for the activity
     */
    @NonNull
    public ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson) {
        if (clientCommunicator == null) {
            throw new IllegalStateException("Can't call this method outside of service context");
        }
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_INTERNAL_DATA_KEY, responseInternalData.toJson());
        ActivityHelper activityHelper = new ActivityHelper(context, activityIntent, clientCommunicator, requestJson, extras);
        clientCommunicator.addActivityHelper(activityHelper);
        return activityHelper.launchActivity();
    }

    /**
     * Add an entry to the audit log for the active flow.
     *
     * AppFlow provides an audit log for each request/flow that is processed, which contains relevant information for reviewing what occurred
     * during the flow as well as to help troubleshooting.
     *
     * If events occur in your flow service that may be useful for the merchant (or yourself) to know about for information and troubleshooting purposes, this
     * method can be used to notify of these events.
     *
     * Note that a maximum of 5 audit entries per flow service per stage is allowed.
     *
     * The message will be truncated if it exceeds 80 characters.
     *
     * @param auditSeverity The severity of the event
     * @param message       The message (as per the String.format() rules) - maximum 80 characters
     * @param parameters    Optional parameters to use for formatting the message, as per String.format() rules
     */
    public void addAuditEntry(AuditEntry.AuditSeverity auditSeverity, String message, Object... parameters) {
        if (auditEntryCount < MAX_AUDIT_ENTRIES) {
            AuditEntry auditEntry = new AuditEntry(auditSeverity, String.format(message, parameters));
            sendMessage(AUDIT_ENTRY, auditEntry.toJson());
            auditEntryCount++;
        }
    }
}