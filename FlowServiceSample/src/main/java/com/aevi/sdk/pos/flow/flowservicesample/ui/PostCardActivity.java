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

package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.pos.flow.flowservicesample.R;

import butterknife.BindView;

/**
 * Sample activity for post-card-reading stage. See base class.
 *
 * This sample illustrates the data made available to the flow application at these stages,
 * and in what different ways a post-card-reading flow app can augment the transaction.
 */
public class PostCardActivity extends BasePreProcessingActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar(toolbar, R.string.fss_post_card);
    }

    @Override
    protected String getCurrentStage() {
        return FlowStages.POST_CARD_READING;
    }

    @Override
    protected String getHelpText() {
        return getString(R.string.post_card_reading_help);
    }
}
