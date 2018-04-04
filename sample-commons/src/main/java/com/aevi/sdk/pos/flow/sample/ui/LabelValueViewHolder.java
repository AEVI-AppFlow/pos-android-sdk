package com.aevi.sdk.pos.flow.sample.ui;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.sample.R;

public class LabelValueViewHolder extends RecyclerView.ViewHolder {

    public TextView label;
    public TextView value;

    LabelValueViewHolder(View view) {
        super(view);
        label = view.findViewById(R.id.label);
        value = view.findViewById(R.id.value);
    }
}
