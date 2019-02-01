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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;
import android.view.View;
import com.aevi.sdk.flow.constants.AmountIdentifiers;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;

public class PaymentResponsesAdapter extends BaseTwoLineAdapter<PaymentResponse> {

    public PaymentResponsesAdapter(List<PaymentResponse> items, OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_medium_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, PaymentResponse paymentResponse, int i) {
        Context context = holder.title.getContext();
        Amounts amounts = paymentResponse.getTotalAmountsProcessed();
        holder.title.setText(context.getString(R.string.payment_response_title,
                                               paymentResponse.getOriginatingPayment().getFlowType().toUpperCase(),
                                               paymentResponse.getOutcome().name()));
        holder.subtitle.setText(context.getString(R.string.payment_response_totals,
                                                  AmountFormatter.formatAmount(amounts.getCurrency(), amounts.getBaseAmountValue()),
                                                  AmountFormatter.formatAmount(amounts.getCurrency(),
                                                                               amounts.getAdditionalAmountValue(AmountIdentifiers.AMOUNT_TIP)),
                                                  AmountFormatter.formatAmount(amounts.getCurrency(),
                                                                               amounts.getAdditionalAmountValue(AmountIdentifiers.AMOUNT_CASHBACK))
        ));
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
