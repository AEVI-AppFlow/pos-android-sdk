package com.aevi.sdk.flow.service;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class BaseServiceInfoProviderTest {

    private static final String SERVICE_INFO_STRING = "heeeelloooo";

    private Context context;

    @Test
    public void shouldSetServiceInfoInBundleWithCorrectKey() throws Exception {
        TestServiceInfoProvider serviceInfoProvider = new TestServiceInfoProvider("");
        Bundle bundle = serviceInfoProvider.call("", "", null);
        assertThat(bundle.getString(BaseServiceInfoProvider.SERVICE_INFO_KEY)).isEqualTo(SERVICE_INFO_STRING);
    }

    @Test
    public void shouldSendCorrectBroadcastForServiceInfoChange() throws Exception {
        TestServiceInfoProvider serviceInfoProvider = new TestServiceInfoProvider("my.broadcast");

        serviceInfoProvider.notifyServiceInfoChange();

        ArgumentCaptor<Intent> broadcastCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(context).sendBroadcast(broadcastCaptor.capture());

        assertThat(broadcastCaptor.getValue().getAction()).isEqualTo("my.broadcast");
        assertThat(broadcastCaptor.getValue().getData().toString()).isEqualTo("package:com.test");
    }

    class TestServiceInfoProvider extends BaseServiceInfoProvider {

        TestServiceInfoProvider(String serviceInfoChangeBroadcast) {
            super(serviceInfoChangeBroadcast);
        }

        @Override
        protected String getServiceInfo() {
            return SERVICE_INFO_STRING;
        }

        @Override
        public Context getContext() {
            context = mock(Context.class);
            when(context.getPackageName()).thenReturn("com.test");
            return context;
        }
    }
}
