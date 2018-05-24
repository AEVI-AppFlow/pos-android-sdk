package com.aevi.sdk.pos.flow.paymentinitiationsample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment.*;

import butterknife.ButterKnife;

public class PopupActivity extends AppCompatActivity {

    public static final String FRAGMENT_KEY = "fragment";
    public static final String FRAGMENT_FLOW_SERVICES = "fragment_flow_services";
    public static final String FRAGMENT_PAYMENT_SERVICES = "fragment_payment_services";
    public static final String FRAGMENT_SERVICE_INFO = "fragment_service_info";
    public static final String FRAGMENT_DEVICES = "fragment_devices";
    public static final String FRAGMENT_SYSTEM_INFO = "fragment_system_info";
    public static final String FRAGMENT_SYSTEM_SETTINGS = "fragment_system_settings";
    public static final String FRAGMENT_SYSTEM_EVENTS = "fragment_system_events";
    public static final String FRAGMENT_JSON = "fragment_json";

    public static final String KEY_JSON = "json";

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
                return new SystemOverviewFragment();
            case FRAGMENT_SYSTEM_EVENTS:
                return new SystemEventFragment();
            case FRAGMENT_SYSTEM_SETTINGS:
                return new SystemSettingsFragment();
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
        transitionToFragment(new ServiceInfoFragment(adapter, model));
    }

    private void transitionToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
