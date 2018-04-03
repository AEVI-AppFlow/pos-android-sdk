package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenericResultActivity extends AppCompatActivity {

    public static final String GENERIC_RESPONSE_KEY = "genericResponse";

    @BindView(R.id.request_status)
    ImageView requestStatus;

    private ModelDisplay modelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_generic_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
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

    @OnClick({R.id.close})
    public void onClose() {
        finish();
    }
}
