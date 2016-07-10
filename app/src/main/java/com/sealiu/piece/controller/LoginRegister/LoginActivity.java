package com.sealiu.piece.controller.LoginRegister;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private static final String TAG = "LoginActivity";
    private FragmentManager fm = getSupportFragmentManager();
    private UserInfoSync userInfoSync = new UserInfoSync();
    private User user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取本地用户信息
        user2 = userInfoSync.getLoginInfo(this);

        // 如果自动登录
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登录中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        Log.i(TAG, "isAutoLogin:" + user2.isAutoLogin());
        Log.i(TAG, "objectId" + user2.getObjectId());
        //检查是否长时间未登录
        if (getLoginTime() && user2.isAutoLogin()
                && user2.getObjectId() != null) {
            //上次登录时选择了自动登录，并且用户的 objectId 不为空；则自动登录
            final User user1 = new User();
            final String username = (String) BmobUser.getObjectByKey(Constants.SP_USERNAME);
            Log.i(TAG, "username:" + username);
            final String pwd = user2.getPwd();
            Log.i(TAG, "pwd:" + pwd);
            String password = Md5Utils.encode(pwd);

            user1.setUsername(username);
            user1.setPassword(password);
            user1.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            try {
                                //userInfoSync.getUserInfo(getApplicationContext(), user1);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            onSubmitLoginBtnClick();
                        } else {
                            Log.e(TAG, e.toString());
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

    /**
     * 检查用户是否超过一个月未登陆
     * @return login
     */
    private boolean getLoginTime() {
        boolean login;
        //获取当前时间
        long timeNow = System.currentTimeMillis();
        Log.i(TAG, "now:" + timeNow);
        //获取用户上次登录时间
        long timePre = SPUtils.getLong(this, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, 0);
        Log.i(TAG, "pre:" + timePre);
        //当用户本次登陆时间大于上次登录时间一个月
        if(timeNow - timePre > 2592000){
            SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false);
            login = false;
        }
        else{
            SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, true);
            SPUtils.putLong(this, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, timeNow);
            login = true;
        }

        Log.i(TAG, "" + login);
        return login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

