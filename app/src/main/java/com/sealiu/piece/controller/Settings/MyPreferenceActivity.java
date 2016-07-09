package com.sealiu.piece.controller.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sealiu.piece.R;

public class MyPreferenceActivity extends AppCompatActivity {

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

    // MyPreferenceFragment
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            Preference aboutPreference = findPreference("pref_about_key");

            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new AboutPreferenceFragment()).addToBackStack(null)
                            .commit();
                    return true;
                }
            });

//            Preference logoutPreference = findPreference("pref_logout_key");
//            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Log.i("Preference", String.valueOf(preference.getKey()));
//
//                    // 有bug，退出到登录页面之后，点击back按钮，有回到MapsActivity ==!
//                    SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, false);
//                    SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false);
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    getActivity().finish();
//                    return true;
//                }
//            });
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
