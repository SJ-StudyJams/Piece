package com.sealiu.piece.controller.User;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.BmobUser;

/**
 * 展示用户的个人信息（头像，昵称，简介）
 * 点击相应信息弹出FragmentDialog，进行设置。
 */
public class EditActivity extends AppCompatActivity implements
        EditNameFragment.EditNameDialogListener,
        EditBioFragment.EditBioDialogListener, EditPhoneFragment.EditPhoneDialogListener,
        EditEmailFragment.EditEmailDialogListener, EditBirthFragment.EditBirthDialogListener,
        EditPwdFragment.EditPwdDialogListener, View.OnClickListener {

    private User user;
    private EditText usernameET, bioET, birthET, phoneET, emailET;
    private static final String TAG = "EditActivity";
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        user = new User();
        //获取当前用户
        BmobUser user1 = User.getCurrentUser();
        //获取objectId
        objectId = user1.getObjectId();
        if (objectId == null) {
            objectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null);
        }

        Log.i(TAG, "id:" + objectId);
        //objectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null);
        UserInfoSync sync = new UserInfoSync();
        try {
            sync.getUserInfo(this, objectId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_user_info);

        //显示昵称
        usernameET = (EditText) findViewById(R.id.user_name);
        String nickname = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, null);

        //String nickname1 = null;
        //if (nickname != null) {
        //    rsaCipherStrategy.initPrivateKey(Constants.PRIVATE_KEY);
        //    nickname1 = rsaCipherStrategy.decrypt(nickname);
        //}
        //Log.i(TAG, "decrypt nickname done");
        Log.i(TAG, "昵称为：" + nickname);
        if (nickname == null) {
            usernameET.setText("点击设置");
        } else {
            usernameET.setText(nickname);
        }

        //显示个人简介
        bioET = (EditText) findViewById(R.id.user_bio);
        String bio = SPUtils.getString(this, objectId, Constants.SP_BIO, null);
        Log.i(TAG, "个人简介：" + bio);
        if (bio == null) {
            bioET.setText("点击设置");
        } else {
            bioET.setText(bio);
        }

        //显示生日
        birthET = (EditText) findViewById(R.id.user_birth);
        String birth = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_BIRTH, null);
        Log.i(TAG, "生日：" + birth);
        if (birth == null) {
            birthET.setText("点击设置");
        } else {
            birthET.setText(birth);
        }

        //显示手机号
        phoneET = (EditText) findViewById(R.id.user_phone);
        String phone = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, null);
        Log.i(TAG, "手机号：" + phone);
        if (phone == null) {
            phoneET.setText("点击设置");
        } else {
            phoneET.setText(phone);
        }

        //显示邮箱
        emailET = (EditText) findViewById(R.id.user_email);
        String email = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_EMAIL, null);
        Log.i(TAG, "Email：" + email);
        if (phone == null) {
            emailET.setText("点击设置");
        } else {
            emailET.setText(email);
        }

        //修改昵称
        findViewById(R.id.user_name).setOnClickListener(this);

        //修改性别
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_sex);
        String sex = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_SEX, null);
        Log.i(TAG, "性别：" + sex);
        if (sex != null) {
            switch (sex) {
                case "1":
                    radioGroup.check(R.id.user_sex_male);
                    break;
                case "2":
                    radioGroup.check(R.id.user_sex_female);
                    break;
                case "3":
                    radioGroup.check(R.id.user_sex_secret);
                    break;
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.user_sex_male:
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_SEX, "1");
                        break;
                    case R.id.user_sex_female:
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_SEX, "2");
                        break;
                    case R.id.user_sex_secret:
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_SEX, "3");
                        break;
                }
            }
        });

        //修改个人简介
        bioET.setOnClickListener(this);

        //修改生日
        birthET.setOnClickListener(this);

        //修改头像
        findViewById(R.id.head_picture).setOnClickListener(this);

        //修改手机号
        phoneET.setOnClickListener(this);

        //修改邮箱
        emailET.setOnClickListener(this);

        //修改密码
        Button button = (Button) findViewById(R.id.user_pwd);
        button.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.user_name:
                new EditNameFragment().show(getSupportFragmentManager(), "Edit_Name");
                break;
            case R.id.user_bio:
                new EditBioFragment().show(getSupportFragmentManager(), "Edit_Bio");
                break;
            case R.id.user_birth:
                new EditBirthFragment().show(getSupportFragmentManager(), "Edit_Birth");
                break;
            case R.id.head_picture:
                Snackbar.make(view, "headPicture", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
                break;
            case R.id.user_phone:
                new EditPhoneFragment().show(getSupportFragmentManager(), "Edit_Phone");
                break;
            case R.id.user_email:
                new EditEmailFragment().show(getSupportFragmentManager(), "Edit_Email");
                break;
            case R.id.user_pwd:
                new EditPwdFragment().show(getSupportFragmentManager(), "Edit_Password");
                break;
        }
    }

    // 修改昵称对话框（确定修改）
    @Override
    public void onEditNameDialogPositiveClick(DialogFragment dialog, String name) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, name);
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
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_BIO, bio);
        bioET.setText(bio);
    }

    // 修改个人简介对话框（取消修改）
    @Override
    public void onEditBioDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改电话对话框（确定修改）
    @Override
    public void onEditPhoneDialogPositiveClick(DialogFragment dialog, String phone) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, phone);
        phoneET.setText(phone);
    }

    // 修改电话对话框（取消修改）
    @Override
    public void onEditPhoneDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改邮箱对话框（确定修改）
    @Override
    public void onEditEmailDialogPositiveClick(DialogFragment dialog, String email) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_EMAIL, email);
        emailET.setText(email);
    }
    // 修改邮箱对话框（取消修改）
    @Override
    public void onEditEmailDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改生日对话框（确定修改）
    public void onEditBirthDialogPositiveClick(DialogFragment dialog, String birth) {
        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_BIRTH, birth);
        birthET.setText(birth);

    }

    // 修改生日对话框（取消修改）
    public void onEditBirthDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改密码对话框（确定修改）
    @Override
    public void onEditPwdDialogPositiveClick(DialogFragment dialog) {
        SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    // 修改密码对话框（取消修改）
    @Override
    public void onEditPwdDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onDestroy() {
        UserInfoSync userInfoSync = new UserInfoSync();
        try {
            userInfoSync.upload(this, user, objectId, Constants.SP_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}