package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.util.Log;

import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseResponseListenerService;

public class ResponseListenerService extends BaseResponseListenerService {

    @Override
    protected void notifyResponse(Response response) {
        Log.d("XXX", "GOT RESPONSE UPDATE: " + response.toJson());
    }
}
