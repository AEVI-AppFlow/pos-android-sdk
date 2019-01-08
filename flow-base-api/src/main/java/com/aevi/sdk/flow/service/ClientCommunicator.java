package com.aevi.sdk.flow.service;

import android.support.annotation.NonNull;
import android.util.Log;
import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.InternalData;
import io.reactivex.Observable;

import java.util.HashSet;
import java.util.Set;

import static com.aevi.sdk.flow.constants.AppMessageTypes.*;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;
import static com.aevi.sdk.flow.service.BaseApiService.BACKGROUND_PROCESSING;


/**
 * This class can be used to communicate with clients.
 *
 * A communicator instance will be created for your Flow Service on connection from a client
 */
public class ClientCommunicator {

    private static final String TAG = ClientCommunicator.class.getSimpleName();

    private final ChannelServer channelServer;
    private final InternalData responseInternalData;
    private final Set<ActivityHelper> activityHelpers;

    ClientCommunicator(ChannelServer channelServer, InternalData responseInternalData) {
        this.channelServer = channelServer;
        this.responseInternalData = responseInternalData;
        this.activityHelpers = new HashSet<>();
    }

    void sendAck() {
        Log.d(TAG, "Sending ack");
        AppMessage appMessage = new AppMessage(REQUEST_ACK_MESSAGE, responseInternalData);
        channelServer.send(appMessage.toJson());
    }

    public InternalData getResponseInternalData() {
        return responseInternalData;
    }

    /**
     * Send a message to the client.
     *
     * @param message The message
     */
    public void sendMessage(AppMessage message) {
        if (channelServer != null) {
            channelServer.send(message.toJson());
        }
    }

    /**
     * Send response to the client.
     *
     * @param response The response to send
     */
    public void sendResponse(@NonNull String response) {
        AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, response, responseInternalData);
        sendMessage(appMessage);
    }

    /**
     * Finish your flow service with no response.
     */
    public void finishWithNoResponse() {
        AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, EMPTY_DATA, responseInternalData);
        sendMessage(appMessage);
    }

    /**
     * End the stream with the client.
     */
    public void endStream() {
        channelServer.sendEndStream();
    }

    /**
     * Finish your flow service with an exception.
     *
     * Can be used to send an errorCode back to the initiating application
     *
     * @param errorCode The errorCode to send back
     * @param message   A human readable message to explain the error
     */
    public void sendResponseAsErrorAndEnd(@NonNull String errorCode, @NonNull String message) {
        FlowException flowServiceException = new FlowException(errorCode, message);
        String msg = flowServiceException.toJson();
        Log.d(TAG, "Sending error message: " + msg);
        AppMessage errorMessage = new AppMessage(FAILURE_MESSAGE, msg, responseInternalData);
        sendMessage(errorMessage);
    }

    /**
     * Send notification that this service will process in the background and won't send back any response.
     *
     * Note that you should NOT show any UI after calling this, nor call any of the "finish...Response" methods.
     *
     * This is typically useful for post-transaction / post-flow services that processes the transaction information with no need
     * to show any user interface or augment the transaction.
     */
    public void notifyBackgroundProcessing() {
        Log.d(TAG, "notifyBackgroundProcessing");
        responseInternalData.addAdditionalData(BACKGROUND_PROCESSING, "true");
        sendResponse(EMPTY_DATA);
    }

    Observable<String> subscribeToMessages() {
        return channelServer.subscribeToMessages();
    }

    void finishStartedActivities() {
        for (ActivityHelper activityHelper : activityHelpers) {
            activityHelper.finishLaunchedActivity();
        }
    }

    /**
     * Add any {@link ActivityHelper} classes used to start activities for this client connection
     *
     * @param activityHelper The Activity helper class to add
     */
    public void addActivityHelper(ActivityHelper activityHelper) {
        activityHelpers.add(activityHelper);
    }
}
