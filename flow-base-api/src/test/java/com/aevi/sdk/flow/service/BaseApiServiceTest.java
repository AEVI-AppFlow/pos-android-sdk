package com.aevi.sdk.flow.service;


import android.support.annotation.NonNull;

import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.MessageErrors;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseApiServiceTest {

    private TestApiService apiService;
    private Request request;
    private AppMessage incomingAppMessage;

    @Mock
    ChannelServer channelServer;

    BehaviorSubject<String> incomingMessagePublisher;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        apiService = new TestApiService(channelServer);
        request = new Request("pigeon");
        incomingAppMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson());

        incomingMessagePublisher = BehaviorSubject.create();
        when(channelServer.subscribeToMessages()).thenReturn(incomingMessagePublisher);
        apiService.onNewClient(channelServer, "com.test");
    }

    @Test
    public void shouldSendAckOnRequestMessage() throws Exception {
        fakeIncomingMessage(incomingAppMessage);

        verifyMessageSent(AppMessageTypes.REQUEST_ACK_MESSAGE, "{}");
        verifyCommsEnded(false);
    }

    @Test
    public void shouldPassRequestToProcessRequest() throws Exception {
        fakeIncomingMessage(incomingAppMessage);

        assertThat(apiService.requestReceived).isEqualTo(request);
        verifyCommsEnded(false);
    }

    @Test
    public void shouldSendErrorMessageIfProcessRequestThrowsException() throws Exception {
        apiService.throwExceptionInProcessRequest = true;
        fakeIncomingMessage(incomingAppMessage);

        verifyMessageSent(AppMessageTypes.FAILURE_MESSAGE, MessageErrors.ERROR_SERVICE_EXCEPTION);
        verifyCommsEnded(true);
    }

    @Test
    public void shouldCallFinishOnFinishRequest() throws Exception {
        AppMessage appMessage = new AppMessage(AppMessageTypes.FORCE_FINISH_MESSAGE);
        fakeIncomingMessage(appMessage);

        assertThat(apiService.finishRequestReceived).isTrue();
        verifyCommsEnded(false);
    }

    private void verifyCommsEnded(boolean ended) {
        if (ended) {
            verify(channelServer).sendEndStream();
        } else {
            verify(channelServer, times(0)).sendEndStream();
        }
    }

    private void verifyMessageSent(String type, String response) {
        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
        verify(channelServer, atLeastOnce()).send(msgCaptor.capture());
        List<String> msgs = msgCaptor.getAllValues();
        String lastMsg = msgs.get(msgs.size() - 1);
        AppMessage messageSent = AppMessage.fromJson(lastMsg);
        assertThat(messageSent.getMessageType()).isEqualTo(type);
        assertThat(messageSent.getMessageData()).isEqualTo(response);
    }

    private void fakeIncomingMessage(AppMessage appMessage) {
        incomingMessagePublisher.onNext(appMessage.toJson());
    }

    class TestApiService extends BaseApiService {

        boolean throwExceptionInProcessRequest;
        Request requestReceived;
        boolean finishRequestReceived;

        TestApiService(ChannelServer channelServer) {
            super("1.0.0");
            channelServerMap.put("1.2.3", channelServer);
        }

        @Override
        protected void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request, @NonNull String flowStage) {
            requestReceived = Request.fromJson(request);
            if (throwExceptionInProcessRequest) {
                throw new IllegalStateException("Skimaroo");
            }
        }


        @Override
        protected void onForceFinish(ClientCommunicator clientCommunicator) {
            super.onForceFinish(clientCommunicator);
            finishRequestReceived = true;
        }
    }
}
