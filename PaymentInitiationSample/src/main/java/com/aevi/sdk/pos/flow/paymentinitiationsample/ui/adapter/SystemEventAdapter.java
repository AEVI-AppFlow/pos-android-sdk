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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.ui.library.recycler.AbstractListWithMenuAdapter;

import java.util.List;

public class SystemEventAdapter extends AbstractListWithMenuAdapter<FlowEvent, SystemEventAdapter.ViewHolder> {

    public SystemEventAdapter(List<FlowEvent> items, OnItemSelectedListener listener) {
        super(items, listener, false, 0);
    }

    @Override
    protected void onBindViewHolderToObject(SystemEventAdapter.ViewHolder viewHolder, FlowEvent flowEvent, int i) {
        viewHolder.eventType.setText(flowEvent.getType());
        if (flowEvent.getEventTrigger() != null) {
            viewHolder.eventTrigger.setText("Triggered by: " + flowEvent.getEventTrigger());
        } else {
            viewHolder.eventTrigger.setVisibility(View.GONE);
        }
        StringBuilder eventData = new StringBuilder();
        for (String key : flowEvent.getData().getKeys()) {
            eventData.append(key);
            eventData.append(" : ");
            eventData.append(flowEvent.getData().getValue(key, "Unknown type").toString());
            eventData.append("\n");
        }
        viewHolder.eventData.setText(eventData.toString());
    }

    public SystemEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_event_info, parent, false);
        return new SystemEventAdapter.ViewHolder(v);
    }

    public class ViewHolder extends AbstractListWithMenuAdapter.ViewHolder {
        public final TextView eventType;
        public final TextView eventTrigger;
        public final TextView eventData;

        ViewHolder(View view) {
            super(view);
            this.eventType = view.findViewById(R.id.event_type);
            this.eventTrigger = view.findViewById(R.id.event_trigger);
            this.eventData = view.findViewById(R.id.event_data);
        }
    }
}
