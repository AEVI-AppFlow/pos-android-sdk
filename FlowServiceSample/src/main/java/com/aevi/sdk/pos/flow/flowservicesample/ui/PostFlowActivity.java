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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.PostFlowModel;

import static com.aevi.sdk.flow.constants.FlowStages.POST_FLOW;

public class PostFlowActivity extends BaseSampleAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PostFlowModel postFlowModel;
    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_flow);
        ButterKnife.bind(this);
        setupToolbar(toolbar, R.string.fss_post_flow);
        postFlowModel = PostFlowModel.fromActivity(this);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        modelDisplay.showTitle(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay.showPaymentResponse(postFlowModel.getPaymentResponse());
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        postFlowModel.sendResponse();
        finish();
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
        return POST_FLOW;
    }

    @Override
    protected Class<?> getRequestClass() {
        return PaymentResponse.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return Object.class;
    }

    @Override
    protected String getModelJson() {
        return null;
    }

    @Override
    protected String getRequestJson() {
        return postFlowModel.getPaymentResponse().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.post_flow_help);
    }
}
