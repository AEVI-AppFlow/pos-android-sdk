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

package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostPaymentActivity extends AppCompatActivity {

    private FlowResponse flowResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_payment);
        ButterKnife.bind(this);
        flowResponse = new FlowResponse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        modelDisplay.showTransactionSummary(TransactionSummary.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY)));
    }

    @OnClick(R.id.add_payment_references)
    public void onAddPaymentReferences() {
        AdditionalData paymentRefs = new AdditionalData();
        paymentRefs.addData("someReference", "addedByPostPaymentSample");
        flowResponse.setPaymentReferences(paymentRefs);
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(flowResponse);
    }

    private void sendResponseAndFinish(FlowResponse flowResponse) {
        try {
            ObservableActivityHelper<FlowResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(flowResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
