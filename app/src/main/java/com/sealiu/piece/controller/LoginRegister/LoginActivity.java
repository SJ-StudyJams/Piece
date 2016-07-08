package com.sealiu.piece.controller.LoginRegister;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private static final String TAG = "LoginActivity";
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果自动登录
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登录中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        if (SPUtils.getBoolean(LoginActivity.this, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false)
                && SPUtils.getString(LoginActivity.this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null) != null) {
            //上次登录时选择了自动登录，并且用户的 objectId 不为空；则自动登录
            User user = new User();
            String username = SPUtils.getString(LoginActivity.this, Constants.SP_FILE_NAME, Constants.SP_USERNAME, null);
            String pwd = SPUtils.getString(LoginActivity.this, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null);
            String password = Md5Utils.encode(pwd);

            user.setUsername(username);
            user.setPassword(password);
            user.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        onSubmitLoginBtnClick();
                    } else {
                        SPUtils.clear(LoginActivity.this, Constants.SP_FILE_NAME);

                        setContentView(R.layout.activity_login);

                        Fragment fragment = fm.findFragmentById(R.id.content_frame);

                        if (fragment == null) {
                            fragment = new LoginFragment();
                            fm.beginTransaction()
                                    .add(R.id.content_frame, fragment, null)
                                    .commit();
                        }
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progress.dismiss();
        } else {
            progress.dismiss();

            setContentView(R.layout.activity_login);

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

