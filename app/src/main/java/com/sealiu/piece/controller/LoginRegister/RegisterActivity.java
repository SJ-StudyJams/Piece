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
import com.sealiu.piece.model.User;
import com.sealiu.piece.service.Impl.UserServiceImpl;
import com.sealiu.piece.service.UserService;

public class RegisterActivity extends AppCompatActivity
        implements RegisterOneFragment.NextStepListener,
        RegisterTwoFragment.CompleteRegisterListener {

    private static final String TAG = "RegisterActivity";
    private FragmentManager fm = getSupportFragmentManager();

    private User user = new User();

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
                .replace(R.id.content_frame, fragment, null)
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

    @Override
    public void onCompleteRegisterBtnClick() {

        EditText pwdET = (EditText) findViewById(R.id.reg_password);
        EditText repeatPwdET = (EditText) findViewById(R.id.repeat_pwd);
        Log.i(TAG, "Pwd//Repeat" + pwdET.getText().toString() + "//" + repeatPwdET.getText().toString());

        user.setPassword(pwdET.getText().toString());
        user.setBio("有效驱蚊，保护家人");

        UserService userService = new UserServiceImpl();
        String objectId = userService.signUp(user);
        // objectId 为null
        // Log.i(TAG, objectId);

        Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
