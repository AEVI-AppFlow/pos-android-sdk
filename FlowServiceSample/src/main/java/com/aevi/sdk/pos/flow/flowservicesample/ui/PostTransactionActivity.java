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
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.sdk.pos.flow.stage.PostTransactionModel;

public class PostTransactionActivity extends BaseSampleAppCompatActivity {

    private PostTransactionModel postTransactionModel;
    private ModelDisplay modelDisplay;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_txn);
        ButterKnife.bind(this);
        postTransactionModel = PostTransactionModel.fromActivity(this);
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        if (modelDisplay != null) {
            modelDisplay.showTitle(false);
        }
        setupToolbar(toolbar, R.string.fss_post_payment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay.showTransactionSummary(postTransactionModel.getTransactionSummary());
    }

    @OnClick(R.id.add_payment_references)
    public void onAddPaymentReferences() {
        postTransactionModel.addReferences("someReference", "addedByPostPaymentSample");
        findViewById(R.id.add_payment_references).setEnabled(false);
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        postTransactionModel.sendResponse();
        finish();
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
        return FlowStages.POST_TRANSACTION;
    }

    @Override
    protected Class<?> getRequestClass() {
        return TransactionSummary.class;
    }

    @Override
    protected Class<?> getResponseClass() {
        return FlowResponse.class;
    }

    @Override
    protected String getModelJson() {
        return postTransactionModel.getFlowResponse().toJson();
    }

    @Override
    protected String getRequestJson() {
        return postTransactionModel.getTransactionSummary().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.post_txn_help);
    }
}
