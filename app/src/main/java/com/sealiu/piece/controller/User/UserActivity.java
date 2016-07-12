package com.sealiu.piece.controller.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * 展示用户发送过的小纸条（类似朋友圈）
 * 目前只提供点击用户头像进入查看页面，小纸条展示放到Piece模块中开发
 */
public class UserActivity extends AppCompatActivity {


    private Bitmap bitmap;
    private ImageView headPictureIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headPictureIV = (ImageView) findViewById(R.id.head_picture);
        headPictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, EditActivity.class));
            }
        });

        initUI();


    }

    /**
     * 初始化界面显示
     */
    private void initUI() {

        String nickName = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, "");
        final String headPicture = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_HEAD_PICTURE, "");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(nickName);
        }

        //显示头像
        if (!headPicture.equals("")) {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x9527) {
                        //显示下载之后的图片
                        headPictureIV.setImageBitmap(bitmap);
                    }
                }
            };

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        URL url = new URL(headPicture);
                        //打开URL对应的资源输入流
                        InputStream inputStream = url.openStream();
                        //从InputStream流中解析出图片
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        //发送消息，通知UI组件显示图片
                        handler.sendEmptyMessage(0x9527);
                        //关闭输入流
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }
}
