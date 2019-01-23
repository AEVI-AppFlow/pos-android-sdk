package com.aevi.sdk.flow.stage;

import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.FlowServiceEvents;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.service.ClientCommunicator;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ServiceComponentDelegateTest {

    private ClientCommunicator clientCommunicator;
    private ServiceComponentDelegate serviceComponentDelegate;
    private PublishSubject<AppMessage> messageSubject = PublishSubject.create();
    private TestObserver<String> messageObserver;
    private boolean sentToActivity;

    @Before
    public void setUp() throws Exception {
        clientCommunicator = mock(ClientCommunicator.class);
        when(clientCommunicator.subscribeToMessages()).thenReturn(messageSubject);
        serviceComponentDelegate = new ServiceComponentDelegate(clientCommunicator) {
            @Override
            public void sendMessageToActivity(String message) {
                sentToActivity = true;
            }
        };
        messageObserver = serviceComponentDelegate.getFlowServiceMessages().test();
    }

    @Test
    public void shouldSubscribeToMessagesOnCreation() throws Exception {
        verify(clientCommunicator).subscribeToMessages();
    }

    @Test
    public void shouldForwardResumeEventToFlowServiceStream() throws Exception {
        messageSubject.onNext(new AppMessage(AppMessageTypes.RESTART_UI));

        messageObserver.assertValue(FlowServiceEvents.RESUME_USER_INTERFACE);
    }

    @Test
    public void shouldSendForceFinishAndCompleteStream() throws Exception {
        messageSubject.onNext(new AppMessage(AppMessageTypes.FORCE_FINISH_MESSAGE));

        messageObserver.assertValue(FlowServiceEvents.FINISH_IMMEDIATELY);
        messageObserver.assertComplete();
        assertThat(sentToActivity).isTrue();
    }

    @Test
    public void shouldForwardResponseOutcomeAndCloseStream() throws Exception {
        messageSubject.onNext(new AppMessage(AppMessageTypes.RESPONSE_OUTCOME, FlowServiceEvents.RESPONSE_ACCEPTED));

        messageObserver.assertValue(FlowServiceEvents.RESPONSE_ACCEPTED);
        messageObserver.assertComplete();
    }
}
