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

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment.PaymentFragment;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentInitiationActivity extends BaseSampleAppCompatActivity<PaymentResponse> {

    private ModelDisplay modelDisplay;

    private PaymentFragment paymentFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_payment);
        setupToolbar(toolbar, R.string.initiate_payment);
    }

    public ModelDisplay getModelDisplay() {
        return modelDisplay;
    }

    @Override
    protected boolean showViewRequestOption() {
        return false;
    }

    @Override
    protected boolean showViewModelOption() {
        return !getResources().getBoolean(R.bool.dualPane);
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return "Payment initiation";
    }

    @Override
    protected Class<?> getRequestClass() {
        return Payment.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return Payment.class;
    }

    @Override
    protected String getModelJson() {
        return paymentFragment.getPayment().toJson();
    }

    @Override
    protected String getRequestJson() {
        return null;
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.initiation_help);
    }
}
