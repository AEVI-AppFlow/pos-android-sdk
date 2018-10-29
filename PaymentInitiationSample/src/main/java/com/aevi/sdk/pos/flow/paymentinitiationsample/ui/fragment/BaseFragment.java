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

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.ui.library.BaseObservableFragment;

abstract class BaseFragment extends BaseObservableFragment {

    protected void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager serviceInfoViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(serviceInfoViewLayoutManager);
        DividerItemDecoration did = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(did);
    }

    protected SampleContext getSampleContext() {
        return SampleContext.getInstance(getContext());
    }
}

