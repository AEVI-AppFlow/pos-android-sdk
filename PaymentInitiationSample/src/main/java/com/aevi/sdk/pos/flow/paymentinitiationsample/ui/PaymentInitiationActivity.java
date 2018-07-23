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
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.PaymentFragmentTabsAdapter;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.athbk.ultimatetablayout.UltimateTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentInitiationActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.tabLayout)
    UltimateTabLayout tabLayout;

    @Nullable
    @BindView(R.id.pager)
    ViewPager pager;

    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setup();
    }

    public ModelDisplay getModelDisplay() {
        return modelDisplay;
    }

    private void setup() {
        if (tabLayout != null) {
            PaymentFragmentTabsAdapter adapter = new PaymentFragmentTabsAdapter(this, getSupportFragmentManager());
            this.modelDisplay = adapter.getModelDetailsFragment();
            pager.setAdapter(adapter);
            tabLayout.setViewPager(pager, adapter);
        } else {
            modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        }
    }
}
