package com.aevi.sdk.pos.flow.sample.ui;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.sample.R;

public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public SectionHeaderViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.section_title);
    }
}
