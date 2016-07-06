package com.sealiu.piece.controller.User;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 展示用户的个人信息（头像，昵称，简介）
 * 点击相应信息弹出FragmentDialog，进行设置。
 */
public class EditActivity extends AppCompatActivity implements
        EditNameFragment.EditNameDialogListener,
        EditBioFragment.EditBioDialogListener {

    private User user = new User();
    private BmobUser user1 = BmobUser.getCurrentUser();
    private EditText usernameET, bioET, birthET;
    private static final String TAG = "EditActivity";
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        objectId = user1.getObjectId();
        UserInfoSync sync = new UserInfoSync();
        sync.getUserInfo(this, objectId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_user_info);




        usernameET = (EditText) findViewById(R.id.user_name);
        String nickname = SPUtils.getString(this, objectId, Constants.SP_NICKNAME, null);
        Log.i(TAG, "昵称为：" + nickname);
        if (nickname == null) {
            usernameET.setText("点击设置");
        } else {
            usernameET.setText(nickname);
        }

        bioET = (EditText) findViewById(R.id.user_bio);
        String bio = SPUtils.getString(this, objectId, Constants.SP_BIO, null);
        Log.i(TAG, "个人简介：" + bio);
        if (bio == null) {
            bioET.setText("点击设置");
        } else {
            bioET.setText(bio);
        }

        birthET = (EditText) findViewById(R.id.user_birth);
        String birth = SPUtils.getString(this, objectId, Constants.SP_BIRTH, null);
        Log.i(TAG, "生日：" + birth);
        if (birth == null) {
            birthET.setText("点击设置");
        } else {
            birthET.setText(birth);
        }

        //修改昵称
        findViewById(R.id.user_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditNameFragment()
                        .show(getSupportFragmentManager(), "Edit_Name");
            }
        });

        //修改个人简介
        findViewById(R.id.user_bio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditBioFragment()
                        .show(getSupportFragmentManager(), "Edit_Bio");
            }
        });

        //修改生日
        findViewById(R.id.user_birth).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "birth", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String birth = year + "-" + month
                                + "-" + dayOfMonth;
                        SPUtils.putString(EditActivity.this, objectId, Constants.SP_BIRTH, birth);
                        birthET.setText(birth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
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

    // 修改昵称对话框（确定修改）
    @Override
    public void onEditNameDialogPositiveClick(DialogFragment dialog, String name) {
        SPUtils.putString(this, objectId, Constants.SP_NICKNAME, name);
        usernameET.setText(name);
        //执行username更新操作\
    }

    // 修改昵称对话框（取消修改）
    @Override
    public void onEditNameDialogNegativeClick(DialogFragment dialog) {
        //取消修改
    }

    // 修改个人简介对话框（确定修改）
    @Override
    public void onEditBioDialogPositiveClick(DialogFragment dialog, String bio) {
        SPUtils.putString(this, objectId, Constants.SP_BIO, bio);
        bioET.setText(bio);
    }

    // 修改个人简介对话框（取消修改）
    @Override
    public void onEditBioDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onDestroy() {
        UserInfoSync userInfoSync = new UserInfoSync();
        userInfoSync.upload(this, user, objectId, objectId);
        super.onDestroy();
    }
}