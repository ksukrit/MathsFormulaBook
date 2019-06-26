package com.alphabyte.mathsformulabook.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.alphabyte.mathsformulabook.R
import com.alphabyte.mathsformulabook.activity.HomeActivity
import com.alphabyte.mathsformulabook.helper.PreferenceHelper
import com.google.firebase.analytics.FirebaseAnalytics

class SettingsFragment : PreferenceFragmentCompat() {

    private var preferenceHelper: PreferenceHelper? = null
    private val DARK_THEME = "darkTheme"
    private val FONT_SIZE = "fontSize"
    internal var arr = intArrayOf(8, 10, 12, 14, 16, 20)


    private val ABOUT_URL = "https://github.com/Sukrit966/MathsFormulaBook/About"
    private val CONTRIBUTE_URL = "https://github.com/Sukrit966/MathsFormulaBook/"
    private val LICENSE_URL = "https://github.com/Sukrit966/MathsFormulaBook/blob/master/app/src/main/assets/license.html"
    private val RATE_URL = "https://github.com/Sukrit966/MathsFormulaBook/blob/master/app/src/main/assets/license.html"


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
        preferenceHelper = PreferenceHelper(context!!)

        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        val switchPreferenceCompat = findPreference<SwitchPreferenceCompat>(getString(R.string.key_darkTheme))
        switchPreferenceCompat!!.setOnPreferenceChangeListener { preference, newValue ->
            val switchPreferenceCompat1 = preference as SwitchPreferenceCompat
            preferenceHelper!!.isDarkTheme = !switchPreferenceCompat1.isChecked
            val params = Bundle()
            params.putString("DarkTheme", (!switchPreferenceCompat1.isChecked).toString())
            mFirebaseAnalytics.logEvent("DarkTheme", params)

            Log.i("TAG", "onPreferenceChange: " + (!switchPreferenceCompat1.isChecked).toString())
            val launchIntent = Intent(activity, HomeActivity::class.java)
            startActivity(launchIntent)
            //getActivity().recreate();
            true
        }

        val listPreference = findPreference<ListPreference>(getString(R.string.key_font))
        var fontSize: Int = preferenceHelper!!.getFontSize()
        val find = arr.indexOf(fontSize)
        if (find == -1) {
            listPreference!!.setDefaultValue(3)
        } else {
            listPreference!!.setDefaultValue(find)
        }

        listPreference!!.setOnPreferenceChangeListener { preference, newValue ->
            val stringValue = newValue.toString()
            val listPreference1 = preference as ListPreference
            val index = listPreference1.findIndexOfValue(stringValue)
            val fontSize = Integer.parseInt(listPreference1.entryValues[index].toString())
            preferenceHelper!!.setFontSize(fontSize)
            Log.i("TAG", "onPreferenceChange: $fontSize")

            true
        }

        val preference = findPreference<Preference>("feedback")
        preference!!.setOnPreferenceClickListener { preference12 ->
            sendFeedback(activity!!)
            true
        }


        val preference1 = findPreference<Preference>("about")
        val preference2 = findPreference<Preference>("contribute")
        val preference3 = findPreference<Preference>("rate")
        val preference4 = findPreference<Preference>("license")

        val intent = Intent(Intent.ACTION_VIEW)

        preference1!!.setOnPreferenceClickListener { preference5 ->
            intent.data = Uri.parse(ABOUT_URL)
            startActivity(intent)
            true
        }

        preference2!!.setOnPreferenceClickListener { preference5 ->
            intent.data = Uri.parse(CONTRIBUTE_URL)
            startActivity(intent)
            true
        }
        preference3!!.setOnPreferenceClickListener { preference5 ->
            intent.data = Uri.parse(RATE_URL)
            startActivity(intent)
            true
        }
        preference4!!.setOnPreferenceClickListener { preference5 ->
            intent.data = Uri.parse(LICENSE_URL)
            startActivity(intent)
            true
        }
    }

    private fun sendFeedback(context: Context) {
        var body: String? = null
        try {
            body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
        } catch (e: PackageManager.NameNotFoundException) {
        }

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("links2phone@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from MathsFormulaBook app")
        intent.putExtra(Intent.EXTRA_TEXT, body)
        //getActivity().startActivity(intent);
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        }

    }

}