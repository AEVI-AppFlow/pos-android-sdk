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


import android.content.Intent;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.ApiFunction;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentInitiationActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.RequestInitiationActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.ApiFunctionsAdapter;

import static com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity.*;

public class ApiFunctionsFragment extends BaseItemFragment<ApiFunction> {

    @Override
    protected void setupItems() {
        title.setText(R.string.choose_api_function);
        ApiFunctionsAdapter adapter = new ApiFunctionsAdapter(getSampleContext().getApiChoices(), this, false);
        items.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(ApiFunction apiFunction) {
        switch (apiFunction.getApiMethod()) {
            case SYSTEM_OVERVIEW:
                showPopup(FRAGMENT_SYSTEM_INFO);
                break;
            case DEVICES:
                showPopup(FRAGMENT_DEVICES);
                break;
            case FLOW_SERVICES:
                showPopup(FRAGMENT_FLOW_SERVICES);
                break;
            case SUBSCRIBE_EVENTS:
                showPopup(FRAGMENT_SYSTEM_EVENTS);
                break;
            case SYSTEM_SETTINGS:
                showPopup(FRAGMENT_SYSTEM_SETTINGS);
                break;
            case PAYMENT_SERVICES:
                showPopup(FRAGMENT_PAYMENT_SERVICES);
                break;
            case GENERIC_REQUEST:
                Intent requestIntent = new Intent(getContext(), RequestInitiationActivity.class);
                startActivity(requestIntent);
                break;
            case INITIATE_PAYMENT:
                Intent paymentIntent = new Intent(getContext(), PaymentInitiationActivity.class);
                startActivity(paymentIntent);
                break;
        }
    }

    private void showPopup(String fragment) {
        Intent intent = new Intent(getContext(), PopupActivity.class);
        intent.putExtra(FRAGMENT_KEY, fragment);
        startActivity(intent);
    }
}
