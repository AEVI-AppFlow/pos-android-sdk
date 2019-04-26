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

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import com.aevi.sdk.flow.constants.FlowStages;
import com.aevi.sdk.pos.flow.sample.R;

import java.util.ArrayList;
import java.util.List;

public class FlowStagesActivity extends AppCompatActivity {

    public static final String KEY_TITLE_BG = "titleBgColor";
    public static final String KEY_CURRENT_STAGE = "titleCurrentStage";

    static class Stage {
        final String name;
        final int viewId;

        public Stage(String name, int viewId) {
            this.name = name;
            this.viewId = viewId;
        }
    }

    private static final List<Stage> stages;

    static {
        stages = new ArrayList<>();
        stages.add(new Stage("Payment initiation", R.id.payment_initiation));
        stages.add(new Stage(FlowStages.PRE_FLOW, R.id.pre_flow));
        stages.add(new Stage(FlowStages.SPLIT, R.id.split));
        stages.add(new Stage(FlowStages.PRE_TRANSACTION, R.id.pre_payment));
        stages.add(new Stage(FlowStages.PAYMENT_CARD_READING, R.id.card_reading));
        stages.add(new Stage(FlowStages.POST_CARD_READING, R.id.post_card_reading));
        stages.add(new Stage(FlowStages.TRANSACTION_PROCESSING, R.id.transaction_processing));
        stages.add(new Stage(FlowStages.POST_TRANSACTION, R.id.post_payment));
        stages.add(new Stage(FlowStages.POST_FLOW, R.id.post_flow));
        stages.add(new Stage("Payment response", R.id.payment_response));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flow_stages);

        int titleBg = getIntent().getIntExtra(KEY_TITLE_BG, 0);
        TextView titleView = findViewById(R.id.title);
        if (titleBg != 0) {
            titleView.setBackgroundColor(titleBg);
        }
        String currentStage = getIntent().getStringExtra(KEY_CURRENT_STAGE);

        boolean postActive = false;
        for (Stage stage : stages) {
            if (stage.name.equals(currentStage)) {
                setStageCurrent(stage.viewId);
                postActive = true;
            } else if (!postActive) {
                setStageExecuted(stage.viewId);
            }
        }
    }

    private void setStageExecuted(int stageId) {
        TextView textView = getView(stageId);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        textView.setEnabled(false);
    }

    private void setStageCurrent(int stageId) {
        TextView textView = getView(stageId);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setSelected(true);
        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollView.post(() -> scrollView.scrollTo(0, textView.getTop()));
    }

    private TextView getView(int stageId) {
        return (TextView) findViewById(stageId);
    }
}
