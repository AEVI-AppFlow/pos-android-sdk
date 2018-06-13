package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;

import com.aevi.sdk.flow.model.InternalData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ApiBaseTest {

    class TestApiBase extends ApiBase {

        public TestApiBase(String propsFile) {
            super(propsFile, mock(Context.class));
        }

        @Override
        public InternalData getInternalData() {
            return super.getInternalData();
        }

        public void callStartFps(Context context) {
            startFps(context);
        }

        public ComponentName getFpsComponent() {
            return FLOW_PROCESSING_SERVICE_COMPONENT;
        }
    }

    private TestApiBase apiBase = new TestApiBase("1.2.3");

    @Test
    public void shouldParsePropertiesFileAndSetVersion() throws Exception {
        assertThat(apiBase.getInternalData().getSenderApiVersion()).isEqualTo("1.2.3"); // As per test resource
    }

    @Test
    public void callStartFpsShouldSendStartServiceIntent() throws Exception {
        apiBase.callStartFps(RuntimeEnvironment.application);
        Intent intent = ShadowApplication.getInstance().peekNextStartedService();
        assertThat(intent.getComponent()).isEqualTo(apiBase.getFpsComponent());
    }

    @Test
    public void callIsProcessingServiceInstalledShouldReturnFalse() throws Exception {
        assertThat(ApiBase.isProcessingServiceInstalled(RuntimeEnvironment.application)).isFalse();
    }

    @Test
    public void callIsProcessingServiceInstalledShouldReturnTrueIfPackageManagerThinksSo() throws Exception {
        pretendFpsIsInstalled();

        assertThat(ApiBase.isProcessingServiceInstalled(RuntimeEnvironment.application)).isTrue();
    }

    private void pretendFpsIsInstalled() {
        RobolectricPackageManager packageManager = shadowOf(RuntimeEnvironment.application.getPackageManager());
        Intent intent = new Intent();
        intent.setComponent(apiBase.getFpsComponent());

        ResolveInfo resolveInfo = new ResolveInfo();
        resolveInfo.isDefault = true;

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.packageName = apiBase.getFpsComponent().getPackageName();
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = serviceInfo.packageName;
        serviceInfo.applicationInfo = applicationInfo;

        resolveInfo.serviceInfo = serviceInfo;

        packageManager.addResolveInfoForIntent(intent, resolveInfo);
    }
}
