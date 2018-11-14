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
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.aevi.sdk.flow.model.FlowException;
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
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.ReceiptRequestInitiationActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;
import com.aevi.ui.library.BaseObservableFragment;
import com.aevi.ui.library.DropDownHelper;
import com.aevi.ui.library.recycler.DropDownSpinner;
import io.reactivex.disposables.Disposable;

import static android.content.Intent.*;
import static com.aevi.sdk.flow.constants.AdditionalDataKeys.DATA_KEY_TRANSACTION;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_RECEIPT_DELIVERY;
import static com.aevi.sdk.flow.constants.PaymentMethods.PAYMENT_METHOD_CASH;
import static com.aevi.sdk.flow.constants.ReceiptKeys.*;

public class ReceiptRequestFragment extends BaseObservableFragment {

    @BindView(R.id.request_flow_spinner)
    DropDownSpinner requestFlowSpinner;

    private String selectedApiRequestFlow;
    private ModelDisplay modelDisplay;
    private Request request;

    private PaymentClient paymentClient;
    private Disposable initiateDisposable;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_generic_request;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelDisplay = ((ReceiptRequestInitiationActivity) getActivity()).getModelDisplay();
        final DropDownHelper dropDownHelper = new DropDownHelper(getActivity());
        dropDownHelper.setupDropDown(requestFlowSpinner, R.array.receipt_flows);
        paymentClient = PaymentApi.getPaymentClient(getContext());
    }

    @OnItemSelected(R.id.request_flow_spinner)
    public void onRequestTypeSelection(int position) {
        selectedApiRequestFlow = (String) requestFlowSpinner.getAdapter().getItem(position);
        this.request = createRequest();
        if (request != null && modelDisplay != null) {
            modelDisplay.showRequest(request);
        }
    }

    @OnClick(R.id.send)
    public void onProcessRequest() {
        if (request != null) {
            Intent intent = new Intent(getContext(), GenericResultActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_NO_ANIMATION);
            initiateDisposable = paymentClient.initiateRequest(request)
                    .subscribe(response -> {
                        if (isAdded()) {
                            intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY, response.toJson());
                            startActivity(intent);
                        }
                    }, throwable -> {
                        Response response;
                        if (throwable instanceof FlowException) {
                            response = new Response(request, false, ((FlowException) throwable).getErrorCode()
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
        final String redeliver = getString(R.string.receipts_redeliver);
        String cash = getString(R.string.receipts_cash);

        Request request = new Request(FLOW_TYPE_RECEIPT_DELIVERY);
        PaymentResponse lastResponse = SampleContext.getInstance(getContext()).getLastReceivedPaymentResponse();

        // Some types require additional information
        if (selectedApiRequestFlow.equals(redeliver)) {
            if (lastResponse == null || lastResponse.getTransactions().isEmpty() || !lastResponse.getTransactions().get(0).hasResponses()) {
                Toast.makeText(getContext(), "Please complete a successful payment before using this request type", Toast.LENGTH_SHORT).show();
                return null;
            }
            request.addAdditionalData(DATA_KEY_TRANSACTION, lastResponse.getTransactions().get(0));
        } else if (selectedApiRequestFlow.equals(cash)) {
            request.addAdditionalData(RECEIPT_AMOUNTS, new Amounts(15000, "EUR"));
            request.addAdditionalData(RECEIPT_PAYMENT_METHOD, PAYMENT_METHOD_CASH);
            request.addAdditionalData(RECEIPT_OUTCOME, TransactionResponse.Outcome.APPROVED.name());
        }
        return request;
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
