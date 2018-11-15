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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.Basket;
import com.aevi.sdk.pos.flow.model.BasketItemBuilder;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.config.FlowConfigurations;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.GenericResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.RequestInitiationActivity;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.BaseObservableFragment;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.*;
import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_TRANSACTION_ID;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_BASKET_STATUS_UPDATE;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_REVERSAL;
import static com.aevi.sdk.flow.constants.StatusUpdateKeys.STATUS_UPDATE_BASKET_MODIFIED;

public class GenericRequestFragment extends BaseObservableFragment {

    private static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";
    private static final String UNSUPPORTED_FLOW = "unsupportedFlowType";

    @BindView(R.id.request_flow_spinner)
    DropDownSpinner requestFlowSpinner;

    @BindView(R.id.send)
    Button sendButton;

    @BindView(R.id.message)
    TextView messageView;

    private String selectedApiRequestFlow;
    private ModelDisplay modelDisplay;
    private Request genericRequest;

    private PaymentClient paymentClient;
    private PaymentSettings paymentSettings;
    private Disposable initiateDisposable;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_generic_request;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelDisplay = ((RequestInitiationActivity) getActivity()).getModelDisplay();
        final DropDownHelper dropDownHelper = new DropDownHelper(getActivity());
        paymentClient = PaymentApi.getPaymentClient(getContext());

        PaymentClient paymentClient = SampleContext.getInstance(getActivity()).getPaymentClient();
        paymentClient.getPaymentSettings()
                .subscribe(paymentSettings -> {
                    FlowConfigurations flowConfigurations = paymentSettings.getFlowConfigurations();
                    GenericRequestFragment.this.paymentSettings = paymentSettings;
                    List<String> flowTypes = new ArrayList<>();
                    String[] knownGenericTypes = getResources().getStringArray(R.array.request_flows);
                    for (String supportedGenericType : knownGenericTypes) {
                        if (flowConfigurations.isFlowTypeSupported(supportedGenericType)) {
                            flowTypes.add(supportedGenericType);
                        }
                    }
                    flowTypes.add(UNSUPPORTED_FLOW); // For illustration of what happens if you initiate a request with unsupported flow
                    dropDownHelper.setupDropDown(requestFlowSpinner, flowTypes, false);
                }, throwable -> dropDownHelper.setupDropDown(requestFlowSpinner, R.array.request_flows));
    }

    @OnItemSelected(R.id.request_flow_spinner)
    public void onRequestTypeSelection(int position) {
        selectedApiRequestFlow = (String) requestFlowSpinner.getAdapter().getItem(position);
        this.genericRequest = createRequest();
        if (genericRequest != null && modelDisplay != null) {
            modelDisplay.showRequest(genericRequest);
        }
        if (genericRequest != null) {
            messageView.setText("");
            setViewsEnabled(true);
        } else {
            setViewsEnabled(false);
        }
    }

    @OnClick(R.id.send)
    public void onProcessRequest() {
        if (genericRequest != null) {
            // Responses come in via ResponseListenerService, and starts the GenericResultActivity from there
            initiateDisposable = paymentClient.initiateRequest(genericRequest)
                    .subscribe(() -> {
                                   messageView.setText("Request accepted by FPS");
                               },
                               throwable -> {
                                   Intent intent = new Intent(getContext(), GenericResultActivity.class);
                                   intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_REORDER_TO_FRONT |
                                                           FLAG_ACTIVITY_NO_ANIMATION);
                                   Response response;
                                   if (throwable instanceof FlowException) {
                                       response = new Response(genericRequest, false, ((FlowException) throwable).getErrorCode()
                                               + " : " + throwable.getMessage());
                                   } else {
                                       response = new Response(genericRequest, false, throwable.getMessage());
                                   }
                                   if (isAdded()) {
                                       intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY, response.toJson());
                                       startActivity(intent);
                                   }
                               });
        }
    }

    private Request createRequest() {
        if (paymentSettings == null) {
            return null; // Wait for settings to come back first
        }
        Request request = new Request(selectedApiRequestFlow);
        PaymentResponse lastResponse = SampleContext.getInstance(getContext()).getLastReceivedPaymentResponse();

        // Some types require additional information
        switch (request.getRequestType()) {
            case FLOW_TYPE_BASKET_STATUS_UPDATE:
                Basket basket = new Basket("sampleBasket");
                basket.addItems(new BasketItemBuilder().withLabel("item").withAmount(200).build());
                request.addAdditionalData(STATUS_UPDATE_BASKET_MODIFIED, basket);
                break;
            case FLOW_TYPE_REVERSAL:
                if (lastResponse == null || lastResponse.getTransactions().isEmpty() || !lastResponse.getTransactions().get(0).hasResponses()) {
                    messageView.setText("Please complete a successful payment before using this request type");
                    return null;
                }
                request.addAdditionalData(DATA_KEY_TRANSACTION_ID, lastResponse.getTransactions().get(0).getLastResponse().getId());
                break;
            case SHOW_LOYALTY_POINTS_REQUEST:
                request.addAdditionalData("customer", CustomerProducer.getDefaultCustomer("Payment Initiation Sample"));
                break;
            default:
                // No extra data required
                break;
        }
        return request;
    }

    private void setViewsEnabled(boolean enabled) {
        sendButton.setEnabled(enabled);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (initiateDisposable != null) {
            initiateDisposable.dispose();
            initiateDisposable = null;
        }
    }
}
