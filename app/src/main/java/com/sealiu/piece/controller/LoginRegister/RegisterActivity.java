package com.sealiu.piece.controller.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;

public class RegisterActivity extends AppCompatActivity
        implements RegisterOneFragment.NextStepListener,
        RegisterTwoFragment.CompleteRegisterListener {

    private static final String TAG = "RegisterActivity";
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = new RegisterOneFragment();
            fm.beginTransaction()
                    .add(R.id.content_frame, fragment, null)
                    .commit();
        }
    }

    @Override
    public void onNextBtnClick() {

        EditText phoneET = (EditText) findViewById(R.id.reg_phone);
        EditText validatingCodeET = (EditText) findViewById(R.id.validating_code);

        Log.i(TAG, "Phone//Code" + phoneET.getText().toString() + "//" + validatingCodeET.getText().toString());

        // 替换 RegisterOneFragment
        Fragment fragment = new RegisterTwoFragment();
        fm.beginTransaction()
                .replace(R.id.content_frame, fragment, null)
                .commit();
    }

    @Override
    public void onCompleteRegisterBtnClick() {

        EditText pwdET = (EditText) findViewById(R.id.reg_password);
        EditText repeatPwdET = (EditText) findViewById(R.id.repeat_pwd);
        Log.i(TAG, "Pwd//Repeat" + pwdET.getText().toString() + "//" + repeatPwdET.getText().toString());

        Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
