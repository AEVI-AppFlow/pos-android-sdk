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
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment.GenericRequestFragment;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

public class RequestInitiationActivity extends BaseSampleAppCompatActivity {

    private ModelDisplay modelDisplay;

    private GenericRequestFragment genericRequestFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_layout)
    FrameLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        genericRequestFragment = (GenericRequestFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_request);
        setupToolbar(toolbar, R.string.initiate_request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressLayout.setVisibility(View.GONE);
    }

    public void showProgressOverlay() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    public ModelDisplay getModelDisplay() {
        return modelDisplay;
    }

    @Override
    protected String getModelTitle() {
        return getString(R.string.request_data);
    }

    @Override
    protected boolean showFlowStagesOption() {
        return false;
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
        return "Generic initiation";
    }

    @Override
    protected Class<?> getRequestClass() {
        return Request.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return Request.class;
    }

    @Override
    protected String getModelJson() {
        return genericRequestFragment.getRequest().toJson();
    }

    @Override
    protected String getRequestJson() {
        return null;
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.generic_initiation_help);
    }

    @Override
    protected boolean allowBack() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressLayout.setVisibility(View.GONE);
    }
}
