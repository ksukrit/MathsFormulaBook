package com.alphabyte.mathsformulabook.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.alphabyte.mathsformulabook.R;
import com.alphabyte.mathsformulabook.activity.HomeActivity;
import com.alphabyte.mathsformulabook.helper.PreferenceHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SettingsFragment extends PreferenceFragmentCompat {

    private PreferenceHelper preferenceHelper;
    private String DARK_THEME = "darkTheme";
    private String FONT_SIZE = "fontSize";

    private static String ABOUT_URL = "https://github.com/Sukrit966/MathsFormulaBook/About";
    private static String CONTRIBUTE_URL = "https://github.com/Sukrit966/MathsFormulaBook/";
    private static String LICENSE_URL = "https://github.com/Sukrit966/MathsFormulaBook/blob/master/app/src/main/assets/license.html";
    private static String RATE_URL = "https://github.com/Sukrit966/MathsFormulaBook/blob/master/app/src/main/assets/license.html";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey);
        preferenceHelper = new PreferenceHelper(getContext());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        SwitchPreferenceCompat switchPreferenceCompat = findPreference(getString(R.string.key_darkTheme));
        switchPreferenceCompat.setOnPreferenceChangeListener((preference, newValue) -> {
            SwitchPreferenceCompat switchPreferenceCompat1 = (SwitchPreferenceCompat) preference;
            preferenceHelper.setDarkTheme(!switchPreferenceCompat1.isChecked());
            Bundle params = new Bundle();
            params.putString("DarkTheme", String.valueOf(!switchPreferenceCompat1.isChecked()));
            mFirebaseAnalytics.logEvent("DarkTheme", params);

            Log.i("TAG", "onPreferenceChange: " + String.valueOf(!switchPreferenceCompat1.isChecked()));
            Intent launchIntent = new Intent(getActivity(), HomeActivity.class);
            startActivity(launchIntent);
            //getActivity().recreate();
            return true;
        });

        ListPreference listPreference = findPreference(getString(R.string.key_font));
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String stringValue = newValue.toString();
            ListPreference listPreference1 = (ListPreference) preference;
            int index = listPreference1.findIndexOfValue(stringValue);
            int fontSize = Integer.parseInt(listPreference1.getEntryValues()[index].toString());
            preferenceHelper.setFontSize(fontSize);
            Log.i("TAG", "onPreferenceChange: " + fontSize);

            return true;
        });

        Preference preference = findPreference("feedback");
        preference.setOnPreferenceClickListener(preference12 -> {
            sendFeedback(getActivity());
            return true;
        });

        Preference preference1 = findPreference("about");
        Preference preference2 = findPreference("contribute");
        Preference preference3 = findPreference("rate");
        Preference preference4 = findPreference("license");

        Intent intent = new Intent(Intent.ACTION_VIEW);

        preference1.setOnPreferenceClickListener(preference5 -> {
            intent.setData(Uri.parse(ABOUT_URL));
            startActivity(intent);
            return true;
        });

        preference2.setOnPreferenceClickListener(preference5 -> {
            intent.setData(Uri.parse(CONTRIBUTE_URL));
            startActivity(intent);
            return true;
        });
        preference3.setOnPreferenceClickListener(preference5 -> {
            intent.setData(Uri.parse(RATE_URL));
            startActivity(intent);
            return true;
        });
        preference4.setOnPreferenceClickListener(preference5 -> {
            intent.setData(Uri.parse(LICENSE_URL));
            startActivity(intent);
            return true;
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
