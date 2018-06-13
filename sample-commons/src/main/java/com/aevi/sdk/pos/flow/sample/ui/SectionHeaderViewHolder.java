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

package com.aevi.sdk.pos.flow.sample.ui;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.sample.R;

public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public SectionHeaderViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.section_title);
    }
}
