package com.aevi.sdk.flow.stage;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.FlowServiceEventTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowEvent;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ActivityComponentDelegateTest {

    private Lifecycle lifecycle;
    private Activity activity;
    private Intent intent;
    private ObservableActivityHelper helper;
    private ActivityComponentDelegate activityComponentDelegate;
    private PublishSubject<String> helperEventSubject = PublishSubject.create();
    private TestObserver<FlowEvent> messageObserver;

    @Before
    public void setUp() throws Exception {
        lifecycle = mock(Lifecycle.class);
        activity = spy(new FakeActivity());
        intent = mock(Intent.class);
        when(intent.getExtras()).thenReturn(new Bundle());
        helper = mock(ObservableActivityHelper.class);
        when(helper.registerForEvents()).thenReturn(helperEventSubject);
        activityComponentDelegate = new ActivityComponentDelegate(activity, "com.something") {
            @Override
            protected ObservableActivityHelper<String> getHelperFromActivity(Activity activity) throws NoSuchInstanceException {
                return helper;
            }
        };
        messageObserver = activityComponentDelegate.getFlowServiceEvents().test();
    }

    @Test
    public void shouldRegisterForLifecycleOnCreation() throws Exception {
        verify(helper).setLifecycle(lifecycle);
    }

    @Test
    public void shouldFinishActivityOnFinishEvent() throws Exception {
        helperEventSubject.onNext(new FlowEvent(FlowServiceEventTypes.FINISH_IMMEDIATELY).toJson());

        verify(activity).finish();
        messageObserver.assertComplete();
    }

    @Test
    public void shouldProxyFlowServiceEvents() throws Exception {
        helperEventSubject.onNext(new FlowEvent("someEvent").toJson());

        messageObserver.assertValue(new FlowEvent("someEvent"));
    }

    @Test
    public void sendMessageShouldSendViaHelper() throws Exception {
        AppMessage appMessage = new AppMessage("hello");
        activityComponentDelegate.sendMessage(appMessage);

        verify(helper).sendMessageToClient(appMessage);
    }

    class FakeActivity extends Activity implements LifecycleOwner {

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycle;
        }

        @Override
        public Intent getIntent() {
            return intent;
        }
    }
}
