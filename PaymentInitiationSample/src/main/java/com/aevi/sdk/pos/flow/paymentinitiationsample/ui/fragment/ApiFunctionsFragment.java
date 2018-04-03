package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.content.Intent;
import android.widget.Toast;

import com.aevi.sdk.pos.flow.paymentinitiationsample.ApiFunction;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
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
            case SYSTEM_INFO:
                showPopup(FRAGMENT_SYSTEM_INFO);
                break;
            case DEVICES:
                showPopup(FRAGMENT_DEVICES);
                break;
            case FLOW_SERVICES:
                showPopup(FRAGMENT_FLOW_SERVICES);
                break;
            case SUBSCRIBE_EVENTS:
                // TODO
                Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case PAYMENT_SERVICES:
                showPopup(FRAGMENT_PAYMENT_SERVICES);
                break;
            case GENERIC_REQUEST:
                Intent requestIntent = new Intent(getContext(), RequestInitiationActivity.class);
                requestIntent.putExtra(RequestInitiationActivity.KEY_FRAGMENT, RequestInitiationActivity.FRAGMENT_GENERIC_REQUEST);
                startActivity(requestIntent);
                break;
            case INITIATE_PAYMENT:
                Intent paymentIntent = new Intent(getContext(), RequestInitiationActivity.class);
                paymentIntent.putExtra(RequestInitiationActivity.KEY_FRAGMENT, RequestInitiationActivity.FRAGMENT_PAYMENT);
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
