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
import android.widget.RadioGroup;

import com.sealiu.piece.R;
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
        EditBioFragment.EditBioDialogListener, EditPhoneFragment.EditPhoneDialogListener, View.OnClickListener {

    private User user;
    private EditText usernameET, bioET, birthET, phoneET;
    private static final String TAG = "EditActivity";
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        user = new User();
        BmobUser user1 = User.getCurrentUser();
        objectId = user1.getObjectId();
        Log.i(TAG, "id:" + objectId);
        //objectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null);
        UserInfoSync sync = new UserInfoSync();
        sync.getUserInfo(this, objectId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_user_info);

        //显示昵称
        usernameET = (EditText) findViewById(R.id.user_name);
        String nickname = SPUtils.getString(this, objectId, Constants.SP_NICKNAME, null);
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
        String birth = SPUtils.getString(this, objectId, Constants.SP_BIRTH, null);
        Log.i(TAG, "生日：" + birth);
        if (birth == null) {
            birthET.setText("点击设置");
        } else {
            birthET.setText(birth);
        }

        //显示手机号
        phoneET = (EditText) findViewById(R.id.user_phone);
        String phone = SPUtils.getString(this, objectId, Constants.SP_PHONE_NUMBER, null);
        Log.i(TAG, "手机号：" + phone);
        if (phone == null) {
            phoneET.setText("点击设置");
        } else {
            phoneET.setText(phone);
        }

        //修改昵称
        findViewById(R.id.user_name).setOnClickListener(this);

        //修改性别
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_sex);
        String sex = SPUtils.getString(this, objectId, Constants.SP_SEX, null);
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
                        SPUtils.putString(EditActivity.this, objectId, Constants.SP_SEX, "1");
                        break;
                    case R.id.user_sex_female:
                        SPUtils.putString(EditActivity.this, objectId, Constants.SP_SEX, "2");
                        break;
                    case R.id.user_sex_secret:
                        SPUtils.putString(EditActivity.this, objectId, Constants.SP_SEX, "3");
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
        findViewById(R.id.user_email).setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.user_name:
                new EditNameFragment()
                        .show(getSupportFragmentManager(), "Edit_Name");
                break;
            case R.id.user_bio:
                new EditBioFragment()
                        .show(getSupportFragmentManager(), "Edit_Bio");
                break;
            case R.id.user_birth:
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
                break;
            case R.id.head_picture:
                Snackbar.make(view, "headPicture", Snackbar.LENGTH_SHORT).setAction("Action", null)
                        .show();
                break;
            case R.id.user_phone:
                new EditPhoneFragment()
                        .show(getSupportFragmentManager(), "Edit_Phone");
                break;
            case R.id.user_email:
                //
                break;
        }
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
    public void onEditPhoneDialogPositiveClick(DialogFragment dialog, String phone) {
        SPUtils.putString(this, objectId, Constants.SP_PHONE_NUMBER, phone);
        phoneET.setText(phone);
    }

    @Override
    public void onEditPhoneDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onDestroy() {
        UserInfoSync userInfoSync = new UserInfoSync();
        userInfoSync.upload(this, user, objectId, objectId);
        super.onDestroy();
    }
}