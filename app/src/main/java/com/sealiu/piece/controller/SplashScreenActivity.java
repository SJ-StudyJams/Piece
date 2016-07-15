package com.sealiu.piece.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView pg1;
    ImageView pg2;
    ImageView pg3;
    ImageView pg4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv1 = (ImageView) findViewById(R.id.imageView1);
        iv2 = (ImageView) findViewById(R.id.imageView2);
        iv3 = (ImageView) findViewById(R.id.imageView3);
        iv4 = (ImageView) findViewById(R.id.imageView4);

        pg1 = (ImageView) findViewById(R.id.progress1);
        pg2 = (ImageView) findViewById(R.id.progress2);
        pg3 = (ImageView) findViewById(R.id.progress3);
        pg4 = (ImageView) findViewById(R.id.progress4);

        Animation upAndDown1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.up_down);
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
                        Animation upAndDown3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.up_down);
                        iv3.startAnimation(upAndDown3);
                        upAndDown3.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                pg3.setVisibility(View.VISIBLE);
                                Animation upAndDown4 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.up_down);
                                iv4.startAnimation(upAndDown4);
                                upAndDown4.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        pg4.setVisibility(View.VISIBLE);
                                        finish();
                                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
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

}
