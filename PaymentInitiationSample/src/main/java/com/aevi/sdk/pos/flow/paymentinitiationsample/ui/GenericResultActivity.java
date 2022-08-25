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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

public class GenericResultActivity extends AppCompatActivity {

    public static final String GENERIC_RESPONSE_KEY = "genericResponse";

    @BindView(R.id.request_status)
    ImageView requestStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        Intent intent = getIntent();
        if (intent.hasExtra(GENERIC_RESPONSE_KEY)) {
            Response response = Response.fromJson(intent.getStringExtra(GENERIC_RESPONSE_KEY));
            modelDisplay.showTitle(false);
            modelDisplay.showResponse(response);
            if (!response.wasSuccessful()) {
                requestStatus.setImageResource(R.drawable.ic_error_circle);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.button_close)
    public void close() {
        finish();
    }
}
