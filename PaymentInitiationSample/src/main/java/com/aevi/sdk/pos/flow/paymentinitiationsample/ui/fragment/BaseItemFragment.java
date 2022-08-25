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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.ui.library.recycler.AbstractListWithMenuAdapter;

public abstract class BaseItemFragment<T> extends BaseFragment implements AbstractListWithMenuAdapter.OnItemSelectedListener<T> {

    @BindView(R.id.items)
    RecyclerView items;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.no_items)
    TextView noItemsText;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        setupRecyclerView(items);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupItems();
    }

    protected abstract void setupItems();

    protected void showNoItemsAvailable(int resId) {
        noItemsText.setVisibility(View.VISIBLE);
        noItemsText.setText(resId);
        items.setVisibility(View.GONE);
    }

    @Override
    public void onMenuItemAction(T t, MenuItem menuItem) {

    }

}
