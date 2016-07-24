package com.sealiu.piece.controller.LoginRegister;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.SplashScreenActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity
        implements RegisterOneFragment.NextStepListener,
        RegisterTwoFragment.CompleteRegisterListener {

    private static final String TAG = "RegisterActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    private final User user = new User();
    private String encryptPassword;
    private FragmentManager fm = getSupportFragmentManager();
    private LoginUser loginUser;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //新建LoginUser对象
        loginUser = new LoginUser();

        setContentView(R.layout.activity_register);
        scrollView = (ScrollView) findViewById(R.id.register_form);

        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = new RegisterOneFragment();
            fm.beginTransaction()
                    .add(R.id.content_frame, fragment, null)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 动态申请权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
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
    public void onCompleteRegisterBtnClick(final String pwd) {
        //try {
        //encryptPassword = Md5Utils.encode(pwd);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        Log.i(TAG, "" + encryptPassword);

        //User设置加密后密码
        user.setPassword(pwd);
        // 注册
        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("注册成功后自动跳转...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if (e == null) {
                    //记录本次登录时间，设置登录标志位
                    SPUtils.putLong(RegisterActivity.this, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, System.currentTimeMillis());
                    SPUtils.putBoolean(RegisterActivity.this, Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, true);
                    SPUtils.putString(RegisterActivity.this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, u.getObjectId());
                    SPUtils.putString(RegisterActivity.this, Constants.SP_FILE_NAME, Constants.SP_USERNAME, u.getUsername());
                    SPUtils.putString(RegisterActivity.this, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, pwd);
                    loginUser.setLoginTime(System.currentTimeMillis());
                    loginUser.setAutoLogin(true);
                    loginUser.setObjectId(u.getObjectId());
                    loginUser.setUsername(u.getUsername());
                    loginUser.setPassword(pwd);

                    progress.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, SplashScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 清除SP
                    SPUtils.clear(RegisterActivity.this, Constants.SP_FILE_NAME);
                    Log.i(TAG, e.toString());
                    String content = Constants.createErrorInfo(e.getErrorCode()) + " 错误码：" + e.getErrorCode();
                    Snackbar.make(scrollView, content, Snackbar.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        UserInfoSync.saveLoginInfo(RegisterActivity.this, loginUser);
        super.onDestroy();
    }
}
