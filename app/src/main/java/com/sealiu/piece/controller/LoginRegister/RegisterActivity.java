package com.sealiu.piece.controller.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;
import com.sealiu.piece.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity
        implements RegisterOneFragment.NextStepListener,
        RegisterTwoFragment.CompleteRegisterListener {

    private static final String TAG = "RegisterActivity";
    private FragmentManager fm = getSupportFragmentManager();

    private final User user = new User();

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

    // 点击下一步按钮
    @Override
    public void onNextBtnClick(int flag) {

        // 如果是手机号注册, 只有验证成功才能到达这一步
        // 如果是邮箱注册，收到一条激活邮件，默认为验证失败。当用户点击激活邮件中的链接再改变邮箱的验证状态。
        boolean isVerified = flag == 1;

        user.setMobilePhoneNumberVerified(isVerified);

        // 替换 RegisterOneFragment
        Fragment fragment = new RegisterTwoFragment();
        fm.beginTransaction()
                .replace(R.id.content_frame, fragment, "RegisterTwo")
                .addToBackStack("RegisterTwo")
                .commit();
    }

    // 点击验证按钮
    @Override
    public void onFetchCodeBtnClick(int flag, String phoneOrEmail) {
        if (flag == 1) {//手机号注册
            user.setMobilePhoneNumber(phoneOrEmail);
        } else {//邮箱注册
            user.setEmail(phoneOrEmail);
        }
        user.setUsername(phoneOrEmail);
    }

    // 完成注册
    @Override
    public void onCompleteRegisterBtnClick(String encodedPwd) {

        user.setPassword(encodedPwd);

        // 注册
        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在登录中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                progress.dismiss();
                if (e == null) {
                    Log.i(TAG, "注册成功:" + user.toString());
                    Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i(TAG, e.toString());
                }
            }
        });
    }
}
