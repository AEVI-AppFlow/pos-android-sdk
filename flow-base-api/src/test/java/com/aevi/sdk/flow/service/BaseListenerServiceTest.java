package com.aevi.sdk.flow.service;

import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

import static com.aevi.sdk.flow.BaseApiClient.FLOW_PROCESSING_SERVICE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseListenerServiceTest {

    TestListenerService listenerService;
    Response response;
    AppMessage incomingAppMessage;

    @Mock
    ChannelServer channelServer;

    PublishSubject<String> incomingMessagePublisher;

    String lastMessage;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        listenerService = new TestListenerService(channelServer);
        response = new Response(new Request("banana"), true, "Believe!");
        incomingAppMessage = new AppMessage(AppMessageTypes.RESPONSE_MESSAGE, response.toJson());

        incomingMessagePublisher = PublishSubject.create();
        when(channelServer.subscribeToMessages()).thenReturn(incomingMessagePublisher);
    }

    private void setupNewFPSClient() {
        listenerService.onNewClient(channelServer, FLOW_PROCESSING_SERVICE);
    }

    private void setupNewEvilClient() {
        listenerService.onNewClient(channelServer, "com.nefarious.app");
    }

    @Test
    public void shouldSendAckOnResponseMessage() {
        fakeIncomingMessage(incomingAppMessage);
        setupNewFPSClient();

        verifyMessageSent(AppMessageTypes.REQUEST_ACK_MESSAGE, "{}");
        verifyCommsEnded(true);
    }

    @Test
    public void shouldPassOnResponseMessage() {
        fakeIncomingMessage(incomingAppMessage);
        setupNewFPSClient();

        assertThat(listenerService.responseCalled).isTrue();
        assertThat(listenerService.responseReceived).isEqualTo(response);
        verifyCommsEnded(true);
    }

    @Test
    public void willIgnoreMessageFromOtherPackages() {
        fakeIncomingMessage(incomingAppMessage);
        setupNewEvilClient();

        assertThat(listenerService.responseCalled).isFalse();
        assertThat(listenerService.responseReceived).isNull();
        verifyCommsEnded(true);
    }

    @Test
    public void willIgnoreWrongMessageType() {
        AppMessage wrongIncomingAppMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, response.toJson());
        fakeIncomingMessage(wrongIncomingAppMessage);
        setupNewFPSClient();

        assertThat(listenerService.responseCalled).isFalse();
        assertThat(listenerService.responseReceived).isNull();
        verifyCommsEnded(true);
    }

    private void fakeIncomingMessage(AppMessage appMessage) {
        lastMessage = appMessage.toJson();
        when(channelServer.getLastMessage()).thenReturn(lastMessage);
        incomingMessagePublisher.onNext(appMessage.toJson());
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

    private void verifyCommsEnded(boolean ended) {
        if (ended) {
            verify(channelServer).sendEndStream();
        } else {
            verify(channelServer, times(0)).sendEndStream();
        }
    }

    class TestListenerService extends BaseListenerService<Response> {

        Response responseReceived;
        boolean responseCalled;

        protected TestListenerService(ChannelServer channelServer) {
            super(Response.class, "1.0.0");
            channelServerMap.put("123", channelServer);
        }

        @Override
        protected void notifyResponse(Response response) {
            responseCalled = true;
            responseReceived = response;
        }

    }
}
