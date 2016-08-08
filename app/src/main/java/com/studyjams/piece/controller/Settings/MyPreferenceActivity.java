package com.studyjams.piece.controller.Settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.studyjams.piece.R;

public class MyPreferenceActivity extends AppCompatActivity implements
        MyPreferenceFragment.MyPreferenceFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.setting_menu_title);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new MyPreferenceFragment())
                .commit();

    }

    @Override
    public void onAboutClick() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new AboutPreferenceFragment()).addToBackStack(null)
                .commit();
    }

    @Override
    public void onFeedbackClick() {
        String s = "Debug-infos:";
        s += "\nOS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\nOS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\nDevice: " + android.os.Build.DEVICE;
        s += "\nModel (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")\n";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sealiu0217@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Piece应用反馈");
        intent.putExtra(Intent.EXTRA_TEXT, s);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static class AboutPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);

            Preference termsPreference = findPreference("pref_terms_of_use_key");
            Preference PrivacyPreference = findPreference("pref_privacy_policy_key");
            Preference LicensesPreference = findPreference("pref_licenses_key");

            termsPreference.setOnPreferenceClickListener(this);
            PrivacyPreference.setOnPreferenceClickListener(this);
            LicensesPreference.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "pref_terms_of_use_key":
                    startActivity(new Intent(getActivity(), TermsActivity.class));
                    break;
                case "pref_privacy_policy_key":
                    startActivity(new Intent(getActivity(), PrivacyActivity.class));
                    break;
                case "pref_licenses_key":
                    startActivity(new Intent(getActivity(), LicensesActivity.class));
                    break;
                case "pref_version_key":
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
