package com.aevi.sdk.flow.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import io.reactivex.functions.Consumer;

import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;
import static com.aevi.sdk.flow.constants.ActivityEvents.FINISH;
import static com.aevi.sdk.flow.constants.ErrorConstants.FLOW_SERVICE_ERROR;

/**
 * A helper class that can be used to start an activity in flow services
 */
public class ActivityHelper {

    public static final String ACTIVITY_REQUEST_KEY = "request";
    public static final String EXTRAS_INTERNAL_DATA_KEY = "internalData";

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
    public ObservableActivityHelper<AppMessage> launchActivity() {
        activityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (request != null) {
            activityIntent.putExtra(ACTIVITY_REQUEST_KEY, request);
        }
        activityIntent.putExtra(ObservableActivityHelper.INTENT_ID, activityId);
        if (extras != null) {
            activityIntent.putExtras(extras);
        }
        ObservableActivityHelper<AppMessage> helper = ObservableActivityHelper.createInstance(context, activityIntent);
        helper.startObservableActivity().subscribe(new Consumer<AppMessage>() {
            @Override
            public void accept(@NonNull AppMessage responseMessage) {
                clientCommunicator.sendMessage(responseMessage);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) {
                handleActivityException(throwable, clientCommunicator);
            }
        });
        return helper;
    }

    private void handleActivityException(Throwable throwable, ClientCommunicator clientCommunicator) {
        if (throwable instanceof FlowException) {
            FlowException me = (FlowException) throwable;
            clientCommunicator.sendResponseAsErrorAndEnd(me.getErrorCode(), me.getErrorMessage());
        } else {
            clientCommunicator.sendResponseAsErrorAndEnd(FLOW_SERVICE_ERROR,
                                                         String.format("Flow service failed during activity: %s", throwable.getMessage()));
        }
    }

    /**
     * Finish an activity launched via {@link #launchActivity()}.
     *
     * Note that the activity must have subscribed via ObservableActivityHelper.registerForEvents().
     */
    public void finishLaunchedActivity() {
        try {
            ObservableActivityHelper<AppMessage> helper = ObservableActivityHelper.getInstance(activityId);
            helper.sendEventToActivity(FINISH);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
    }
}
