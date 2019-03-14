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
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.util.Preconditions;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.lang.ref.WeakReference;

import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.FINISH_IMMEDIATELY;
import static com.aevi.sdk.flow.stage.ServiceComponentDelegate.EXTRAS_INTERNAL_DATA_KEY;

/**
 * Provides activity-based implementation for stage models.
 *
 * This implementation assumes that the activity was started via the {@link ServiceComponentDelegate} and messages being sent here will
 * be proxied back via that delegate.
 */
@SuppressWarnings("unchecked")
class ActivityComponentDelegate extends AndroidComponentDelegate {

    private static final String TAG = ActivityComponentDelegate.class.getSimpleName();
    private final WeakReference<Activity> activityReference;
    private final PublishSubject<FlowEvent> flowServiceMessageSubject;
    private final InternalData responseInternalData;

    ActivityComponentDelegate(Activity activity) {
        super(ServiceComponentDelegate.getSenderInternalData(activity));
        Preconditions.checkNotNull(activity, "Activity can not be null");
        Preconditions.checkNotNull(activity.getIntent(), "Activity intent can not be null");
        this.activityReference = new WeakReference<>(activity);
        this.responseInternalData = InternalData.fromJson(activity.getIntent().getExtras().getString(EXTRAS_INTERNAL_DATA_KEY));
        this.flowServiceMessageSubject = PublishSubject.create();
        listenToMessages();
    }

    /*
    This will register the activity to receive finish requests from the service, typically when the request has timed out.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void listenToMessages() {
        try {
            Activity activity = getActivity();
            ObservableActivityHelper<String> helper = getHelperFromActivity(activity);
            if (activity instanceof LifecycleOwner) {
                helper.setLifecycle(((LifecycleOwner) activity).getLifecycle());
            }
            helper.registerForEvents().subscribe(event -> {
                FlowEvent flowEvent = FlowEvent.fromJson(event);
                switch (flowEvent.getType()) {
                    case FINISH_IMMEDIATELY:
                        Activity localActivity = getActivity();
                        if (localActivity != null) {
                            Log.i(TAG, "Force finishing activity");
                            localActivity.finish();
                        }
                        flowServiceMessageSubject.onComplete();
                        break;
                    default:
                        flowServiceMessageSubject.onNext(flowEvent);
                        break;
                }
            });
        } catch (NoSuchInstanceException e) {
            Log.e(TAG, "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
        }
    }

    @Nullable
    private Activity getActivity() {
        return activityReference.get();
    }

    @Override
    void sendMessage(AppMessage appMessage) {
        appMessage.updateInternalData(responseInternalData);
        Activity activity = getActivity();
        if (activity != null) {
            try {
                ObservableActivityHelper<AppMessage> helper = getHelperFromActivity(activity);
                helper.sendMessageToClient(appMessage);
            } catch (NoSuchInstanceException e) {
                Log.e(TAG, "Failed to retrieve ObservableActivityHelper - was the activity started correctly?");
            }
        } else {
            Log.e(TAG, "Activity reference no longer available to send message via");
        }
    }

    @Override
    Observable<FlowEvent> getFlowServiceEvents() {
        return flowServiceMessageSubject;
    }

    @Override
    ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson) {
        throw new UnsupportedOperationException("Starting a new activity from an activity is not supported");
    }

    protected ObservableActivityHelper getHelperFromActivity(Activity activity) throws NoSuchInstanceException {
        return ObservableActivityHelper.getInstance(activity.getIntent());
    }
}
