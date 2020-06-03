package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.events.ConfirmationResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.NotificationHelper;

import static com.aevi.sdk.flow.constants.events.FlowEventTypes.EVENT_CONFIRMATION_RESPONSE;

public class ConfirmationActionReceiver extends BroadcastReceiver {
    private static final String TAG = ConfirmationActionReceiver.class.getSimpleName();
    public static final String SEND_FLOW_EVENT_RESPONSE = "com.aevi.sdk.flow.event.action.SEND";

    public static final String EXTRA_NOTIFICATION_ID = "notifId";
    public static final String EXTRA_CONFIRMATION_ID = "confId";
    public static final String EXTRA_RESPONSE = "response";
    public static final String EXTRA_ORIGINATING_ID = "originatingId";

    @Override
    public void onReceive(Context context, Intent intent) {
        String confId = intent.getStringExtra(EXTRA_CONFIRMATION_ID);
        String response = intent.getStringExtra(EXTRA_RESPONSE);
        String origId = intent.getStringExtra(EXTRA_ORIGINATING_ID);

        FlowEvent flowEvent = new FlowEvent(EVENT_CONFIRMATION_RESPONSE, new ConfirmationResponse(confId, new String[]{response}));
        flowEvent.setOriginatingRequestId(origId);

        Log.d(TAG, "Sending flow event: " + flowEvent.toJson());

        PaymentClient paymentClient = PaymentApi.getPaymentClient(context.getApplicationContext());
        paymentClient.sendEvent(flowEvent).subscribe(
                () -> Log.i(TAG, "FPS accepted Flow Event"),
                throwable -> Log.e(TAG, "Failed to send flow event", throwable));

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.dismissNotification();
    }
}
