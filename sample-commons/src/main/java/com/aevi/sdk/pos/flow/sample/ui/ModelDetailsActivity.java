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

package com.aevi.sdk.pos.flow.sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.sample.R;
import com.aevi.util.json.JsonConverter;

public class ModelDetailsActivity extends AppCompatActivity {

    public static final String KEY_MODEL_DATA = "modelData";
    public static final String KEY_MODEL_TYPE = "modelType";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TITLE_BG = "titleBgColor";

    private ModelDisplay modelDisplay;
    private boolean hideFragmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_model_details);
        String title = getIntent().getStringExtra(KEY_TITLE);
        int titleBg = getIntent().getIntExtra(KEY_TITLE_BG, 0);
        TextView titleView = findViewById(R.id.title);
        if (title != null) {
            titleView.setText(title);
            hideFragmentTitle = true;
        }
        if (titleBg != 0) {
            titleView.setBackgroundColor(titleBg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (hideFragmentTitle) {
            modelDisplay.showTitle(false);
        }

        String modelType = getIntent().getStringExtra(KEY_MODEL_TYPE);
        String data = getIntent().getStringExtra(KEY_MODEL_DATA);

        if (modelType.equals(Payment.class.getName())) {
            modelDisplay.showPayment(Payment.fromJson(data));
        } else if (modelType.equals(TransactionRequest.class.getName())) {
            modelDisplay.showTransactionRequest(TransactionRequest.fromJson(data));
        } else if (modelType.equals(TransactionSummary.class.getName())) {
            modelDisplay.showTransactionSummary(TransactionSummary.fromJson(data));
        } else if (modelType.equals(SplitRequest.class.getName())) {
            modelDisplay.showSplitRequest(SplitRequest.fromJson(data));
        } else if (modelType.equals(PaymentResponse.class.getName())) {
            modelDisplay.showPaymentResponse(PaymentResponse.fromJson(data));
        } else if (modelType.equals(Response.class.getName())) {
            modelDisplay.showResponse(Response.fromJson(data));
        } else if (modelType.equals(Request.class.getName())) {
            modelDisplay.showRequest(Request.fromJson(data));
        } else if (modelType.equals(Card.class.getName())) {
            modelDisplay.showCard(JsonConverter.deserialize(data, Card.class));
        } else if (modelType.equals(TransactionResponse.class.getName())) {
            modelDisplay.showTransactionResponse(TransactionResponse.fromJson(data));
        } else if (modelType.equals(FlowResponse.class.getName())) {
            modelDisplay.showFlowResponse(FlowResponse.fromJson(data));
        } else {
            throw new IllegalArgumentException("Unknown type: " + modelType);
        }
    }
}
