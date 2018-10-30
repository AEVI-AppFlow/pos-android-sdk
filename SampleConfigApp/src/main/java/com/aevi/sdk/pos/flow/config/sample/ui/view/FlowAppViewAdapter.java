package com.aevi.sdk.pos.flow.config.sample.ui.view;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aevi.sdk.app.scanning.model.AppInfoModel;
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
import com.aevi.sdk.pos.flow.config.sample.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aevi.sdk.pos.flow.config.sample.ui.view.IconHelper.getIcon;

public class FlowAppViewAdapter extends AbstractListWithMenuAdapter<AppInfoModel, FlowAppViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final OnFlowSectionListener onFlowSectionListener;

    private static final Map<String, Integer> FLOW_APP_COLOR_MAP = new HashMap<>();
    private static int COLOR_SELECT_POS = 1;
    private static int[] COLOR_SELECT_ARRAY;

    FlowAppViewAdapter(Context context, List<AppInfoModel> objects,
                              OnFlowSectionListener onFlowSectionListener) {
        super(objects, null, false, 0);
        this.onFlowSectionListener = onFlowSectionListener;
        if (COLOR_SELECT_ARRAY == null) {
            COLOR_SELECT_ARRAY = context.getResources().getIntArray(R.array.flowAppColorList);
        }
    }

    @Override
    protected void onBindViewHolderToObject(ViewHolder holder, final AppInfoModel appEntity, int position) {
        holder.title.setText(appEntity.getPaymentFlowServiceInfo().getDisplayName());
        holder.appIcon.setImageDrawable(getIcon(holder.title.getContext(), appEntity));

        if (appEntity.getStages().size() == 0) {
            holder.background.setBackgroundColor(COLOR_SELECT_ARRAY[0]);
        } else {
            holder.background.setBackgroundColor(getFlowAppColor(appEntity));
        }
    }

    private int getFlowAppColor(AppInfoModel flowApp) {
        String id = flowApp.getPaymentFlowServiceInfo().getId();
        if (!FLOW_APP_COLOR_MAP.containsKey(id)) {
            FLOW_APP_COLOR_MAP.put(id, getNextColor());
        }
        return FLOW_APP_COLOR_MAP.get(id);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_flow_config, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(objects, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        objects.remove(position);
        notifyItemRemoved(position);
        onFlowSectionListener.notifyAppsChanged(objects);
    }

    @Override
    public void onMoveComplete() {
        onFlowSectionListener.notifyAppsChanged(objects);
    }

    public interface OnFlowSectionListener {

        void notifyAppsChanged(List<AppInfoModel> flowApps);

    }

    List<AppInfoModel> getFlowApps() {
        return objects;
    }

    private static int getNextColor() {
        if (COLOR_SELECT_POS >= COLOR_SELECT_ARRAY.length) {
            COLOR_SELECT_POS = 1;
        }
        return COLOR_SELECT_ARRAY[COLOR_SELECT_POS++];
    }

    void setApps(List<AppInfoModel> apps) {
        objects = apps;
    }

    class ViewHolder extends AbstractListWithMenuAdapter.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener, ItemTouchHelperViewHolder {

        @BindView(R.id.flow_app_bk)
        LinearLayout background;

        @BindView(R.id.flow_app_label)
        TextView title;

        @BindView(R.id.flow_app_icon)
        ImageView appIcon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.8f);
            title.setAlpha(0.8f);
        }

        @Override
        public void onItemClear() {
            itemView.setAlpha(1);
            title.setAlpha(1);
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.flowItemBackground));
        }
    }
}
