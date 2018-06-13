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

package com.aevi.sdk.pos.flow.paymentservicesample.ui;

import android.os.Bundle;

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.ui.library.recycler.DropDownSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTokenActivity extends BaseSampleAppCompatActivity<Response> {

    @BindView(R.id.card_scheme_spinner)
    DropDownSpinner cardSchemeSpinner;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_token);
        ButterKnife.bind(this);
        request = Request.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        registerForActivityEvents();
    }

    @OnClick(R.id.reply_with_token)
    public void onRespondWitToken() {
        sendTokenResponseAndFinish(CustomerProducer.CUSTOMER_TOKEN);
    }

    @OnClick(R.id.reply_no_token)
    public void onRespondWithFailure() {
        sendTokenResponseAndFinish(null);
    }

    private void sendTokenResponseAndFinish(Token token) {
        Response response = new Response(request, token != null, token != null ? "Sample token generated" : "Failed to generate sample token");
        response.addAdditionalData(AdditionalDataKeys.DATA_KEY_TOKEN, token);
        sendResponseAndFinish(response);
    }
}
