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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment.PaymentFragment;
import com.aevi.sdk.pos.flow.sample.ui.ModelDetailsFragment;

public class PaymentFragmentTabsAdapter extends FragmentPagerAdapter {

    private String[] labels;
    private int[] resIds;
    private ModelDetailsFragment modelDetailsFragment;

    public PaymentFragmentTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.fragment_tabs);
        this.labels = new String[typedArray.length()];
        this.resIds = new int[typedArray.length()];
        for (int i = 0; i < labels.length; i++) {
            this.labels[i] = typedArray.getString(i);
            this.resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        modelDetailsFragment = new ModelDetailsFragment();
    }

    public ModelDetailsFragment getModelDetailsFragment() {
        return modelDetailsFragment;
    }

    @Override
    public Fragment getItem(int position) {
        int resId = resIds[position];
        switch (resId) {
            case R.string.setup_model:
                return new PaymentFragment();
            case R.string.view_model:
                return modelDetailsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

}
