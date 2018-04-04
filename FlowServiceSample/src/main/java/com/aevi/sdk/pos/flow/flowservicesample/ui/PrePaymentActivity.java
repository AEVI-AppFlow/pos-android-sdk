package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.AmountIdentifiers;
import com.aevi.sdk.flow.constants.CustomerDataKeys;
import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.AmountsModifier;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.sample.AmountFormatter;
import com.aevi.sdk.pos.flow.sample.CustomerProducer;
import com.aevi.sdk.pos.flow.sample.ui.ModelDetailsActivity;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aevi.sdk.pos.flow.model.AmountsModifier.percentageToFraction;

/**
 * Sample for pre-payment (stages pre-transaction and post-card-reading).
 *
 * This sample illustrates the data made available to the flow application at these stages,
 * and in what different ways a pre-payment flow app can augment the transaction.
 */
public class PrePaymentActivity extends AppCompatActivity {

    private static final String SAMPLE_POINTS_USED_KEY = "sampleLoyaltyPointsUsed";

    private TransactionRequest transactionRequest;
    private FlowResponse flowResponse;
    private AmountsModifier amountsModifier;
    private ModelDisplay modelDisplay;

    @BindViews({R.id.pay_with_points, R.id.pay_with_giftcard})
    List<View> payViews;

    @BindView(R.id.add_surcharge)
    TextView surchargeView;

    @BindView(R.id.pay_with_giftcard)
    TextView giftCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_payment);
        ButterKnife.bind(this);

        // SamplePrePaymentService uses the API launchActivity() method, which means the request will be available as per below
        transactionRequest = TransactionRequest.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));

        surchargeView.setText(getString(R.string.add_surcharge_fee, AmountFormatter.formatAmount(transactionRequest.getAmounts().getCurrency(),
                getResources().getInteger(R.integer.surcharge_fee))));
        giftCardView.setText(getString(R.string.pay_portion_with_gift_card, AmountFormatter.formatAmount(transactionRequest.getAmounts().getCurrency(),
                getResources().getInteger(R.integer.pay_gift_card_value))));
        amountsModifier = new AmountsModifier(transactionRequest.getAmounts());
        flowResponse = new FlowResponse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        updateModel();
    }

    @OnClick(R.id.show_request)
    public void onShowRequest() {
        Intent intent = new Intent(this, ModelDetailsActivity.class);
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_TYPE, TransactionRequest.class.getName());
        intent.putExtra(ModelDetailsActivity.KEY_MODEL_DATA, transactionRequest.toJson());
        intent.putExtra(ModelDetailsActivity.KEY_TITLE, "TransactionRequest");
        intent.putExtra(ModelDetailsActivity.KEY_TITLE_BG, getResources().getColor(R.color.colorPrimary));
        startActivity(intent);
    }

    private void updateModel() {
        if (modelDisplay != null) {
            modelDisplay.showFlowResponse(flowResponse);
        }
    }

    @OnClick(R.id.add_surcharge)
    public void onAddSurcharge() {
        int surchargeFree = getResources().getInteger(R.integer.surcharge_fee);
        amountsModifier.setAdditionalAmount(AmountIdentifiers.AMOUNT_SURCHARGE, surchargeFree);
        flowResponse.updateRequestAmounts(amountsModifier.build());
        updateModel();
        surchargeView.setEnabled(false);
    }

    @OnClick(R.id.add_charity)
    public void onAddCharity(View v) {
        int charityPercentage = getResources().getInteger(R.integer.charity_percentage);
        amountsModifier.setAdditionalAmountAsBaseFraction(AmountIdentifiers.AMOUNT_CHARITY_DONATION, percentageToFraction(charityPercentage));
        flowResponse.updateRequestAmounts(amountsModifier.build());
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.pay_with_points)
    public void onPayWithPoints() {
        long points = getResources().getInteger(R.integer.pay_points);
        long pointsAmountValue = getRandomPointsValue(points);

        flowResponse.setAmountsPaid(new Amounts(pointsAmountValue, transactionRequest.getAmounts().getCurrency()), PaymentMethods.LOYALTY_POINTS);
        flowResponse.addAdditionalRequestData(SAMPLE_POINTS_USED_KEY, points);
        disablePayViews();
        updateModel();
    }

    private long getRandomPointsValue(long points) {
        return (long) (points * (Math.random() * 4));
    }

    @OnClick(R.id.pay_with_giftcard)
    public void onPayWithGiftCard() {
        long giftCardValue = getResources().getInteger(R.integer.pay_gift_card_value);
        flowResponse.setAmountsPaid(new Amounts(giftCardValue, transactionRequest.getAmounts().getCurrency()), PaymentMethods.GIFT_CARD);
        disablePayViews();
        updateModel();
    }

    @OnClick(R.id.add_customer_data)
    public void addCustomerData(View v) {
        Customer customer;
        if (transactionRequest.getAdditionalData().hasData(AdditionalDataKeys.DATA_KEY_CUSTOMER)) {
            customer = transactionRequest.getAdditionalData().getValue(AdditionalDataKeys.DATA_KEY_CUSTOMER, Customer.class);
            customer.addCustomerDetails(CustomerDataKeys.CITY, "London");
            customer.addCustomerDetails("updatedBy", "PrePayment Sample");
        } else {
            customer = CustomerProducer.getDefaultCustomer("PrePayment Sample");
        }
        flowResponse.addAdditionalRequestData(AdditionalDataKeys.DATA_KEY_CUSTOMER, customer);
        updateModel();
        v.setEnabled(false);
    }

    @OnClick(R.id.send_response)
    public void onSendResponse() {
        // Lastly, as we were started via launchActivity() in the API, we pass back the response to the service in the manner below
        try {
            ObservableActivityHelper<FlowResponse> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(flowResponse);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }

        // Always remember to finish the activity after sending the response!
        finish();
    }

    private void disablePayViews() {
        for (View payView : payViews) {
            payView.setEnabled(false);
        }
    }
}
