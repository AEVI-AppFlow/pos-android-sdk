package com.aevi.sdk.flow.service;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BaseServiceInfoProviderTest {

    private static final String SERVICE_INFO_STRING = "heeeelloooo";

    @Test
    public void shouldSetServiceInfoInBundleWithCorrectKey() throws Exception {
        TestServiceInfoProvider serviceInfoProvider = new TestServiceInfoProvider("");
        Bundle bundle = serviceInfoProvider.call("", "", null);
        assertThat(bundle.getString(BaseServiceInfoProvider.SERVICE_INFO_KEY)).isEqualTo(SERVICE_INFO_STRING);
    }

    class TestServiceInfoProvider extends BaseServiceInfoProvider {

        TestServiceInfoProvider(String serviceInfoChangeBroadcast) {
            super(serviceInfoChangeBroadcast);
        }

        @Override
        protected String getServiceInfo() {
            return SERVICE_INFO_STRING;
        }
    }
}
