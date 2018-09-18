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
import android.widget.Toast;

import com.aevi.android.rxmessenger.MessageException;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.constants.ReceiptKeys;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.GenericResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.RequestInitiationActivity;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.BaseObservableFragment;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class GenericRequestFragment extends BaseObservableFragment {

    private static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";

    @BindView(R.id.request_type_spinner)
    DropDownSpinner requestTypeSpinner;

    private String selectedApiRequestType;
    private ModelDisplay modelDisplay;
    private Request request;

    private PaymentClient paymentClient;

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

        // TODO this isn't right - it needs to use the new "custom request types" concept once available
        SampleContext.getInstance(getActivity()).getPaymentClient().getPaymentFlowServices()
                .subscribe(paymentFlowServices -> {
                    Set<String> requestTypes = paymentFlowServices.getAllSupportedRequestTypes();
                    requestTypes.remove(FinancialRequestTypes.PAYMENT); // Filter out "payment" here, as we handle that via PaymentFragment
                    requestTypes.add("unsupportedType"); // For illustration of what happens if you initiate a request with unsupported type
                    dropDownHelper.setupDropDown(requestTypeSpinner, new ArrayList<>(requestTypes), false);
                }, throwable -> dropDownHelper.setupDropDown(requestTypeSpinner, R.array.request_types));
    }

    @OnItemSelected(R.id.request_type_spinner)
    public void onRequestTypeSelection(int position) {
        selectedApiRequestType = (String) requestTypeSpinner.getAdapter().getItem(position);
        this.request = createRequest();
        if (request != null && modelDisplay != null) {
            modelDisplay.showRequest(request);
        }
    }

    @OnClick(R.id.send)
    public void onProcessRequest() {
        if (request != null) {
            Intent intent = new Intent(getContext(), GenericResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            paymentClient.processRequest(request).subscribe(response -> {
                if (isAdded()) {
                    intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY, response.toJson());
                    startActivity(intent);
                }
            }, throwable -> {
                Response response;
                if (throwable instanceof MessageException) {
                    response = new Response(request, false, ((MessageException) throwable).getCode()
                            + " : " + throwable.getMessage());
                } else {
                    response = new Response(request, false, throwable.getMessage());
                }
                if (isAdded()) {
                    intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY, response.toJson());
                    startActivity(intent);
                }
            });
        }
    }

    private Request createRequest() {
        Request request = new Request(selectedApiRequestType);
        PaymentResponse lastResponse = SampleContext.getInstance(getContext()).getLastReceivedPaymentResponse();

        // Some types require additional information
        switch (selectedApiRequestType) {
            case FinancialRequestTypes.REVERSAL:
            case FinancialRequestTypes.RESPONSE_REDELIVERY:
                if (lastResponse == null) {
                    Toast.makeText(getContext(), "Please complete a successful payment before using this request type", Toast.LENGTH_SHORT).show();
                    return null;
                }
                request.addAdditionalData(AdditionalDataKeys.DATA_KEY_TRANSACTION_ID, lastResponse.getTransactions().get(0).getId());
                break;
            case FinancialRequestTypes.CASH_RECEIPT_DELIVERY:
                Amounts cashAmounts = new Amounts(15000, "EUR");
                String paymentMethod = PaymentMethods.CASH;
                String outcome = TransactionResponse.Outcome.APPROVED.name();
                request.addAdditionalData(ReceiptKeys.RECEIPT_AMOUNTS, cashAmounts);
                request.addAdditionalData(ReceiptKeys.RECEIPT_PAYMENT_METHOD, paymentMethod);
                request.addAdditionalData(ReceiptKeys.RECEIPT_OUTCOME, outcome);
                break;
            case SHOW_LOYALTY_POINTS_REQUEST:
                request.addAdditionalData(AdditionalDataKeys.DATA_KEY_CUSTOMER, CustomerProducer.getDefaultCustomer("Payment Initiation Sample"));
                break;
            default:
                // No extra data required
                break;
        }
        return request;
    }
}
