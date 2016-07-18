package com.sealiu.piece.controller.Settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.sealiu.piece.R;

/**
 * Created by liuyang
 * on 2016/7/18.
 */
public class MyPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference aboutPreference = findPreference("pref_about_key");
        Preference feedbackPreference = findPreference("pref_feedback_key");

        aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyPreferenceFragmentListener listener = (MyPreferenceFragmentListener) getActivity();
                listener.onAboutClick();
                return true;
            }
        });

        feedbackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyPreferenceFragmentListener listener = (MyPreferenceFragmentListener) getActivity();
                listener.onFeedbackClick();
                return true;
            }
        });
    }

    public interface MyPreferenceFragmentListener {
        void onAboutClick();

        void onFeedbackClick();
    }

}
