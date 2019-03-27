package com.alphabyte.maths.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.alphabyte.maths.R;
import com.alphabyte.maths.activity.HomeActivity;
import com.alphabyte.maths.helper.PreferenceHelper;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    PreferenceHelper preferenceHelper;
    private String DARK_THEME = "darkTheme";
    private String FONT_SIZE = "fontSize";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen,rootKey);
        preferenceHelper = new PreferenceHelper(getContext());

        SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) findPreference(getString(R.string.key_darkTheme));
        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;
                preferenceHelper.setDarkTheme(!switchPreferenceCompat.isChecked());
                Log.i("TAG", "onPreferenceChange: " + String.valueOf(!switchPreferenceCompat.isChecked()));
                Intent launchIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(launchIntent);
                return true;
            }
        });

        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.key_font));
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                int fontSize = Integer.parseInt(listPreference.getEntryValues()[index].toString());
                preferenceHelper.setFontSize(fontSize);
                Log.i("TAG", "onPreferenceChange: " + fontSize);

                return true;
            }
        });

        Preference preference = findPreference("feedback");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sendFeedback(getActivity());
                return true;
            }
        });

    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                int d = Integer.parseInt(listPreference.getEntryValues()[index].toString());
                Log.i("TAG", "onPreferenceChange: " + d);

                /* Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                */
            } else if(preference instanceof SwitchPreferenceCompat){
                SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;
                Log.i("TAG", "onPreferenceChange: " + String.valueOf(!switchPreferenceCompat.isChecked()));
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"links2phone@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        getActivity().startActivity(intent);

    }
}
