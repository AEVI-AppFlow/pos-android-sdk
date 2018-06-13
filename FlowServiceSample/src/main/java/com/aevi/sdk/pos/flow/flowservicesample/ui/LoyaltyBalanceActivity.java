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

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoyaltyBalanceActivity extends BaseSampleAppCompatActivity<Response> {

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_balance);
        ButterKnife.bind(this);
        request = Request.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        registerForActivityEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        AdditionalData loyaltyData = new AdditionalData();
        Customer customer = request.getRequestData().getValue(AdditionalDataKeys.DATA_KEY_CUSTOMER, Customer.class);
        if (customer == null) {
            sendResponseAndFinish(new Response(request, false, "Customer data missing"));
        } else {
            loyaltyData.addData(AdditionalDataKeys.DATA_KEY_CUSTOMER, customer);
            loyaltyData.addData("loyaltyPointsBalance", new Random().nextInt(1000));
            loyaltyData.addData("loyaltySignUpDate", "2016-05-24");
            loyaltyData.addData("loyaltyAccumulatedTotal", new Random().nextInt(10000) + 1000);
            modelDisplay.showCustomData(loyaltyData);
            modelDisplay.showTitle(false);
        }
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(new Response(request, true, "Loyalty balance presented"));
    }

}
