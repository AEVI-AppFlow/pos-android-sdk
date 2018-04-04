package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.constants.CardDataKeys;
import com.aevi.sdk.flow.constants.ReferenceKeys;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionResponseAdapter extends RecyclerView.Adapter<TransactionResponseAdapter.ViewHolder> {

    private final Context context;
    private final List<TransactionResponse> transactionResponses;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.payment_app)
        TextView paymentApp;

        @BindView(R.id.payment_app_container)
        ViewGroup paymentAppContainer;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.response_code)
        TextView responseCode;

        @BindView(R.id.amount_charged)
        TextView amountCharged;

        @BindView(R.id.card_container)
        ViewGroup cardContainer;

        @BindView(R.id.card_used)
        TextView cardUsed;

        @BindView(R.id.payment_type)
        TextView paymentType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TransactionResponseAdapter(Context context, List<TransactionResponse> transactionResponses) {
        this.context = context;
        this.transactionResponses = transactionResponses;
    }

    @Override
    public TransactionResponseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_transaction_response, parent, false);
        return new TransactionResponseAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TransactionResponse response = transactionResponses.get(position);

        holder.responseCode.setText(response.getResponseCode());
        if (response.getAmountsProcessed() != null) {
            holder.amountCharged.setText(
                    AmountFormatter.formatAmount(response.getAmountsProcessed().getCurrency(), response.getAmountsProcessed().getTotalAmountValue()));
            holder.amountCharged.setVisibility(View.VISIBLE);

            String paymentMethod = response.getPaymentMethod() != null ? response.getPaymentMethod() : "Unknown";
            holder.paymentType.setText(paymentMethod);
            holder.paymentType.setVisibility(View.VISIBLE);
        }

        String paymentService = response.getReferences().getValue(ReferenceKeys.PAYMENT_SERVICE, String.class);
        if (paymentService != null) {
            holder.paymentApp.setText(paymentService);
            holder.paymentAppContainer.setVisibility(View.VISIBLE);
        } else {
            holder.paymentAppContainer.setVisibility(View.GONE);
        }

        holder.status.setText(response.getOutcome().name());
        if (response.getCard() != null && response.getCard().getMaskedPan() != null) {
            Card card = response.getCard();
            String cardNetwork = card.getAdditionalData().getValue(CardDataKeys.NETWORK, String.class);
            cardNetwork = cardNetwork == null ? "N/A" : cardNetwork;
            holder.cardUsed.setText(getString(R.string.card_used_value, cardNetwork, card.getMaskedPan(), card.getExpiryDate()));
            holder.cardUsed.setVisibility(View.VISIBLE);
            holder.cardContainer.setVisibility(View.VISIBLE);
        } else {
            holder.cardContainer.setVisibility(View.GONE);
        }
    }

    private String getString(int resId, @NonNull Object... params) {
        return context.getString(resId, params);
    }

    @Override
    public int getItemCount() {
        return transactionResponses.size();
    }
}
