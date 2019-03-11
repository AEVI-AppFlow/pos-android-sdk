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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.model.config.FlowConfigurations;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.RequestInitiationActivity;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.BaseObservableFragment;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_TRANSACTION;
import static com.aevi.sdk.flow.constants.ErrorConstants.PROCESSING_SERVICE_BUSY;
import static com.aevi.sdk.flow.constants.FlowTypes.*;
import static com.aevi.sdk.flow.constants.PaymentMethods.PAYMENT_METHOD_CASH;
import static com.aevi.sdk.flow.constants.ReceiptKeys.*;
import static com.aevi.sdk.flow.constants.StatusUpdateKeys.STATUS_UPDATE_BASKET_MODIFIED;

public class GenericRequestFragment extends BaseObservableFragment {

    private static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";
    private static final String UNSUPPORTED_FLOW = "unsupportedFlowType";

    @BindView(R.id.request_flow_spinner)
    DropDownSpinner requestFlowSpinner;

    @BindView(R.id.sub_type_spinner)
    DropDownSpinner subTypeSpinner;

    @BindView(R.id.subtype_header)
    TextView subTypeHeader;

    @BindView(R.id.send)
    Button sendButton;

    @BindView(R.id.message)
    TextView messageView;

    @BindView(R.id.process_background)
    CheckBox processInBackground;

    private String selectedApiRequestFlow;
    private String selectedSubType;
    private ModelDisplay modelDisplay;
    private Request genericRequest;

