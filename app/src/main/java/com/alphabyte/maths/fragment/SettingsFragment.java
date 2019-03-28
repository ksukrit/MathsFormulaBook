package com.alphabyte.maths.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.alphabyte.maths.R;
import com.alphabyte.maths.activity.HomeActivity;
import com.alphabyte.maths.helper.PreferenceHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

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

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) findPreference(getString(R.string.key_darkTheme));
        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;
                preferenceHelper.setDarkTheme(!switchPreferenceCompat.isChecked());
                Bundle params = new Bundle();
                params.putString("DarkTheme",String.valueOf(!switchPreferenceCompat.isChecked()));
                mFirebaseAnalytics.logEvent("DarkTheme", params);

                Log.i("TAG", "onPreferenceChange: " + String.valueOf(!switchPreferenceCompat.isChecked()));
                Intent launchIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(launchIntent);
                //getActivity().recreate();
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


    private void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"links2phone@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from MathsFormulaBook app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        //getActivity().startActivity(intent);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
