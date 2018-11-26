/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.content.Intent;
import android.widget.Toast;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.service.BasePaymentResponseListenerService;

public class PaymentResponseListenerService extends BasePaymentResponseListenerService {

    @Override
    protected void notifyResponse(PaymentResponse paymentResponse) {
        // Here we only displaying our result activity for successful payment responses.
        if (paymentResponse.getOutcome() == PaymentResponse.Outcome.FAILED) {
            Toast.makeText(this, paymentResponse.getFailureMessage(), Toast.LENGTH_SHORT).show();
        } else {
            final Intent intent = new Intent(this, PaymentResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION);

            SampleContext.getInstance(this).setLastReceivedPaymentResponse(paymentResponse);
            intent.putExtra(PaymentResultActivity.PAYMENT_RESPONSE_KEY, paymentResponse.toJson());
            startActivity(intent);
        }
    }

    @Override
    protected void notifyError(String errorCode, String errorMessage) {
        Toast.makeText(this, String.format("Payment error %s: %s", errorCode, errorMessage), Toast.LENGTH_SHORT).show();
    }
}
