package com.aevi.payment;


import android.app.Service;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowPackageManager;

import java.util.ArrayList;
import java.util.List;

import static com.aevi.sdk.flow.ApiHelper.PAYMENT_CONTROL_SERVICE_COMPONENT;
import static org.mockito.Mockito.mock;

public class ApiTestBase {

    protected void setupMockBoundMessengerService() {
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        MockMessageService mockMessageService = new MockMessageService();

        shadowApplication
                .setComponentNameAndServiceForBindService(com.aevi.sdk.flow.ApiHelper.PAYMENT_CONTROL_SERVICE_COMPONENT, mockMessageService.onBind(null));

        Intent intent = new Intent();
        intent.setComponent(PAYMENT_CONTROL_SERVICE_COMPONENT);

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
