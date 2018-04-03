package com.aevi.payment.api.sample.ui.fragment;


import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aevi.payment.api.sample.R;
import com.aevi.ui.library.recycler.AbstractListWithMenuAdapter;

import butterknife.BindView;

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
    public void onResume() {
        super.onResume();
        setupRecyclerView(items);
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
