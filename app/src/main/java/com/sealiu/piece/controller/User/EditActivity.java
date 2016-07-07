package com.sealiu.piece.controller.User;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 展示用户的个人信息（头像，昵称，简介）
 * 点击相应信息弹出FragmentDialog，进行设置。
 */
public class EditActivity extends AppCompatActivity implements
        EditNameFragment.EditNameDialogListener,
        EditBioFragment.EditBioDialogListener,
        EditEmailFragment.EditEmailDialogListener,
        EditPhoneFragment.EditPhoneDialogListener,
        EditPwdFragment.EditPwdDialogListener {

    private User user = new User();
    private BmobUser user1 = BmobUser.getCurrentUser();
    private EditText usernameET, bioET, birthET;
    private static final String TAG = "EditActivity";
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_user_info);


        objectId = user1.getObjectId();

        usernameET = (EditText) findViewById(R.id.user_name);
        String nickname = (String) User.getObjectByKey(Constants.SP_NICKNAME);
        Log.i(TAG, "昵称为：" + nickname);
        if (nickname == null) {
            usernameET.setText("点击设置");
        } else {
            usernameET.setText(nickname);
        }

        bioET = (EditText) findViewById(R.id.user_bio);
        String bio = (String) User.getObjectByKey(Constants.SP_BIO);
        Log.i(TAG, "个人简介：" + bio);
        if (bio == null) {
            bioET.setText("点击设置");
        } else {
            bioET.setText(bio);
        }

        birthET = (EditText) findViewById(R.id.user_birth);
        String birth = (String) User.getObjectByKey(Constants.SP_BIRTH);
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
                        user.setBirth(birth);
                        user.update(user1.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    Log.i(TAG, "更新用户信息成功");
                                }else{
                                    Log.i(TAG, "更新用户信息失败:" + e.getMessage());
                                }
                            }
                        });
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

        findViewById(R.id.user_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditEmailFragment().show(getSupportFragmentManager(), "Edit_Email");
            }
        });

        findViewById(R.id.user_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditPhoneFragment().show(getSupportFragmentManager(), "Edit_Phone");
            }
        });

        findViewById(R.id.user_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditPwdFragment().show(getSupportFragmentManager(), "Edit_Pwd");
            }
        });

    }

    // 修改昵称对话框（确定修改）
    @Override
    public void onEditNameDialogPositiveClick(DialogFragment dialog, String name) {
        user.setNickname(name);
        Log.i(TAG, "id" + objectId);
        user.update(user1.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.i(TAG, "更新用户信息成功");
                }else{
                    Log.i(TAG, "更新用户信息失败:" + e.getMessage());
                }
            }
        });
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
        user.setBio(bio);
        user.update(user1.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.i(TAG, "更新用户信息成功");
                }else{
                    Log.i(TAG, "更新用户信息失败:" + e.getMessage());
                }
            }
        });
        bioET.setText(bio);
    }

    // 修改个人简介对话框（取消修改）
    @Override
    public void onEditBioDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onEditEmailDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onEditEmailDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onEditPhoneDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onEditPhoneDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onEditPwdDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onEditPwdDialogNegativeClick(DialogFragment dialog) {

    }
}