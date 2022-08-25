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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import static com.aevi.sdk.pos.flow.model.PaymentResponse.Outcome.FAILED;
import static com.aevi.sdk.pos.flow.model.PaymentResponse.Outcome.PARTIALLY_FULFILLED;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentResultActivity extends AppCompatActivity {

    public static final String PAYMENT_RESPONSE_KEY = "paymentResponse";
    public static final String ERROR_KEY = "error";
    private static final String TAG = PaymentResultActivity.class.getSimpleName();

    @BindView(R.id.request_status)
    ImageView requestStatus;

    @BindView(R.id.message_error_code)
    @Nullable
    TextView messageErrorCode;

    @BindView(R.id.message_error_description)
    @Nullable
    TextView messageErrorDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().hasExtra(PAYMENT_RESPONSE_KEY) ? R.layout.activity_payment_approved : R.layout.activity_payment_error);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra(PAYMENT_RESPONSE_KEY)) {
            PaymentResponse response = PaymentResponse.fromJson(intent.getStringExtra(PAYMENT_RESPONSE_KEY));
            ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
            modelDisplay.showPaymentResponse(response);
            modelDisplay.showTitle(false);
            if (response.getOutcome() == PARTIALLY_FULFILLED) {
                requestStatus.setImageResource(R.drawable.ic_check_circle_amber);
            } else if (response.getOutcome() == FAILED) {
                requestStatus.setImageResource(R.drawable.ic_error_circle);
            }
        } else if (intent.hasExtra(ERROR_KEY)) {
            FlowException error = FlowException.fromJson(intent.getStringExtra(ERROR_KEY));
            showErrorResult(error);
        }
    }

    private void showErrorResult(FlowException error) {
        messageErrorCode.setText(error.getErrorCode());
        messageErrorDesc.setText(error.getErrorMessage());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.button_close)
    public void close() {
        finish();
    }
}
