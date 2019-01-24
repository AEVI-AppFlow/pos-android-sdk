package com.aevi.sdk.flow.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.stage.BackupClientCommunicatorHelper;
import io.reactivex.Observable;

import static com.aevi.sdk.flow.constants.AppMessageTypes.*;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;


/**
 * This class can be used to communicate with clients.
 *
 * This is an internal class not intended to be used directly by external applications. No guarantees are made of backwards compatibility and the
 * class may be removed without any warning.
 */
public class ClientCommunicator implements ChannelServer.ClientListener {

    private static final String TAG = ClientCommunicator.class.getSimpleName();

    private final Context context;
    private final ChannelServer channelServer;
    private final InternalData responseInternalData;
    private final boolean allowBackupMessage;

    private boolean connected = true;

    ClientCommunicator(Context context, ChannelServer channelServer, InternalData responseInternalData, boolean allowBackupMessage) {
        this.context = context;
        this.channelServer = channelServer;
        this.responseInternalData = responseInternalData;
        this.allowBackupMessage = allowBackupMessage;
        channelServer.addClientListener(this);
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
            if (connected) {
                channelServer.send(message.toJson());
            } else if (allowBackupMessage) {
                // our processing service has gone away so we need to send a message to it via listener
                BackupClientCommunicatorHelper bcch = new BackupClientCommunicatorHelper(context, channelServer.getClientPackageName());
                bcch.notifyListenersWithResponse(message.toJson());
            }
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
     * Subscribe to messages from the client.
     *
     * @return An observable stream of client messages
     */
    public Observable<AppMessage> subscribeToMessages() {
        return channelServer.subscribeToMessages().map(AppMessage::fromJson);
    }

    @Override
    public void onClientDispose() {
        // client has been disposed of by something other than us closing it
        connected = false;
    }

    @Override
    public void onClientClosed() {

    }
}
