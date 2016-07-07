package com.sealiu.piece.controller.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

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

            Preference logoutPreference = findPreference("pref_logout_key");

            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.i("Preference", String.valueOf(preference.getKey()));
                    SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, false);
                    SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false);

                    // 有bug，退出到登录页面之后，点击back按钮，有回到MapsActivity ==!
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                    return false;
                }
            });
        }
    }

}
