package com.sealiu.piece.controller.LoginRegister;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;
import com.sealiu.piece.utils.SPUtils;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private static final String TAG = "LoginActivity";
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Boolean isLogin = SPUtils.getBoolean(this,"isLogin",false);
        if (isLogin) {
            onSubmitLoginBtnClick();
        } else {
            Fragment fragment = fm.findFragmentById(R.id.content_frame);

            if (fragment == null) {
                fragment = new LoginFragment();
                fm.beginTransaction()
                        .add(R.id.content_frame, fragment, null)
                        .commit();
            }
        }

    }

    @Override
    public void onSubmitLoginBtnClick() {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackRegisterBtnClick() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    public void onThirdPartLoginBtnClick() {
        Fragment fragment = new ThirdPartFragment();
        fm.beginTransaction()
                .replace(R.id.content_frame, fragment, "ThirdPart")
                .addToBackStack("ThirdPart")
                .commit();
    }
}

