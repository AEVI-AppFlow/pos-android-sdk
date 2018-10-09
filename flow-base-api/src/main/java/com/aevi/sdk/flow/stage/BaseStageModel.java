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
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.ActivityEvents;
import com.aevi.sdk.flow.service.BaseApiService;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Base model for all stage models that provide the stage specific data functions.
 */
public abstract class BaseStageModel {

    private WeakReference<Activity> activityReference;
    private WeakReference<BaseApiService> serviceReference;
    private String clientMessageId;

    /**
     * Initialise the stage model
     *
     * @param activity        The activity, if initialised from an activity
     * @param service         The service, if initialised from a service
     * @param clientMessageId The client message id
     */
    protected BaseStageModel(@Nullable Activity activity, @Nullable BaseApiService service, @NonNull String clientMessageId) {
        if (activity != null) {
            this.activityReference = new WeakReference<>(activity);
            if (activity instanceof LifecycleOwner) {
                registerForFinishRequest(activity.getIntent(), ((LifecycleOwner) activity).getLifecycle());
            }
        }
        if (service != null) {
            this.serviceReference = new WeakReference<>(service);
        }
        this.clientMessageId = clientMessageId;
    }

    /**
     * Get the client message id.
     *
     * @return The client message id
     */
    public String getClientMessageId() {
        return clientMessageId;
    }

    /**
     * Do send the response back to the calling client.
     *
     * @param response The response
     */
    protected void doSendResponse(String response) {
        Activity activity = getActivity();
        BaseApiService service = getService();
        if (activity != null) {
            try {
                ObservableActivityHelper<String> helper = ObservableActivityHelper.getInstance(activity.getIntent());
                helper.publishResponse(response);
            } catch (NoSuchInstanceException e) {
                Log.e(getClass().getSimpleName(), "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
            }
        } else if (service != null) {
            service.finishWithResponse(clientMessageId, response);
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
     * Can be called from a service context to start up an activity to handle the current stage with associated user interface.
     *
     * @param activityCls The activity class to start
     */
    public void processInActivity(Class<? extends Activity> activityCls) {
        BaseApiService service = getService();
        if (service != null) {
            Intent intent = new Intent(service, activityCls);
            launchActivity(service, intent, clientMessageId, getRequestJson());
        }
    }

    /**
     * Can be called from a service context to start up an activity to handle the current stage with associated user interface.
     *
     * @param activityComponentName The component name for the activity to start
     */
    public void processInActivity(ComponentName activityComponentName) {
        BaseApiService service = getService();
        if (service != null) {
            Intent intent = new Intent();
            intent.setComponent(activityComponentName);
            launchActivity(service, intent, clientMessageId, getRequestJson());
        }
    }

    private void launchActivity(final BaseApiService service, Intent intent, final String clientMessageId, final String request) {
        service.launchActivity(intent, clientMessageId, request, null);
    }

    protected abstract String getRequestJson();

    protected BaseApiService getService() {
        if (serviceReference != null) {
            return serviceReference.get();
        }
        return null;
    }

    protected Activity getActivity() {
        if (activityReference != null) {
            return activityReference.get();
        }
        return null;
    }

}
