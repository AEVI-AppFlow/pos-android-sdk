package com.aevi.sdk.flow.service;

import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import io.reactivex.subjects.BehaviorSubject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static com.aevi.sdk.flow.constants.ErrorConstants.FLOW_SERVICE_ERROR;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        FlowException expected =
                new FlowException(FLOW_SERVICE_ERROR, "Flow service failed with exception: Skimaroo");

        verifyMessageSent(AppMessageTypes.FAILURE_MESSAGE, expected.toJson());
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
        protected void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request,
                                      @Nullable InternalData senderInternalData) {
            requestReceived = Request.fromJson(request);
            if (throwExceptionInProcessRequest) {
                throw new IllegalStateException("Skimaroo");
            }
        }

    }
}
