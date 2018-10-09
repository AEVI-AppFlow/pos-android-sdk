package com.aevi.sdk.flow.service;

import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

import org.junit.Before;
import org.junit.Test;

import static com.aevi.sdk.flow.BaseApiClient.FLOW_PROCESSING_SERVICE;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class BaseListenerServiceTest {

    TestListenerService listenerService;
    Response response;
    AppMessage incomingAppMessage;

    @Before
    public void setUp() throws Exception {
        listenerService = new TestListenerService();
        response = new Response(new Request("banana"), true, "Believe!");
        incomingAppMessage = new AppMessage(AppMessageTypes.RESPONSE_MESSAGE, response.toJson());
    }

    @Test
    public void shouldSendAckOnResponseMessage() {
        listenerService.fakeIncomingMessage(incomingAppMessage, FLOW_PROCESSING_SERVICE);

        assertThat(listenerService.messageSent.getMessageType()).isEqualTo(AppMessageTypes.REQUEST_ACK_MESSAGE);
        assertThat(listenerService.endStreamSent).isTrue();
    }

    @Test
    public void shouldPassOnResponseMessage() {
        listenerService.fakeIncomingMessage(incomingAppMessage, FLOW_PROCESSING_SERVICE);

        assertThat(listenerService.responseCalled).isTrue();
        assertThat(listenerService.responseReceived).isEqualTo(response);
        assertThat(listenerService.endStreamSent).isTrue();
    }

    @Test
    public void willIgnoreMessageFromOtherPackages() {
        listenerService.fakeIncomingMessage(incomingAppMessage, "com.nefarious.app");

        assertThat(listenerService.responseCalled).isFalse();
        assertThat(listenerService.responseReceived).isNull();
        assertThat(listenerService.endStreamSent).isTrue();
    }

    @Test
    public void willIgnoreWrongMessageType() {
        AppMessage wrongIncomingAppMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, response.toJson());
        listenerService.fakeIncomingMessage(wrongIncomingAppMessage, FLOW_PROCESSING_SERVICE);

        assertThat(listenerService.responseCalled).isFalse();
        assertThat(listenerService.responseReceived).isNull();
        assertThat(listenerService.endStreamSent).isTrue();
    }

    class TestListenerService extends BaseListenerService<Response> {

        Response responseReceived;
        boolean endStreamSent;
        boolean responseCalled;
        AppMessage messageSent;

        protected TestListenerService() {
            super(Response.class, "1.0.0");
        }

        void fakeIncomingMessage(AppMessage appMessage, String packageName) {
            handleRequest("123", appMessage.toJson(), packageName);
        }

        @Override
        protected void notifyResponse(Response response) {
            responseCalled = true;
            responseReceived = response;
        }

        @Override
        public boolean sendEndStreamMessageToClient(String clientId) {
            endStreamSent = true;
            return true;
        }

        @Override
        public boolean sendMessageToClient(String clientId, String response) {
            messageSent = AppMessage.fromJson(response);
            return true;
        }
    }
}
