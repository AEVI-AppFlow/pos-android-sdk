package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.ResponseQuery;
import com.aevi.sdk.flow.model.ResponseQueryBuilder;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.PaymentResponsesAdapter;

public class PaymentResponsesFragment extends BaseItemFragment<Device> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_payment_responses);
        long now = System.currentTimeMillis();
        long then = System.currentTimeMillis() - 48 * 60 * 60 * 1000;

        ResponseQuery paymentResponseQuery = new ResponseQueryBuilder(then, now)
                .withMaxResults(100)
                .build();
        getSampleContext()
                .getPaymentClient()
                .queryPaymentResponses(paymentResponseQuery)
                .toList()
                .subscribe(paymentResponses -> {
                    if (paymentResponses.isEmpty()) {
                        showNoItemsAvailable(R.string.no_payment_responses_found);
                    } else {
                        PaymentResponsesAdapter adapter = new PaymentResponsesAdapter(paymentResponses, PaymentResponsesFragment.this, false);
                        items.setAdapter(adapter);
                    }
                }, throwable -> {
                    if (throwable instanceof FlowException) {
                        Intent errorIntent = new Intent(getContext(), PaymentResultActivity.class);
                        errorIntent.putExtra(PaymentResultActivity.ERROR_KEY, ((FlowException) throwable).toJson());
                        startActivity(errorIntent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Unrecoverable error occurred - see logs", Toast.LENGTH_SHORT).show();
                        Log.e(DevicesFragment.class.getSimpleName(), "Error", throwable);
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onItemSelected(Device device) {
    }
}