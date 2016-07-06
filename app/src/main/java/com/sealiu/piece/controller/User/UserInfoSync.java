package com.sealiu.piece.controller.User;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat on 7/6/2016.
 */
public class UserInfoSync {
    private String nickname, password, bio, sex, birth, email, phone_number;
    private static final String TAG = "UserInfoSync";

    /**
     * 上传用户更改后信息
     * @param context
     * @param user
     * @param objectId
     * @param filename
     * @return
     */
    public void upload(final Context context, User user, @Nullable String objectId, final String filename) {

        nickname = SPUtils.getString(context, filename, Constants.SP_NICKNAME, null);
        password = SPUtils.getString(context, filename, Constants.SP_PASSWORD, null);
        bio = SPUtils.getString(context, filename, Constants.SP_BIO, null);
        sex = SPUtils.getString(context, filename, Constants.SP_SEX, null);
        birth = SPUtils.getString(context, filename, Constants.SP_BIRTH, null);
        email = SPUtils.getString(context, filename, Constants.SP_EMAIL, null);
        phone_number = SPUtils.getString(context, filename, Constants.SP_PHONE_NUMBER, null);
        Log.i("UserInfoSync", "nick:" + nickname);
        Log.i("UserInfoSync", "bio:" + bio);
        Log.i("UserInfoSync", "sex:" + sex);
        Log.i("UserInfoSync", "bir:" + birth);
        Log.i("UserInfoSync", "ema:" + email);
        Log.i("UserInfoSync", "pho:" + phone_number);
        Log.i("UserInfoSync", "pwd:" + password);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setBio(bio);
        user.setBirth(birth);
        user.setUser_sex(sex);
        user.setEmail(email);
        user.setMobilePhoneNumber(phone_number);
        user.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("EditActivity", "update success");
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    /**
     * 从服务器获取当前用户信息
     * @param context
     * @param filename
     */
    public void getUserInfo(Context context, String filename) {
        nickname = (String) User.getObjectByKey(Constants.SP_NICKNAME);
        bio = (String) User.getObjectByKey(Constants.SP_BIO);
        sex = (String) User.getObjectByKey(Constants.SP_SEX);
        birth = (String) User.getObjectByKey(Constants.SP_BIRTH);
        email = (String) User.getObjectByKey(Constants.SP_EMAIL);
        phone_number = (String) User.getObjectByKey(Constants.SP_PHONE_NUMBER);
        password = (String) User.getObjectByKey(Constants.SP_PASSWORD);
        SPUtils.putString(context, filename, Constants.SP_NICKNAME, nickname);
        SPUtils.putString(context, filename, Constants.SP_PASSWORD, password);
        SPUtils.putString(context, filename, Constants.SP_BIO, bio);
        SPUtils.putString(context, filename, Constants.SP_SEX, sex);
        SPUtils.putString(context, filename, Constants.SP_BIRTH, birth);
        SPUtils.putString(context, filename, Constants.SP_EMAIL, email);
        SPUtils.putString(context, filename, Constants.SP_PHONE_NUMBER, phone_number);
        Log.i("UserInfoSync", "nick:" + nickname);
        Log.i("UserInfoSync", "bio:" + bio);
        Log.i("UserInfoSync", "sex:" + sex);
        Log.i("UserInfoSync", "bir:" + birth);
        Log.i("UserInfoSync", "ema:" + email);
        Log.i("UserInfoSync", "pho:" + phone_number);
        Log.i("UserInfoSync", "pwd:" + password);
    }

}
