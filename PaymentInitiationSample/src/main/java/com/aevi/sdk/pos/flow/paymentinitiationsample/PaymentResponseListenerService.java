package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.util.Log;

import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.service.BasePaymentResponseListenerService;

public class PaymentResponseListenerService extends BasePaymentResponseListenerService {

    @Override
    protected void notifyResponse(PaymentResponse paymentResponse) {
        Log.d("XXX", "GOT RESPONSE UPDATE: " + paymentResponse.toJson());
    }
}
