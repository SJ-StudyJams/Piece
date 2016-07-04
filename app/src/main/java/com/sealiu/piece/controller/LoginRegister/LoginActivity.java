package com.sealiu.piece.controller.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.MapsActivity;
import com.sealiu.piece.model.User;
import com.sealiu.piece.service.Impl.UserServiceImpl;
import com.sealiu.piece.service.UserService;
import com.sealiu.piece.utils.Md5Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private static final String TAG = "LoginActivity";
    private FragmentManager fm = getSupportFragmentManager();
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.content_frame, fragment, null)
                    .commit();
        }
    }

    @Override
    public void onSubmitLoginBtnClick() {

        //user.setUsername(username);
        //String password = Md5Utils.encode(pwd);
        //user.setPassword(password);

        //UserService userService = new UserServiceImpl();
        //String objectId = userService.login(user);
        //if (objectId == null) {
        //    Log.e(TAG, "登陆失败");
        //}
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
        fm.beginTransaction().replace(R.id.content_frame, fragment, null).commit();
    }
}

