package com.sealiu.piece.controller.User;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sealiu.piece.R;

/**
 * 展示用户的个人信息（头像，昵称，简介）
 * 点击相应信息弹出FragmentDialog，进行设置。
 */
public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_user_info);

        //修改昵称
        findViewById(R.id.user_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "username", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
            }
        });

        //修改个人简介
        findViewById(R.id.user_bio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "bio", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
            }
        });

        //修改生日
        findViewById(R.id.user_birth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "birth", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
            }
        });


        //修改头像
        findViewById(R.id.head_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "headPicture", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
            }
        });


    }
}
