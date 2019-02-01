package com.aevi.sdk.flow.stage;

import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.service.ClientCommunicator;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Test;

import static com.aevi.sdk.flow.constants.AppMessageTypes.FLOW_SERVICE_EVENT;
import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ServiceComponentDelegateTest {

    private ClientCommunicator clientCommunicator;
    private ServiceComponentDelegate serviceComponentDelegate;
    private PublishSubject<AppMessage> messageSubject = PublishSubject.create();
    private TestObserver<FlowEvent> messageObserver;
    private boolean sentToActivity;

    @Before
    public void setUp() throws Exception {
        clientCommunicator = mock(ClientCommunicator.class);
        when(clientCommunicator.subscribeToMessages()).thenReturn(messageSubject);
        serviceComponentDelegate = new ServiceComponentDelegate(clientCommunicator) {
            @Override
            public void sendEventToActivity(FlowEvent event) {
                sentToActivity = true;
            }
        };
        messageObserver = serviceComponentDelegate.getFlowServiceEvents().test();
    }

    @Test
    public void shouldSubscribeToMessagesOnCreation() throws Exception {
        verify(clientCommunicator).subscribeToMessages();
    }

    @Test
    public void shouldForwardResumeEventToFlowServiceStream() throws Exception {
        messageSubject.onNext(new AppMessage(FLOW_SERVICE_EVENT, new FlowEvent(RESUME_USER_INTERFACE).toJson()));

        messageObserver.assertValue(new FlowEvent(RESUME_USER_INTERFACE));
    }

    @Test
    public void shouldSendForceFinishAndCompleteStream() throws Exception {
        messageSubject.onNext(new AppMessage(FLOW_SERVICE_EVENT, new FlowEvent(FINISH_IMMEDIATELY).toJson()));

        messageObserver.assertValue(new FlowEvent(FINISH_IMMEDIATELY));
        messageObserver.assertComplete();
        assertThat(sentToActivity).isTrue();
    }

    @Test
    public void shouldForwardResponseOutcomeAndCloseStream() throws Exception {
        messageSubject.onNext(new AppMessage(FLOW_SERVICE_EVENT, new FlowEvent(RESPONSE_ACCEPTED).toJson()));

        messageObserver.assertValue(new FlowEvent(RESPONSE_ACCEPTED));
        messageObserver.assertComplete();
    }
}
