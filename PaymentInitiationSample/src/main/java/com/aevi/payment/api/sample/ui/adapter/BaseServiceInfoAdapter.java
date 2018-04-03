package com.aevi.payment.api.sample.ui.adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aevi.payment.api.sample.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseServiceInfoAdapter<T> extends RecyclerView.Adapter<BaseServiceInfoAdapter.ViewHolder> {

    protected Context context;
    protected T info;
    protected String yes, no;
    protected String[] labels;
    protected int[] resIds;

    public BaseServiceInfoAdapter(Context context, int labelsTypedArrayResource, T info) {
        this.context = context;
        this.info = info;
        TypedArray typedArray = context.getResources().obtainTypedArray(labelsTypedArrayResource);
        this.labels = new String[typedArray.length()];
        this.resIds = new int[typedArray.length()];
        for (int i = 0; i < labels.length; i++) {
            this.labels[i] = typedArray.getString(i);
            this.resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        this.yes = context.getString(R.string.yes);
        this.no = context.getString(R.string.no);
    }

    @Override
    public BaseServiceInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_two_line_item_medium_title, parent, false);
        return new BaseServiceInfoAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return labels.length;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    protected String getArrayValue(String[] array) {
        if (array.length == 0) {
            return context.getString(R.string.na);
        } else if (array.length == 1) {
            return array[0];
        } else {
            return Arrays.toString(array);
        }
    }

    protected String getYesNo(boolean b) {
        if (b) {
            return yes;
        }
        return no;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_title)
        TextView label;

        @BindView(R.id.item_subtitle)
        TextView value;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
