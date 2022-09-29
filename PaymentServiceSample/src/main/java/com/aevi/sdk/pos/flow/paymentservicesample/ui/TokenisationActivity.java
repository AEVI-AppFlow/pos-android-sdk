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
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.FlowTypes;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.BaseSampleAppCompatActivity;
import com.aevi.ui.library.recycler.DropDownSpinner;

public class TokenisationActivity extends BaseSampleAppCompatActivity {

    @BindView(R.id.card_scheme_spinner)
    DropDownSpinner cardSchemeSpinner;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GenericStageModel genericStageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_token);
        ButterKnife.bind(this);
        genericStageModel = GenericStageModel.fromActivity(this);
        setupToolbar(toolbar, R.string.pss_select_token);
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
        Response response = new Response(genericStageModel.getRequest(), token != null,
                                         token != null ? "Sample token generated" : "Failed to generate sample token");
        response.addAdditionalData(AdditionalDataKeys.DATA_KEY_TOKEN, token);
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
        Response response = new Response(genericStageModel.getRequest(), true, "Sample token generated");
        response.addAdditionalData(AdditionalDataKeys.DATA_KEY_TOKEN, CustomerProducer.CUSTOMER_TOKEN);
        return response.toJson();
    }

    @Override
    protected String getRequestJson() {
        return genericStageModel.getRequest().toJson();
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.token_help);
    }
}
