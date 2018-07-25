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
import android.support.v7.widget.Toolbar;

import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.PaymentStage;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostFlowActivity extends BaseSampleAppCompatActivity<NoOpModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PaymentResponse paymentResponse;
    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_flow);
        ButterKnife.bind(this);
        registerForActivityEvents();
        setupToolbar(toolbar, R.string.fss_post_flow);
        paymentResponse = PaymentResponse.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        modelDisplay.showTitle(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay.showPaymentResponse(paymentResponse);
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(new NoOpModel());
    }

    @Override
    protected boolean showViewModelOption() {
        return false;
    }

    @Override
    protected boolean showViewRequestOption() {
        return false;
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return PaymentStage.POST_FLOW.name();
    }

    @Override
    protected Class<?> getRequestClass() {
        return PaymentResponse.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return NoOpModel.class;
    }

    @Override
    protected String getModelJson() {
        return null;
    }

    @Override
    protected String getRequestJson() {
        return paymentResponse.toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.post_flow_help);
    }
}
