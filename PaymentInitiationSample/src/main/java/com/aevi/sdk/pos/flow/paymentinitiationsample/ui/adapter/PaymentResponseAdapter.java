package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;

import android.content.Context;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.ui.library.recycler.LabelsListAdapter;

public class PaymentResponseAdapter extends LabelsListAdapter {

    private final PaymentResponse response;
    private final Context context;

    public PaymentResponseAdapter(Context context, PaymentResponse response) {
        super(context, R.array.payment_response_labels);
        this.context = context;
        this.response = response;
    }

    @Override
    public void onBindViewHolder(LabelsListAdapter.ViewHolder holder, int position) {
        int label = resIds[position];
        holder.label.setText(label);
        String value = "";
        switch (label) {
            case R.string.transaction_type:
                long total = response.getOriginatingPayment().getAmounts().getTotalAmountValue();
                value = AmountFormatter.formatAmount(response.getOriginatingPayment().getAmounts().getCurrency(), total);
                holder.label.setText(getFriendlyTypeName(response.getOriginatingPayment().getTransactionType()));
                break;
            case R.string.overall_status:
                value = getYesNo(response.isAllTransactionsApproved());
                break;
            case R.string.num_transactions:
                value = String.valueOf(response.getTransactions().size());
                break;
            case R.string.outcome:
                value = response.getOutcome().name();
                break;
            case R.string.failure_reason:
                value = response.getFailureReason().name();
                break;
        }
        holder.value.setText(value);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorResultStripeEven));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorResultStripeOdd));
        }
    }

    private String getFriendlyTypeName(String transactionType) {
        return context.getString(
                context.getResources().getIdentifier("transaction_type_" + transactionType.toLowerCase(), "string", context.getPackageName()));
    }
}