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
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public abstract class AbstractListWithMenuAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    List<T> objects;
    private OnItemSelectedListener listener;
    private int menuResId;
    private final boolean withContextMenu;

    @SuppressWarnings("unchecked")
    public abstract class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            if (withContextMenu) {
                view.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null) {
                listener.onItemSelected(objects.get(position));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(menuResId, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null) {
                T object = objects.get(getAdapterPosition());
                listener.onMenuItemAction(object, item);
            }
            return false;
        }
    }

    AbstractListWithMenuAdapter(List<T> objects, OnItemSelectedListener listener, boolean withContextMenu, int menuResId) {
        this.listener = listener;
        this.objects = objects;
        this.menuResId = menuResId;
        this.withContextMenu = withContextMenu;
    }

    public interface OnItemSelectedListener<T> {

        void onItemSelected(T object);

        void onMenuItemAction(T object, MenuItem item);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        T object = objects.get(position);
        onBindViewHolderToObject(holder, object, position);
    }

    protected abstract void onBindViewHolderToObject(V holder, T object, int position);

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
