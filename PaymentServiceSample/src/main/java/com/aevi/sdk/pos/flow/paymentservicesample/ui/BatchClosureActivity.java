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
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.constants.FlowTypes;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;

public class BatchClosureActivity extends BaseSampleAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GenericStageModel genericStageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_batch);
        ButterKnife.bind(this);
        genericStageModel = GenericStageModel.fromActivity(this);
        setupToolbar(toolbar, R.string.pss_batch_closure);
    }

    @OnClick(R.id.reply_with_success)
    public void onRespondWithToken() {
        sendTokenResponseAndFinish(true);
    }

    @OnClick(R.id.reply_with_failure)
    public void onRespondWithFailure() {
        sendTokenResponseAndFinish(false);
    }

    private void sendTokenResponseAndFinish(Boolean success) {
        Response response = new Response(genericStageModel.getRequest(), success,
                                         success ? "Batch closed successfully" : "Failed to close batch");
        genericStageModel.sendResponse(response);
        finish();
    }

    @Override
    protected boolean showFlowStagesOption() {
        return false;
    }

    @Override
    protected int getPrimaryColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected String getCurrentStage() {
        return FlowTypes.FLOW_TYPE_TOKENISATION;
    }

    @Override
    protected Class<?> getRequestClass() {
        return Request.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return Response.class;
    }

    @Override
    protected String getModelJson() {
        Response response = new Response(genericStageModel.getRequest(), true, "Batch closed successfully");
        return response.toJson();
    }

    @Override
    protected String getRequestJson() {
        return genericStageModel.getRequest().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.batch_help);
    }
}
