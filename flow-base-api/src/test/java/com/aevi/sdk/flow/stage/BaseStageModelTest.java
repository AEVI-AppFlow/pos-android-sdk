package com.aevi.sdk.flow.stage;

import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AuditEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.aevi.sdk.flow.model.AuditEntry.AuditSeverity.WARNING;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

import androidx.annotation.NonNull;

public class BaseStageModelTest {

    private AndroidComponentDelegate androidComponentDelegate;
    private TestModel testModel;

    @Before
    public void setUp() throws Exception {
        androidComponentDelegate = mock(AndroidComponentDelegate.class);
        testModel = new TestModel(androidComponentDelegate);
    }

    @Test
    public void shouldSendAuditEntryModelCorrectly() throws Exception {
        testModel.addAuditEntry(WARNING, "test");

        ArgumentCaptor<AppMessage> auditEntryArgumentCaptor = ArgumentCaptor.forClass(AppMessage.class);
        verify(androidComponentDelegate).sendMessage(auditEntryArgumentCaptor.capture());

        AppMessage appMessage = auditEntryArgumentCaptor.getValue();
        assertThat(appMessage.getMessageType()).isEqualTo(AppMessageTypes.AUDIT_ENTRY);
        AuditEntry auditEntry = AuditEntry.fromJson(appMessage.getMessageData());
        assertThat(auditEntry.getAuditSeverity()).isEqualTo(WARNING);
        assertThat(auditEntry.getAuditMessage()).isEqualTo("test");
    }

    @Test
    public void shouldOnlyAllowFiveAuditEntries() throws Exception {
        for (int i = 0; i < 10; i++) {
            testModel.addAuditEntry(WARNING, "test");
        }

        verify(androidComponentDelegate, times(5)).sendMessage(isA(AppMessage.class));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldOnlyAllowResponseSentOnce() throws Exception {
        testModel.sendEmptyResponse();
        testModel.sendEmptyResponse();
    }

    static class TestModel extends BaseStageModel {

        TestModel(AndroidComponentDelegate androidComponentDelegate) {
            super(androidComponentDelegate);
        }

        @NonNull
        @Override
        public String getRequestJson() {
            return null;
        }
    }
}
