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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import butterknife.ButterKnife;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment.*;

public class PopupActivity extends AppCompatActivity {

    private static final String FRAG_TAG = "fraggleRock";

    public static final String FRAGMENT_KEY = "fragment";
    public static final String FRAGMENT_FLOW_SERVICES = "fragment_flow_services";
    public static final String FRAGMENT_DEVICES = "fragment_devices";
    public static final String FRAGMENT_PAYMENT_RESPONSES = "fragment_payment_responses";
    public static final String FRAGMENT_SYSTEM_INFO = "fragment_system_info";
    public static final String FRAGMENT_SYSTEM_EVENTS = "fragment_system_events";
    public static final String FRAGMENT_JSON = "fragment_json";

    public static final String KEY_JSON = "json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            String fragmentStr = getIntent().getStringExtra(FRAGMENT_KEY);
            Fragment fragment = getFragment(fragmentStr);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, FRAG_TAG).commit();
        }
    }

    private Fragment getFragment(String fragmentValue) {
        switch (fragmentValue) {
            case FRAGMENT_FLOW_SERVICES:
                return new FlowServicesFragment();
            case FRAGMENT_DEVICES:
                return new DevicesFragment();
            case FRAGMENT_PAYMENT_RESPONSES:
                return new PaymentResponsesFragment();
            case FRAGMENT_SYSTEM_INFO:
                return new SystemOverviewFragment();
            case FRAGMENT_SYSTEM_EVENTS:
                return new SystemEventFragment();
            case FRAGMENT_JSON:
                String json = getIntent().getStringExtra(KEY_JSON);
                return JsonDisplayFragment.create("", json);
            default:
                return null;
        }
    }

    public void showJsonFragment(String title, String json) {
        transitionToFragment(JsonDisplayFragment.create(title, json));
    }

    public void showServiceInfoFragment(String adapter, String model) {
        transitionToFragment(ServiceInfoFragment.create(adapter, model));
    }

    private void transitionToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment, FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

}
