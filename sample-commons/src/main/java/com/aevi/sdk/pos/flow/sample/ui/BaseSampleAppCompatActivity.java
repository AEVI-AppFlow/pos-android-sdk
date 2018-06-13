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

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.ActivityEvents;

public class BaseSampleAppCompatActivity<T> extends AppCompatActivity {

    protected void registerForActivityEvents() {
        try {
            ObservableActivityHelper<T> helper = ObservableActivityHelper.getInstance(getIntent());
            helper.registerForEvents(getLifecycle()).subscribe(event -> {
                switch (event) {
                    case ActivityEvents.FINISH:
                        finish();
                        break;
                }
            });
        } catch (NoSuchInstanceException e) {
            Log.e(getClass().getSimpleName(), "No ObservableActivityHelper found - finishing activity");
            finish();
        }
    }

    protected void sendResponseAndFinish(T response) {
        try {
            ObservableActivityHelper<T> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(response);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
