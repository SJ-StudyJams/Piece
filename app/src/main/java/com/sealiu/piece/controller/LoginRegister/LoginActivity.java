package com.sealiu.piece.controller.LoginRegister;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.Maps.MapsActivity;
import com.sealiu.piece.model.LoginUser;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private static final String TAG = "LoginActivity";
    private FragmentManager fm = getSupportFragmentManager();
    private ProgressDialog progress;
    private LoginUser loginUser;

    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //启动MainService
        //Intent intent1 = new Intent(this, PieceMainService.class);
        //startService(intent1);
        Log.i(TAG, "onCreate");

        scrollView = (ScrollView) findViewById(R.id.login_form);

        // 需要手动登录
        setContentView(R.layout.activity_login);
        Fragment fragment = fm.findFragmentById(R.id.content_frame);
        if (fragment == null) {fragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.content_frame, fragment, null)
                    .commit();}

    }

    @Override
    public void onSubmitLoginBtnClick() {
        Log.i(TAG, "Start MapsActivity");
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackRegisterBtnClick() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

//    @Override
//    public void onThirdPartLoginBtnClick() {
//        Fragment fragment = new ThirdPartFragment();
//        fm.beginTransaction()
//                .replace(R.id.content_frame, fragment, "ThirdPart")
//                .addToBackStack("ThirdPart")
//                .commit();
//    }

    @Override
    public void onResetPasswordBtnClick() {
        startActivity(new Intent(LoginActivity.this, ResetPwdActivity.class));
    }
}

