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

package com.aevi.sdk.pos.flow.sample.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import com.aevi.sdk.pos.flow.sample.R;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import java.util.List;

public class RecyclerViewSection extends StatelessSection {

    private final Context context;
    private final String title;
    private final List<Pair<String, String>> keyValueItems;
    private final boolean firstHeader;

    public RecyclerViewSection(Context context, int titleRes, List<Pair<String, String>> keyValueItems, boolean firstHeader) {
        this(context, context.getString(titleRes), keyValueItems, firstHeader);
    }

    public RecyclerViewSection(Context context, String title, List<Pair<String, String>> keyValueItems, boolean firstHeader) {
        super(SectionParameters.builder().itemResourceId(R.layout.snippet_list_item)
                      .headerResourceId(R.layout.snippet_list_header).build());
        this.context = context;
        this.title = title;
        this.keyValueItems = keyValueItems;
        this.firstHeader = firstHeader;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SectionHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
        sectionHeaderViewHolder.title.setText(title);
        if (firstHeader) {
            sectionHeaderViewHolder.itemView.setPadding(0, 0, 0, 0);
        }
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
