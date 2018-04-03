package com.aevi.payment.api.sample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.aevi.payment.api.sample.R;
import com.aevi.payment.api.sample.ui.fragment.*;

import butterknife.ButterKnife;

public class PopupActivity extends AppCompatActivity {

    public static final String FRAGMENT_KEY = "fragment";
    public static final String FRAGMENT_FLOW_SERVICES = "fragment_flow_services";
    public static final String FRAGMENT_PAYMENT_SERVICES = "fragment_payment_services";
    public static final String FRAGMENT_SERVICE_INFO = "fragment_service_info";
    public static final String FRAGMENT_DEVICES = "fragment_devices";
    public static final String FRAGMENT_SYSTEM_INFO = "fragment_system_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        String fragmentStr = getIntent().getStringExtra(FRAGMENT_KEY);
        Fragment fragment = getFragment(fragmentStr);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private Fragment getFragment(String fragmentValue) {
        switch (fragmentValue) {
            case FRAGMENT_FLOW_SERVICES:
                return new FlowServicesFragment();
            case FRAGMENT_PAYMENT_SERVICES:
                return new PaymentServicesFragment();
            case FRAGMENT_DEVICES:
                return new DevicesFragment();
            case FRAGMENT_SYSTEM_INFO:
                return new SystemInfoFragment();
            default:
                return null;
        }
    }

    public void showFragment(String fragKey, String adapter, String model) {
        Fragment fragment;
        switch (fragKey) {
            case FRAGMENT_SERVICE_INFO:
            default:
                fragment = new ServiceInfoFragment(adapter, model);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
