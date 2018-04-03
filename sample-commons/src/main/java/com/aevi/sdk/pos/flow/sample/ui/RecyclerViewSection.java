package com.aevi.sdk.pos.flow.sample.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.aevi.sdk.pos.flow.sample.R;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class RecyclerViewSection extends StatelessSection {

    private final Context context;
    private final String title;
    private final List<Pair<String, String>> keyValueItems;

    public RecyclerViewSection(Context context, int titleRes, List<Pair<String, String>> keyValueItems) {
        this(context, context.getString(titleRes), keyValueItems);
    }

    public RecyclerViewSection(Context context, String title, List<Pair<String, String>> keyValueItems) {
        super(SectionParameters.builder().itemResourceId(R.layout.snippet_list_item)
                .headerResourceId(R.layout.snippet_list_header).build());
        this.context = context;
        this.title = title;
        this.keyValueItems = keyValueItems;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SectionHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
        sectionHeaderViewHolder.title.setText(title);
    }

    @Override
    public int getContentItemsTotal() {
        return keyValueItems.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new LabelValueViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Pair<String, String> labelValue = keyValueItems.get(position);
        LabelValueViewHolder viewHolder = (LabelValueViewHolder) holder;
        viewHolder.label.setText(labelValue.first);
        viewHolder.value.setText(labelValue.second);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorResultStripeEven));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorResultStripeOdd));
        }
    }
}
