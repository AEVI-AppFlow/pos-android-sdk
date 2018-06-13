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
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.pos.flow.model.Transaction;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final Context context;
    private final List<Transaction> transactions;
    private final Customer customer;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.amount_charged)
        TextView amountCharged;

        @BindView(R.id.customer)
        TextView customer;

        @BindView(R.id.transaction_responses)
        RecyclerView transactionResponses;

        @BindView(R.id.customer_container)
        ViewGroup customerContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            transactionResponses.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }

    public TransactionAdapter(Context context, List<Transaction> transactions, Customer customer) {
        this.context = context;
        this.transactions = transactions;
        this.customer = customer;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_transaction, parent, false);
        return new TransactionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);

        holder.transactionResponses
                .setAdapter(new TransactionResponseAdapter(holder.transactionResponses.getContext(), transaction.getTransactionResponses()));

        holder.amountCharged.setText(
                AmountFormatter.formatAmount(transaction.getProcessedAmounts().getCurrency(), transaction.getProcessedAmounts().getTotalAmountValue()));
        holder.amountCharged.setVisibility(View.VISIBLE);

        if (customer != null) {
            holder.customer.setText(customer.getFullName());
            holder.customerContainer.setVisibility(View.VISIBLE);
        } else {
            holder.customerContainer.setVisibility(View.GONE);
        }
    }

    private String getString(int resId, @NonNull Object... params) {
        return context.getString(resId, params);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
