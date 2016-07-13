package com.sealiu.piece.controller.Piece;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.SPUtils;

import java.io.InputStream;
import java.net.URL;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WritePieceActivity extends AppCompatActivity {

    private static final String TAG = "WritePieceActivity";

    private Double mLatitude, mLongitude;
    private String mLocationName;

    private TextView myLocationTV;
    private ImageView headPictureIV;
    private TextView nickNameTV;
    private Spinner visibilitySpinner;
    private EditText piectContentET;

    private RelativeLayout snackBarHolderView;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_piece);

        snackBarHolderView = (RelativeLayout) findViewById(R.id.layout_holder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myLocationTV = (TextView) findViewById(R.id.my_location_name);
        nickNameTV = (TextView) findViewById(R.id.user_nickname);
        headPictureIV = (ImageView) findViewById(R.id.user_head_picture);
        visibilitySpinner = (Spinner) findViewById(R.id.visibility);
        piectContentET = (EditText) findViewById(R.id.piece_content);

        initUI();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_piece, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send_piece:
                // 发送编写的Piece

                final String objectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, "");
                final String pieceContent = piectContentET.getText().toString();
                final int visibilityPosition = visibilitySpinner.getSelectedItemPosition();

                if (pieceContent.equals("")) {
                    Snackbar.make(snackBarHolderView, "请填写内容", Snackbar.LENGTH_LONG).show();
                    break;
                }

                //向Bmob后台写数据
                Log.i(TAG, "用户ID：" + objectId + "; 可见范围：" + visibilityPosition + "; 纸条内容：" + pieceContent);
                Piece piece = new Piece(objectId, pieceContent, mLatitude, mLongitude, visibilityPosition);
                piece.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("errorCode", e.getErrorCode());
                            setResult(RESULT_CANCELED, intent);
                        }
                        finish();
                    }
                });

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化界面显示（头像，昵称，位置信息）
     */
    private void initUI() {

        mLatitude = getIntent().getDoubleExtra("LAT", 0);
        mLongitude = getIntent().getDoubleExtra("LNG", 0);
        mLocationName = getIntent().getStringExtra("LOC");

        String detailPosition = mLocationName + " (" + mLatitude + " ," + mLongitude + ")";
        myLocationTV.setText(detailPosition);

        String nickName = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, "");
        final String headPicture = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_HEAD_PICTURE, "");


        if (!nickName.equals("")) {
            nickNameTV.setText(nickName);
        }

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
