package com.aevi.sdk.pos.flow;


import android.app.Service;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.aevi.sdk.flow.ApiBase;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowPackageManager;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ApiTestBase extends ApiBase {

    protected ApiTestBase(String propsFile) {
        super(propsFile);
    }

    protected void setupMockBoundMessengerService() {
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        MockMessageService mockMessageService = new MockMessageService();

        shadowApplication
                .setComponentNameAndServiceForBindService(FLOW_PROCESSING_SERVICE_COMPONENT, mockMessageService.onBind(null));

        Intent intent = new Intent();
        intent.setComponent(FLOW_PROCESSING_SERVICE_COMPONENT);

        ShadowPackageManager shadowPackageManager = Shadows.shadowOf(RuntimeEnvironment.application.getPackageManager());
        ResolveInfo resolveInfo = new ResolveInfo();
        resolveInfo.serviceInfo = new ServiceInfo();
        shadowPackageManager.addResolveInfoForIntent(intent, resolveInfo);
    }

    class MockMessageService extends Service {

        List<Message> messages = new ArrayList<>();

        class IncomingHandler extends Handler {

            @Override
            public void handleMessage(Message msg) {
                messages.add(msg);
            }
        }

        private final Messenger incomingMessenger = mock(Messenger.class);

        @Override
        public IBinder onBind(Intent intent) {
            return incomingMessenger.getBinder();
        }
    }
}
