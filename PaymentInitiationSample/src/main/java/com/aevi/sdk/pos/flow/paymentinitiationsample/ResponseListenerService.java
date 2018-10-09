package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.util.Log;

import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseResponseListenerService;

public class ResponseListenerService extends BaseResponseListenerService {

    private static final String TAG = ResponseListenerService.class.getSimpleName();

    @Override
    protected void notifyResponse(Response response) {
        Log.d(TAG, "Got response in listener: " + response.toJson());
    }
}
