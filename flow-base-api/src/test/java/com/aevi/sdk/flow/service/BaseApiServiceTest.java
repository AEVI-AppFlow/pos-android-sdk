package com.aevi.sdk.flow.service;


import android.support.annotation.NonNull;

import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.MessageErrors;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BaseApiServiceTest {

    private TestApiService apiService;
    private Request request;
    private AppMessage incomingAppMessage;

    @Before
    public void setUp() throws Exception {
        apiService = new TestApiService();
        request = new Request("pigeon");
        incomingAppMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson());
    }

    @Test
    public void shouldSendAckOnRequestMessage() throws Exception {
        apiService.fakeIncomingMessage(incomingAppMessage);

        assertThat(apiService.messageSent.getMessageType()).isEqualTo(AppMessageTypes.REQUEST_ACK_MESSAGE);
        assertThat(apiService.endStreamSent).isFalse();
    }

    @Test
    public void shouldPassRequestToProcessRequest() throws Exception {
        apiService.fakeIncomingMessage(incomingAppMessage);

        assertThat(apiService.requestReceived).isEqualTo(request);
        assertThat(apiService.endStreamSent).isFalse();
    }

    @Test
    public void shouldSendErrorMessageIfProcessRequestThrowsException() throws Exception {
        apiService.throwExceptionInProcessRequest = true;
        boolean exceptionThrown = false;
        try {
            apiService.fakeIncomingMessage(incomingAppMessage);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isTrue();
        assertThat(apiService.messageSent.getMessageType()).isEqualTo(AppMessageTypes.FAILURE_MESSAGE);
        assertThat(apiService.messageSent.getMessageData()).isEqualTo(MessageErrors.ERROR_SERVICE_EXCEPTION);
        assertThat(apiService.endStreamSent).isTrue();
    }

    @Test
    public void shouldCallFinishOnFinishRequest() throws Exception {
        AppMessage appMessage = new AppMessage(AppMessageTypes.FORCE_FINISH_MESSAGE);
        apiService.fakeIncomingMessage(appMessage);

        assertThat(apiService.finishRequestReceived).isTrue();
        assertThat(apiService.endStreamSent).isFalse();
    }

    @Test
    public void shouldAllowFinishWithNoResponse() throws Exception {
        apiService.callFinishWithEmptyResponse();

        assertThat(apiService.messageSent.getMessageType()).isEqualTo(AppMessageTypes.RESPONSE_MESSAGE);
        assertThat(apiService.messageSent.getMessageData()).isEqualTo(AppMessage.EMPTY_DATA);
        assertThat(apiService.endStreamSent).isTrue();
    }

    @Test
    public void shouldAllowFinishWithResponse() throws Exception {
        Response response = new Response(request, true, "The Rock or The Mountain?");
        apiService.callFinishWithResponse(response);

        assertThat(apiService.messageSent.getMessageType()).isEqualTo(AppMessageTypes.RESPONSE_MESSAGE);
        assertThat(apiService.messageSent.getMessageData()).isEqualTo(response.toJson());
        assertThat(apiService.endStreamSent).isTrue();
    }

    class TestApiService extends BaseApiService {

        boolean throwExceptionInProcessRequest;

        Request requestReceived;
        boolean finishRequestReceived;
        AppMessage messageSent;
        boolean endStreamSent;
        String errorCodeSent;
        String errorMessageSent;

        TestApiService() {
            super("1.0.0");
        }

        void fakeIncomingMessage(AppMessage appMessage) {
            handleRequest("123", appMessage.toJson(), "com.test");
        }

        void callFinishWithEmptyResponse() {
            finishWithNoResponse("1.2.3");
        }

        void callFinishWithResponse(Response response) {
            finishWithResponse("1.2.3", response.toJson());
        }

        @Override
        protected void processRequest(@NonNull String clientMessageId, @NonNull String request, @NonNull String flowStage) {
            requestReceived = Request.fromJson(request);
            if (throwExceptionInProcessRequest) {
                throw new IllegalStateException("Skimaroo");
            }
        }

        @Override
        protected void onFinish(@NonNull String clientMessageId) {
            finishRequestReceived = true;
        }

        @Override
        public boolean sendMessageToClient(String clientId, String message) {
            messageSent = AppMessage.fromJson(message);
            return true;
        }

        @Override
        public boolean sendEndStreamMessageToClient(String clientId) {
            endStreamSent = true;
            return true;
        }

        @Override
        public boolean sendErrorMessageToClient(String clientId, String code, String message) {
            errorCodeSent = code;
            errorMessageSent = message;
            return true;
        }


    }
}
