package com.aevi.sdk.pos.flow.paymentservicesample.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.*;

import com.aevi.sdk.pos.flow.paymentservicesample.PaymentServiceInfoProvider;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.paymentservicesample.service.PaymentCardReadingService;

import java.util.Arrays;

import static android.content.pm.PackageManager.*;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        sharedPreferences = getPreferenceManager().getSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            setSummary(getPreferenceScreen().getPreference(i));
        }
    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_supports_read_card_key))) {
            boolean enable = ((SwitchPreference) getPreferenceScreen().findPreference(key)).isChecked();
            enableDisableCardReadingService(getActivity(), enable);
        } else {
            Preference pref = getPreferenceScreen().findPreference(key);
            setSummary(pref);
        }
        PaymentServiceInfoProvider.notifyServiceInfoChange(getActivity());
    }

    private void setSummary(Preference pref) {
        if (pref instanceof MultiSelectListPreference) {
            updateSummary((MultiSelectListPreference) pref);
        }
    }

    private void updateSummary(MultiSelectListPreference pref) {
        pref.setSummary(Arrays.toString(pref.getValues().toArray()));
    }

    public static void enableDisableCardReadingService(Context context, boolean enable) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), PaymentCardReadingService.class.getName());
        int enableDisableFlag = enable ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(componentName, enableDisableFlag, DONT_KILL_APP);
    }
}
