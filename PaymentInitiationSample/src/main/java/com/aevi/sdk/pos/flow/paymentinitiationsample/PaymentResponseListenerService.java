package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.util.Log;

import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.service.BasePaymentResponseListenerService;

public class PaymentResponseListenerService extends BasePaymentResponseListenerService {

    private static final String TAG = PaymentResponseListenerService.class.getSimpleName();

    @Override
    protected void notifyResponse(PaymentResponse paymentResponse) {
        Log.d(TAG, "Got response in payment response listener: " + paymentResponse.toJson());
    }
}
