package com.aevi.sdk.pos.flow.flowservicesample.ui.adapter;


import android.content.Context;

import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.ui.library.recycler.LabelsListAdapter;

public class PaymentPropertiesAdapter extends LabelsListAdapter {

    private Payment payment;

    public PaymentPropertiesAdapter(Context context, int labelsArrayRes) {
        super(context, labelsArrayRes);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }
}
