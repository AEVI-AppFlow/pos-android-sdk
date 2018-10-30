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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.ActivityEvents;
import com.aevi.sdk.flow.service.ActivityHelper;
import com.aevi.sdk.flow.service.ClientCommunicator;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Base model for all stage models that provide the stage specific data functions.
 */
public abstract class BaseStageModel {

    private WeakReference<Activity> activityReference;
    protected ClientCommunicator clientCommunicator;

    /**
     * Initialise the stage model from an activity.
     *
     * @param activity The activity initialised from
     */
    protected BaseStageModel(@Nullable Activity activity) {
        if (activity != null) {
            this.activityReference = new WeakReference<>(activity);
            if (activity instanceof LifecycleOwner) {
                registerForFinishRequest(activity.getIntent(), ((LifecycleOwner) activity).getLifecycle());
            }
        }
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
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String event) throws Exception {
                                if (event.equals(ActivityEvents.FINISH)) {
                                    Activity activity = getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            }
                        });
            }
        } catch (NoSuchInstanceException e) {
            Log.e(getClass().getSimpleName(), "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
        }
    }

    /**
     * Initialise the stage model from a service.
     *
     * @param clientCommunicator The client communication channel for this model
     */
    protected BaseStageModel(@NonNull ClientCommunicator clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public abstract void sendResponse();

    /**
     * Do send the response back to the calling client.
     *
     * @param response The response
     */
    protected void doSendResponse(String response) {
        Activity activity = getActivity();
        if (activity != null) {
            try {
                ObservableActivityHelper<String> helper = ObservableActivityHelper.getInstance(activity.getIntent());
                helper.publishResponse(response);
            } catch (NoSuchInstanceException e) {
                Log.e(getClass().getSimpleName(), "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
            }
        } else {
            clientCommunicator.sendResponseAndEnd(response);
        }
    }

    public abstract String getRequestJson();

    protected Activity getActivity() {
        if (activityReference != null) {
            return activityReference.get();
        }
        return null;
    }

    /**
     * Send this model and its associated request to be processed by an activity.
     *
     * @param context       The Android context
     * @param activityClass The class of the activity to send it to
     * @return An Observable stream of lifecycle events for the activity
     */
    public ObservableActivityHelper<String> processInActivity(Context context, Class<? extends Activity> activityClass) {
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
    public ObservableActivityHelper<String> processInActivity(Context context, Intent activityIntent, String requestJson) {
        ActivityHelper activityHelper =
                new ActivityHelper(context, activityIntent, clientCommunicator, requestJson, null);
        if (clientCommunicator != null) {
            clientCommunicator.addActivityHelper(activityHelper);
        }
        return activityHelper.launchActivity();
    }
}