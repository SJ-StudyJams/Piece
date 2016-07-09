package com.sealiu.piece.controller.User;

import android.content.Context;
import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat
 * on 7/6/2016.
 */
public class UserInfoSync {
    private String nickname, bio, sex, birth, email, phone_number;
    private boolean is_valid_email, is_valid_phone;
    private static final String TAG = "UserInfoSync";

    /**
     * 上传用户更改后用户信息
     *
     * @param context  上下文对象
     * @param objectId user 的objectId （bmob后台生成）
     * @param filename SP 文件名
     */
    public void upload(final Context context, String objectId, final String filename) throws Exception {
        nickname = SPUtils.getString(context, filename, Constants.SP_NICKNAME, "");
        bio = SPUtils.getString(context, filename, Constants.SP_BIO, "");
        sex = SPUtils.getString(context, filename, Constants.SP_SEX, "");
        birth = SPUtils.getString(context, filename, Constants.SP_BIRTH, "");
        email = SPUtils.getString(context, filename, Constants.SP_EMAIL, "");
        phone_number = SPUtils.getString(context, filename, Constants.SP_PHONE_NUMBER, "");

        User user = new User();
        user.setNickname(nickname);
        user.setBio(bio);
        user.setBirth(birth);
        user.setUser_sex(sex);
        user.setEmail(email);
        user.setMobilePhoneNumber(phone_number);

        user.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i(TAG, "update success");
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    /**
     * 从服务器获取当前用户信息
     *
     * @param context  上下文对象
     * @param filename SP 文件名
     */
    public void getUserInfo(Context context, String filename) throws Exception {
        nickname = (String) User.getObjectByKey(Constants.SP_NICKNAME);
        bio = (String) User.getObjectByKey(Constants.SP_BIO);
        sex = (String) User.getObjectByKey(Constants.SP_SEX);
        birth = (String) User.getObjectByKey(Constants.SP_BIRTH);
        email = (String) User.getObjectByKey(Constants.SP_EMAIL);
        phone_number = (String) User.getObjectByKey(Constants.SP_PHONE_NUMBER);
        is_valid_email = (boolean) User.getObjectByKey(Constants.SP_IS_VALID_EMAIL);
        is_valid_phone = (boolean) User.getObjectByKey(Constants.SP_IS_VALID_PHONE_NUMBER);

        SPUtils.putString(context, filename, Constants.SP_NICKNAME, nickname);
        SPUtils.putString(context, filename, Constants.SP_BIO, bio);
        SPUtils.putString(context, filename, Constants.SP_SEX, sex);
        SPUtils.putString(context, filename, Constants.SP_BIRTH, birth);
        SPUtils.putString(context, filename, Constants.SP_EMAIL, email);
        SPUtils.putString(context, filename, Constants.SP_PHONE_NUMBER, phone_number);
        SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_EMAIL, is_valid_email);
        SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_PHONE_NUMBER, is_valid_phone);

        Log.i(TAG, "getUserInfo success");
    }
}
