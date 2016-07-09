package com.sealiu.piece.controller.User;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobUser;

/**
 * 展示用户的个人信息（头像，昵称，简介）
 * 点击相应信息弹出FragmentDialog，进行设置。
 */
public class EditActivity extends AppCompatActivity implements
        EditNameFragment.EditNameDialogListener,
        EditBioFragment.EditBioDialogListener,
        EditPhoneFragment.EditPhoneDialogListener,
        EditEmailFragment.EditEmailDialogListener,
        EditBirthFragment.EditBirthDialogListener,
        EditPwdFragment.EditPwdDialogListener,
        PickPictureFragment.PickPictureListener,
        View.OnClickListener {

    private static final String TAG = "EditActivity";
    private EditText usernameET, bioET, birthET, phoneET, emailET;
    private ImageView headPicture;
    private String objectId;
    private Uri imageUri;

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 111;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 222;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;

    public static NestedScrollView layoutScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        layoutScroll = (NestedScrollView) findViewById(R.id.scroll_view);

        objectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, "");

        if (objectId.equals("")) {
            //获取当前用户
            BmobUser bmobUser = User.getCurrentUser();
            //获取objectId
            objectId = bmobUser.getObjectId();
        }

        // 从bmob后台同步用户信息到sp文件中存储
        UserInfoSync sync = new UserInfoSync();
        try {
            sync.getUserInfo(this, Constants.SP_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.edit_user_info);
        }

        // 将sp文件中存储的用户信息显示出来，并设置监听
        displayContent();
    }

    /**
     * 显示个人资料内容，并设置监听函数修改资料
     */
    private void displayContent() {
        String nickname = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, "");
        String sex = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_SEX, "");
        String bio = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_BIO, "");
        String birth = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_BIRTH, "");
        String phone = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, "");
        String email = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_EMAIL, "");

        usernameET = (EditText) findViewById(R.id.user_name);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_sex);
        bioET = (EditText) findViewById(R.id.user_bio);
        headPicture = (ImageView) findViewById(R.id.head_picture);
        birthET = (EditText) findViewById(R.id.user_birth);
        phoneET = (EditText) findViewById(R.id.user_phone);
        emailET = (EditText) findViewById(R.id.user_email);
        Button changePwdBTN = (Button) findViewById(R.id.user_pwd);


        //显示昵称
        if (nickname.equals("")) {
            usernameET.setText("点击设置");
        } else {
            usernameET.setText(nickname);
        }

        //显示个人简介
        if (bio.equals("")) {
            bioET.setText("点击设置");
        } else {
            bioET.setText(bio);
        }

        //显示生日
        if (birth.equals("")) {
            birthET.setText("点击设置");
        } else {
            birthET.setText(birth);
        }

        //显示手机号
        if (phone.equals("")) {
            phoneET.setText("点击设置");
        } else {
            phoneET.setText(phone);
        }

        //显示邮箱
        if (email.equals("")) {
            emailET.setText("点击设置");
        } else {
            emailET.setText(email);
        }

        //显示手机/邮箱的验证状态
        updateVerifiedStatus();

        //修改昵称,个人简介,生日,头像,手机号,邮箱,密码
        usernameET.setOnClickListener(this);
        bioET.setOnClickListener(this);
        birthET.setOnClickListener(this);
        headPicture.setOnClickListener(this);
        phoneET.setOnClickListener(this);
        emailET.setOnClickListener(this);
        changePwdBTN.setOnClickListener(this);

        //显示性别
        if (!sex.equals("")) {
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
        } else {
            radioGroup.check(R.id.user_sex_secret);
        }

        //修改性别
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
    }

    /**
     * 初始化手机/邮箱的验证状态
     * 如果修改了手机/邮箱，那么是否验证就会改变
     */
    private void updateVerifiedStatus() {
        boolean isValidPhone = SPUtils.getBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_PHONE_NUMBER, false);
        boolean isValidEmail = SPUtils.getBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_EMAIL, false);

        ImageView phoneIsValidIV = (ImageView) findViewById(R.id.phone_is_valid);
        ImageView emailIsValidIV = (ImageView) findViewById(R.id.email_is_valid);

        if (isValidPhone)
            phoneIsValidIV.setVisibility(View.VISIBLE);
        else
            phoneIsValidIV.setVisibility(View.INVISIBLE);

        if (isValidEmail)
            emailIsValidIV.setVisibility(View.VISIBLE);
        else
            emailIsValidIV.setVisibility(View.INVISIBLE);
    }

    //实现控件的监听，打开对应的对话框
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_name:
                new EditNameFragment().show(getSupportFragmentManager(), "Edit_Name");
                break;
            case R.id.user_bio:
                new EditBioFragment().show(getSupportFragmentManager(), "Edit_Bio");
                break;
            case R.id.user_birth:
                new EditBirthFragment().show(getSupportFragmentManager(), "Edit_Birth");
                break;
            case R.id.head_picture:
                PickPictureFragment ppFragment = new PickPictureFragment();
                ppFragment.show(getSupportFragmentManager(), "Pick_Picture");
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
    public void onEditNameDialogPositiveClick(DialogFragment dialog, String newNickname, final String oldNickname) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, newNickname);
        usernameET.setText(newNickname);

        Snackbar.make(layoutScroll, "昵称修改成功", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, oldNickname);
                        if (oldNickname.equals(""))
                            usernameET.setText("点击设置");
                        else
                            usernameET.setText(oldNickname);
                    }
                }).show();
    }

    // 修改昵称对话框（取消修改）
    @Override
    public void onEditNameDialogNegativeClick(DialogFragment dialog) {
        //取消修改
    }

    // 修改个人简介对话框（确定修改）
    @Override
    public void onEditBioDialogPositiveClick(DialogFragment dialog, String newBio, final String oldBio) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_BIO, newBio);
        bioET.setText(newBio);

        Snackbar.make(layoutScroll, "个人简介修改成功", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_BIO, oldBio);
                        if (oldBio.equals(""))
                            bioET.setText("点击设置");
                        else
                            bioET.setText(oldBio);
                    }
                }).show();
    }

    // 修改个人简介对话框（取消修改）
    @Override
    public void onEditBioDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改电话对话框（确定修改）
    @Override
    public void onEditPhoneDialogPositiveClick(DialogFragment dialog, String phone) {
        SPUtils.putString(this, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, phone);
        SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_PHONE_NUMBER, false);
        //改变手机号的验证状态
        updateVerifiedStatus();
        Snackbar.make(layoutScroll, "手机号修改成功", Snackbar.LENGTH_LONG).show();
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
        SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_EMAIL, false);
        //改变邮箱的验证状态
        updateVerifiedStatus();
        emailET.setText(email);
    }

    // 修改邮箱对话框（取消修改）
    @Override
    public void onEditEmailDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改生日对话框（确定修改）
    @Override
    public void onEditBirthDialogPositiveClick(DialogFragment dialog, String birthAfter, final String birthBefore) {
        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_BIRTH, birthAfter);
        birthET.setText(birthAfter);

        Snackbar.make(layoutScroll, "生日修改成功", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SPUtils.putString(EditActivity.this, Constants.SP_FILE_NAME, Constants.SP_BIRTH, birthBefore);
                        if (birthBefore.equals(""))
                            birthET.setText("点击设置");
                        else
                            birthET.setText(birthBefore);
                    }
                }).show();
    }

    // 修改生日对话框（取消修改）
    @Override
    public void onEditBirthDialogNegativeClick(DialogFragment dialog) {

    }

    // 修改密码对话框（确定修改）
    @Override
    public void onEditPwdDialogPositiveClick(DialogFragment dialog) {
    }

    // 修改密码对话框（取消修改）
    @Override
    public void onEditPwdDialogNegativeClick(DialogFragment dialog) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        UserInfoSync userInfoSync = new UserInfoSync();
        try {
            userInfoSync.upload(this, objectId, Constants.SP_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", "true");
                    // 裁剪框的比例，1：1
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, CROP_PHOTO);
                    } else {
                        Snackbar.make(layoutScroll, "没有裁剪图片程序", Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", "true");
                    // 裁剪框的比例，1：1
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, CROP_PHOTO);
                    } else {
                        Snackbar.make(layoutScroll, "没有裁剪图片程序", Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream(imageUri));
                        headPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCameraClick() {
        //启动相机
        File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Snackbar.make(layoutScroll, "没有相机程序", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAlbumClick() {
        //启动相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CHOOSE_PHOTO);
        } else {
            Snackbar.make(layoutScroll, "没有相册程序", Snackbar.LENGTH_LONG).show();
        }
    }
}