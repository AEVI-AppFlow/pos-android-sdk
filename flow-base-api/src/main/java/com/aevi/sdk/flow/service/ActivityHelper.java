package com.aevi.sdk.flow.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aevi.android.rxmessenger.MessageException;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;

import java.util.UUID;

import io.reactivex.functions.Consumer;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;
import static com.aevi.sdk.flow.constants.ActivityEvents.FINISH;

/**
 * A helper class that can be used to start an activity in flow services
 */
public class ActivityHelper {

    public static final String ACTIVITY_REQUEST_KEY = "request";

    private final ClientCommunicator clientCommunicator;
    private final Intent activityIntent;
    private final Context context;
    private final String request;
    private final Bundle extras;

    private final String activityId;

    /**
     * Helper to launch an activity with the request passed in.
     *
     * The request will be passed in the intent as a string extra with the key "request".
     *
     * @param context            The android context
     * @param activityIntent     The Intent to call for the activity that should handle the request.
     * @param clientCommunicator The communicator to send/receive client messages
     * @param request            The request model.
     * @param extras             Extras to add to the intent
     */
    public ActivityHelper(Context context, Intent activityIntent, ClientCommunicator clientCommunicator, String request, Bundle extras) {
        activityId = UUID.randomUUID().toString();
        this.context = context;
        this.activityIntent = activityIntent;
        this.clientCommunicator = clientCommunicator;
        this.request = request;
        this.extras = extras;
    }

    /**
     * Can be called from a service context to start up an activity to handle the current stage with associated user interface.
     *
     * If you are interested in receiving lifecycle events from the started activity here then you should subscribe to the returned
     * returned observable.
     *
     * @return An {@link ObservableActivityHelper} object that can be used to get an observable stream of events for the lifecycle of this activity
     */
    public ObservableActivityHelper<String> launchActivity() {
        activityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (request != null) {
            activityIntent.putExtra(ACTIVITY_REQUEST_KEY, request);
        }
        activityIntent.putExtra(ObservableActivityHelper.INTENT_ID, activityId);
        if (extras != null) {
            activityIntent.putExtras(extras);
        }
        ObservableActivityHelper<String> helper = ObservableActivityHelper.createInstance(context, activityIntent);
        helper.startObservableActivity().subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String response) throws Exception {
                clientCommunicator.sendResponseAndEnd(response);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                handleActivityException(throwable, clientCommunicator);
            }
        });
        return helper;
    }

    /**
     * Can be overriden to handle when the activity responds with an error / exception.
     */
    private void handleActivityException(Throwable throwable, ClientCommunicator clientCommunicator) {
        if (throwable instanceof MessageException) {
            MessageException me = (MessageException) throwable;
            clientCommunicator.send(me);
        } else {
            clientCommunicator.sendResponseAsErrorAndEnd(throwable.getMessage());
        }
    }

    /**
     * Finish an activity launched via {@link #launchActivity()}.
     *
     * Note that the activity must have subscribed via ObservableActivityHelper.registerForEvents().
     */
    public void finishLaunchedActivity() {
        try {
            ObservableActivityHelper<String> helper = ObservableActivityHelper.getInstance(activityId);
            helper.sendEventToActivity(FINISH);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
    }
}
