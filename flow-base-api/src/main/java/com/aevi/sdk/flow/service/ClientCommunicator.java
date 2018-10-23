package com.aevi.sdk.flow.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.android.rxmessenger.MessageException;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.InternalData;

import io.reactivex.Observable;

import static com.aevi.sdk.flow.constants.AppMessageTypes.FAILURE_MESSAGE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.REQUEST_ACK_MESSAGE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.RESPONSE_MESSAGE;
import static com.aevi.sdk.flow.model.AppMessage.EMPTY_DATA;
import static com.aevi.sdk.flow.service.BaseApiService.BACKGROUND_PROCESSING;

public class ClientCommunicator {

    private static final String TAG = ClientCommunicator.class.getSimpleName();

    private final ChannelServer channelServer;
    private final InternalData internalData;

    public ClientCommunicator(ChannelServer channelServer, InternalData internalData) {
        this.channelServer = channelServer;
        this.internalData = internalData;
    }

    public void sendAck() {
        Log.d(TAG, "Sending ack");
        AppMessage appMessage = new AppMessage(REQUEST_ACK_MESSAGE, internalData);
        channelServer.send(appMessage.toJson());
    }

    public void sendResponseAndEnd(@NonNull String response) {
        if (channelServer != null) {
            AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, response, internalData);
            channelServer.send(appMessage.toJson());
            channelServer.sendEndStream();
        }
    }

    public void send(MessageException me) {
        if (channelServer != null) {
            channelServer.send(me);
        }
    }

    public void finishWithNoResponse() {
        if (channelServer != null) {
            AppMessage appMessage = new AppMessage(RESPONSE_MESSAGE, EMPTY_DATA, internalData);
            channelServer.send(appMessage.toJson());
            channelServer.sendEndStream();
        }
    }

    public void sendResponseAsErrorAndEnd(@NonNull String error) {
        if (channelServer != null) {
            Log.d(TAG, "Sending error message: " + error);
            AppMessage errorMessage = new AppMessage(FAILURE_MESSAGE, error, internalData);
            channelServer.send(errorMessage.toJson());
            channelServer.sendEndStream();
        }
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
        internalData.addAdditionalData(BACKGROUND_PROCESSING, "true");
        sendResponseAndEnd(EMPTY_DATA);
    }

    public Observable<String> subscribeToMessages() {
        return channelServer.subscribeToMessages();
    }


}