    private PaymentClient paymentClient;
    private PaymentSettings paymentSettings;
    private Disposable initiateDisposable;
    private DropDownHelper dropDownHelper;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_generic_request;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelDisplay = ((RequestInitiationActivity) getActivity()).getModelDisplay();
        dropDownHelper = new DropDownHelper(getActivity());
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
                }, this::handleError);
    }

    @OnItemSelected(R.id.request_flow_spinner)
    public void onRequestTypeSelection(int position) {
        selectedApiRequestFlow = (String) requestFlowSpinner.getAdapter().getItem(position);
        updateState(true);
    }

    private void updateState(boolean checkSubTypes) {
        this.genericRequest = createRequest();
        if (genericRequest != null && modelDisplay != null) {
            modelDisplay.showRequest(genericRequest);
        }
        if (genericRequest != null) {
            messageView.setText("");
            setViewsEnabled(true);
        } else {
            setViewsEnabled(false);
            if (modelDisplay != null) {
                modelDisplay.showRequest(new Request(selectedApiRequestFlow));
            }
        }
        if (checkSubTypes) {
            checkSubTypeSelection(selectedApiRequestFlow);
        }
    }

    private void checkSubTypeSelection(String type) {
        switch (type) {
            case FLOW_TYPE_RECEIPT_DELIVERY:
                setupSubTypes(type, R.array.receipt_flows);
                break;
            default:
                hideSubTypes();
        }
    }

    private void setupSubTypes(String type, int subTypesArray) {
        subTypeSpinner.setVisibility(View.VISIBLE);
        subTypeHeader.setVisibility(View.VISIBLE);
        subTypeHeader.setText(getString(R.string.choose_sub_type, type));
        dropDownHelper.setupDropDown(subTypeSpinner, subTypesArray, false);
    }

    @OnItemSelected(R.id.sub_type_spinner)
    public void onSubTypeSelection(int position) {
        selectedSubType = (String) subTypeSpinner.getAdapter().getItem(position);
        updateState(false);
    }

    @OnCheckedChanged(R.id.process_background)
    public void onProcessBackgroundStateChanged(CheckBox checkBox, boolean processInBackground) {
        updateState(false);
    }

    private void hideSubTypes() {
        subTypeSpinner.setVisibility(View.GONE);
        subTypeHeader.setVisibility(View.GONE);
    }

    @OnClick(R.id.send)
    public void onProcessRequest() {
        if (genericRequest != null) {
            // Responses come in via ResponseListenerService, and starts the GenericResultActivity from there
            initiateDisposable = paymentClient.initiateRequest(genericRequest)
                    .subscribe(() -> {
                        messageView.setText(R.string.request_accepted);
                        ((RequestInitiationActivity) getActivity()).showProgressOverlay();
                    }, this::handleError);
        }
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof FlowException) {
            FlowException flowException = (FlowException) throwable;
            if (!flowException.getErrorCode().equals(PROCESSING_SERVICE_BUSY)) {
                Intent errorIntent = new Intent(getContext(), PaymentResultActivity.class);
                errorIntent.putExtra(PaymentResultActivity.ERROR_KEY, flowException.toJson());
                startActivity(errorIntent);
            } else {
                showErrorToast("Processing service busy", throwable);
            }
        } else {
            showErrorToast("Unrecoverable error occurred - see logs", throwable);
        }
    }

    private void showErrorToast(String message, Throwable throwable) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Log.e(PaymentFragment.class.getSimpleName(), "Error", throwable);
    }

    private Request createRequest() {
        if (paymentSettings == null) {
            return null; // Wait for settings to come back first
        }
        PaymentResponse lastResponse = SampleContext.getInstance(getContext()).getLastReceivedPaymentResponse();

        // Some types require additional information
        Request request = null;
        switch (selectedApiRequestFlow) {
            case FLOW_TYPE_BASKET_STATUS_UPDATE: {
                request = new Request(selectedApiRequestFlow);
                Basket basket = new Basket("sampleBasket");
                basket.addItems(new BasketItemBuilder().withLabel("item").withAmount(200).build());
                request.addAdditionalData(STATUS_UPDATE_BASKET_MODIFIED, basket);
                break;
            }
            case FLOW_TYPE_REVERSAL: {
                TransactionResponse paymentAppResponse = checkForPaymentAppResponse(lastResponse);
                if (paymentAppResponse == null) {
                    return null;
                }
                request = new Request(selectedApiRequestFlow, paymentAppResponse.getReferences());
                break;
            }
            case FLOW_TYPE_RECEIPT_DELIVERY: {
                if (selectedSubType == null) {
                    return null;
                }
                TransactionResponse paymentAppResponse = checkForPaymentAppResponse(lastResponse);
                if (paymentAppResponse == null) {
                    return null;
                }
                String redeliver = getString(R.string.receipts_redeliver);
                String cash = getString(R.string.receipts_cash);
                // Some types require additional information
                if (selectedSubType.equals(redeliver)) {
                    request = new Request(selectedApiRequestFlow);
                    request.addAdditionalData(DATA_KEY_TRANSACTION, lastResponse.getTransactions().get(0));
                } else if (selectedSubType.equals(cash)) {
                    request = new Request(selectedApiRequestFlow);
                    request.addAdditionalData(RECEIPT_AMOUNTS, new Amounts(15000, "EUR"));
                    request.addAdditionalData(RECEIPT_PAYMENT_METHOD, PAYMENT_METHOD_CASH);
                    request.addAdditionalData(RECEIPT_OUTCOME, TransactionResponse.Outcome.APPROVED.name());
                }
                break;
            }
            case SHOW_LOYALTY_POINTS_REQUEST: {
                request = new Request(selectedApiRequestFlow);
                request.addAdditionalData("customer", CustomerProducer.getDefaultCustomer("Payment Initiation Sample"));
                break;
            }
            default:
                // No extra data required
                break;
        }
        return request;
    }

    private TransactionResponse checkForPaymentAppResponse(PaymentResponse lastResponse) {
        if (lastResponse == null || lastResponse.getTransactions().isEmpty() || !lastResponse.getTransactions().get(0).hasResponses()
                || lastResponse.getTransactions().get(0).getPaymentAppResponse() == null) {
            messageView.setText(R.string.please_complete_payment_first);
            return null;
        }
        return lastResponse.getTransactions().get(0).getPaymentAppResponse();
    }

    private void setViewsEnabled(boolean enabled) {
        sendButton.setEnabled(enabled);
    }

    public Request getRequest() {
        return genericRequest;
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
