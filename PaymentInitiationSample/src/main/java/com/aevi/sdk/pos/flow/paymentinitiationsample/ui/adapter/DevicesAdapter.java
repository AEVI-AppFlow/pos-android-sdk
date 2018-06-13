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


import android.view.View;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.model.Device;
import com.aevi.ui.library.recycler.AbstractListWithMenuAdapter;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;


public class DevicesAdapter extends BaseTwoLineAdapter<Device> {

    public DevicesAdapter(List<Device> items, AbstractListWithMenuAdapter.OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_medium_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, Device device, int i) {
        holder.title.setText(device.getName());
        holder.subtitle.setText(device.getId());
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
