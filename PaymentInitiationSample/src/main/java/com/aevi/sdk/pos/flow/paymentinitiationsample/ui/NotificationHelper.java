package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.model.events.ConfirmationOption;
import com.aevi.sdk.pos.flow.model.events.ConfirmationRequest;
import com.aevi.sdk.pos.flow.model.events.ProgressMessage;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ConfirmationActionReceiver;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.*;
import static com.aevi.sdk.pos.flow.paymentinitiationsample.ConfirmationActionReceiver.*;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "SamplePaymentInitiator";
    private static final int NOTIFICATION_ID = 6689;

    private static final String TAG = NotificationHelper.class.getSimpleName();

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setupNotificationChannel();
    }

    private void updateNotification(Notification notification) {
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void notifyEvent(FlowEvent flowEvent) {
        String type = flowEvent.getType();
        String title = "Flow Event: " + flowEvent.getType();
        String message = "";
        int icon = 0;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title);

        switch (type) {
            case EVENT_PROGRESS_MESSAGE:
                ProgressMessage progressMessage = flowEvent.getEventData(ProgressMessage.class);
                message = progressMessage.getMessageText();
                icon = R.drawable.ic_event;
                break;
            case EVENT_CONFIRMATION_REQUEST:
                ConfirmationRequest conf = flowEvent.getEventData(ConfirmationRequest.class);
                message = conf.getTitleText();
                icon = R.drawable.ic_question_answer;
                int rCount = 0;
                if (!conf.isInput()) {
                    for (ConfirmationOption option : conf.getConfirmationOptions()) {
                        Intent confAction = new Intent(context, ConfirmationActionReceiver.class);
                        confAction.setAction(SEND_FLOW_EVENT_RESPONSE);
                        confAction.putExtra(EXTRA_CONFIRMATION_ID, conf.getId());
                        confAction.putExtra(EXTRA_RESPONSE, option.getValue());
                        confAction.putExtra(EXTRA_ORIGINATING_ID, flowEvent.getOriginatingRequestId());
                        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, rCount++, confAction, PendingIntent.FLAG_CANCEL_CURRENT);
                        builder.addAction(R.drawable.ic_question_answer, option.getLabel(), actionPendingIntent);
                    }
                }
                break;
        }

        builder
                .setContentText(message)
                .setSmallIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        updateNotification(builder.build());
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void dismissNotification() {
        notificationManager.cancelAll();
    }
}
