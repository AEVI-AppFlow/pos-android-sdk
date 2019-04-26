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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.FlowServiceEventDataKeys;
import com.aevi.sdk.flow.constants.FlowServiceEventTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AuditEntry;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import io.reactivex.Observable;

import static com.aevi.sdk.flow.constants.AppMessageTypes.AUDIT_ENTRY;
import static com.aevi.sdk.flow.constants.AppMessageTypes.RESPONSE_MESSAGE;
import static com.aevi.sdk.flow.constants.InternalDataKeys.FLOW_INITIATOR;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;
import static com.aevi.sdk.flow.stage.ServiceComponentDelegate.ACTIVITY_REQUEST_KEY;

/**
 * Internal base class for all stage models that provide the stage specific data functions.
 *
 * A stage model can be initialised from either a service or activity context and a {@link AndroidComponentDelegate} implementation will be chosen
 * accordingly to manage the logic that is specific to the component the model was created from.
 *
 * This is an internal class not intended to be used directly by external applications. No guarantees are made of backwards compatibility and the
 * class may be removed without any warning.
 */
public abstract class BaseStageModel {

    private static final int MAX_AUDIT_ENTRIES = 5;

    private final AndroidComponentDelegate androidComponentDelegate;
    private int auditEntryCount;
    private boolean responseSent;

    /**
     * Initialise the stage model with an android component delegate directly.
     *
     * @param androidComponentDelegate The android component delegate
     */
    protected BaseStageModel(AndroidComponentDelegate androidComponentDelegate) {
        this.androidComponentDelegate = androidComponentDelegate;
    }

    /**
     * Initialise the stage model from an activity.
     *
     * @param activity The flow service activity
     */
    protected BaseStageModel(@Nullable Activity activity) {
        this(new ActivityComponentDelegate(activity));
    }

    /**
     * Initialise the stage model from a service.
     *
     * @param clientCommunicator The client communication channel for this model
     * @param senderInternalData The InternalData of the app calling this stage
     */
    protected BaseStageModel(@NonNull ClientCommunicator clientCommunicator, InternalData senderInternalData) {
        this(new ServiceComponentDelegate(clientCommunicator, senderInternalData));
    }

    @Nullable
    protected static String getActivityRequestJson(Activity activity) {
        return activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
    }

    /**
     * returns the packagename of the client application that initiated this flow in the first place
     *
     * @return A package name or "UNKNOWN" if not known for some reason
     */
    public String getFlowInitiatorPackage() {
        return getInternalData(androidComponentDelegate.getSenderInternalData(), FLOW_INITIATOR);
    }

    protected String getInternalData(@Nullable InternalData senderInternalData, String dataKey) {
        return senderInternalData != null ? senderInternalData.getAdditionalDataValue(dataKey, "UNKNOWN") : "UNKNOWN";
    }

    /**
     * Returns a stream of events relevant for this service from the flow processing service.
     *
     * It is important that your flow service listens to these events and handle them appropriately.
     *
     * See {@link FlowServiceEventTypes} for possible event types and {@link FlowServiceEventDataKeys} for keys
     * associated with event data.
     *
     * Note that by default these events are ONLY sent to a subscribed service, not activity. The exception is when using the
     * ActivityProxyService which forwards all events to the handling activity. If your service start an activity and you want to forward
     * the events, you can do so via the {@link ObservableActivityHelper} that is returned from {@link #processInActivity(Context, Class)}.
     *
     * @return a stream of {@link FlowEvent} for the flow service
     */
    public Observable<FlowEvent> getEvents() {
        return androidComponentDelegate.getFlowServiceEvents();
    }

    /**
     * Get the JSON representing the request model.
     *
     * @return the JSON representing the request model
     */
    @NonNull
    public abstract String getRequestJson();

    /**
     * Send this model and its associated request to be processed by an activity.
     *
     * Note that this is only supported when called from a service and not from an activity.
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
     * Note that this is only supported when called from a service and not from an activity.
     *
     * @param context        The Android context
     * @param activityIntent The activity intent
     * @param requestJson    The request json (see {@link #getRequestJson()} to retrieve the input request)
     * @return An Observable stream of lifecycle events for the activity
     */
    @NonNull
    public ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson) {
        return androidComponentDelegate.processInActivity(context, activityIntent, requestJson);
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

    /**
     * Send empty response.
     */
    protected final void sendEmptyResponse() {
        doSendResponse(EMPTY_DATA);
    }

    /**
     * Send the response back to the calling client.
     *
     * @param response The response
     */
    protected final void doSendResponse(String response) {
        if (responseSent) {
            throw new IllegalStateException("Response may only be sent once");
        }
        responseSent = true;
        sendMessage(RESPONSE_MESSAGE, response);
    }

    private void sendMessage(String messageType, String messageData) {
        AppMessage appMessage = new AppMessage(messageType, messageData);
        androidComponentDelegate.sendMessage(appMessage);
    }
}