package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!PaymentApi.isProcessingServiceInstalled(this)) {
            Toast.makeText(this, "API processing service is not installed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
}
