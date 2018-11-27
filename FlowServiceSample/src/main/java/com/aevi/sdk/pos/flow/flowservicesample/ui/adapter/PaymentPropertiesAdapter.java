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

package com.aevi.sdk.pos.flow.flowservicesample.ui.adapter;


import android.content.Context;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.ui.library.recycler.LabelsListAdapter;

public class PaymentPropertiesAdapter extends LabelsListAdapter {

    private Payment payment;

    public PaymentPropertiesAdapter(Context context, int labelsArrayRes) {
        super(context, labelsArrayRes);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }
}
