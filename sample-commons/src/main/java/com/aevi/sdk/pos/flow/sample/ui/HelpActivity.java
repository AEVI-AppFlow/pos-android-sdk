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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.sample.R;

public class HelpActivity extends AppCompatActivity {

    public static final String KEY_TEXT = "text";
    public static final String KEY_TITLE_BG = "titleBgColor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);

        TextView helpText = findViewById(R.id.text);
        String text = getIntent().getStringExtra(KEY_TEXT);
        helpText.setText(text);

        int titleBg = getIntent().getIntExtra(KEY_TITLE_BG, 0);
        if (titleBg != 0) {
            TextView titleView = findViewById(R.id.title);
            titleView.setBackgroundColor(titleBg);
        }
    }
}
