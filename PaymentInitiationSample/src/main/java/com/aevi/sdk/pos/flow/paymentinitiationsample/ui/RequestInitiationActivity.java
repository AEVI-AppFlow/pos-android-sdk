package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestInitiationActivity extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";
    public static final String FRAGMENT_PAYMENT = "fragment_payment";
    public static final String FRAGMENT_GENERIC_REQUEST = "fragment_generic_request";

    @BindView(R.id.title)
    TextView title;

    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra(KEY_FRAGMENT).equals(FRAGMENT_PAYMENT)) {
            setContentView(R.layout.activity_payment);
            ButterKnife.bind(this);
            title.setText(R.string.initiate_payment);
        } else {
            setContentView(R.layout.activity_request);
            ButterKnife.bind(this);
            title.setText(R.string.initiate_request);
        }
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
    }

    public ModelDisplay getModelDisplay() {
        return modelDisplay;
    }
}
