package com.sealiu.piece.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.controller.Maps.MapsActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView pg1;
    ImageView pg2;
    ImageView pg3;
    ImageView pg4;
    private LoginUser loginUser;
    private Animation upAndDown1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginUser = UserInfoSync.getLoginInfo(SplashScreenActivity.this);
        String username = loginUser.getUsername();
        String pwd = loginUser.getPassword();
        Log.i(TAG, "" + pwd);

        if (!isOutOfDate()
                && loginUser.getObjectId() != null
                && loginUser.isAutoLogin()
                && username != null
                && pwd != null) {
            //距上次登录没有超过1个月，objectId不为空，且用户为登录状态，则自动登录

            BmobService bmobService = new BmobService(this, loginUser);
            loginUser = bmobService.login(username, pwd);
        }

        iv1 = (ImageView) findViewById(R.id.imageView1);
        iv2 = (ImageView) findViewById(R.id.imageView2);
        iv3 = (ImageView) findViewById(R.id.imageView3);
        iv4 = (ImageView) findViewById(R.id.imageView4);

        pg1 = (ImageView) findViewById(R.id.progress1);
        pg2 = (ImageView) findViewById(R.id.progress2);
        pg3 = (ImageView) findViewById(R.id.progress3);
        pg4 = (ImageView) findViewById(R.id.progress4);


        upAndDown1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.up_down);

        iv1.startAnimation(upAndDown1);
        upAndDown1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pg1.setVisibility(View.VISIBLE);
                Animation upAndDown2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.up_down);

                iv2.startAnimation(upAndDown2);
                upAndDown2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        pg2.setVisibility(View.VISIBLE);
                        Animation upAndDown3 = AnimationUtils.loadAnimation(getBaseContext(),
                                R.anim.up_down);

                        iv3.startAnimation(upAndDown3);
                        upAndDown3.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {pg3.setVisibility(View.VISIBLE);
                                Animation upAndDown4 = AnimationUtils.loadAnimation(getBaseContext(),
                                        R.anim.up_down);

                                iv4.startAnimation(upAndDown4);
                                upAndDown4.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        pg4.setVisibility(View.VISIBLE);
                                        finish();
                                        boolean flag = SPUtils.getBoolean(SplashScreenActivity.this, Constants.SP_FILE_NAME, "login", false);
                                        Log.i("TAG", "" + loginUser.isLogin());
                                        if (flag) {
                                            Log.i(TAG, "Login success");
                                            startActivity(new Intent(SplashScreenActivity.this,
                                                    MapsActivity.class));
                                        } else {
                                            String content = Constants
                                                    .createErrorInfo(loginUser.getErrorMsg())
                                                    + " 错误码：" + loginUser.getErrorMsg();
                                            Toast.makeText(SplashScreenActivity.this, content,
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }



    @Override
    protected void onDestroy() {
        UserInfoSync.saveLoginInfo(SplashScreenActivity.this, loginUser);
        super.onDestroy();
    }

    /**
     * 检查自动登录是否过期
     *
     * @return 超过：true  没有超过：false
     */
    private boolean isOutOfDate() {
        //获取当前时间
        long timeNow = System.currentTimeMillis();
        Log.i(TAG, "now:" + timeNow);
        //获取用户上次登录时间
        //long timePre = SPUtils.getLong(this, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, 0);
        long timePre = loginUser.getLoginTime();
        Log.i(TAG, "pre:" + timePre);
        //当用户本次登陆时间大于上次登录时间一个月
        boolean result = timeNow - timePre > Constants.OUT_OF_DATE_LIMIT;
        return result;
    }

}
