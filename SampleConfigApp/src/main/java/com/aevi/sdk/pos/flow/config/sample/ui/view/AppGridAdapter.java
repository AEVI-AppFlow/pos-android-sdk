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
package com.aevi.sdk.pos.flow.config.sample.ui.view;

import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aevi.sdk.app.scanning.model.AppInfoModel;
import com.aevi.sdk.pos.flow.config.sample.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aevi.sdk.pos.flow.config.sample.ui.view.IconHelper.getIcon;

public class AppGridAdapter extends AbstractListWithMenuAdapter<AppInfoModel, AppGridAdapter.ViewHolder> {

    private OnAppClickChangedListener listener;

    public AppGridAdapter(List<AppInfoModel> apps, OnAppClickChangedListener listener) {
        super(apps, null, false, 0);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolderToObject(ViewHolder holder, AppInfoModel item, int position) {
        holder.image.setImageDrawable(getIcon(holder.image.getContext(), item));
        holder.title.setText(item.getPaymentFlowServiceInfo().getDisplayName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_item_app, parent, false);
        return new ViewHolder(v);
    }

    public interface OnAppClickChangedListener {

        void onAppClicked(AppInfoModel app);
    }

    class ViewHolder extends AbstractListWithMenuAdapter.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.app_icon)
        ImageView image;

        @BindView(R.id.app_text)
        TextView title;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.app_wrapper)
        void onAppClicked() {
            if (listener != null) {
                int position = getAdapterPosition();
                listener.onAppClicked(objects.get(position));
            }
        }
    }
}