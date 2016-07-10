package com.sealiu.piece.controller.User;

import android.content.Context;

import android.support.annotation.Nullable;
import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;


import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by art2cat on 7/6/2016.
 */
public class UserInfoSync {
    private String nickname,  bio, sex, birth, email, phone_number;
    private String nickname1, bio1, sex1, birth1, email1, phone_number1;
    private String nicknameE, bioE, sexE, birthE, emailE, phone_numberE;
    private String nicknameD, bioD, sexD, birthD, emailD, phone_numberD;
    private double time1;
    private long time;
    private boolean isAutoLogin, phoneNumberVerified, emailVerified;
    private static final String TAG = "UserInfoSync";

    /**
     * 上传用户更改后用户信息
     * @param context
     * @param user
     * @param objectId
     * @param filename
     * @return
     */
    public void upload(final Context context, User user, String objectId, final String filename) throws Exception {
        nickname = SPUtils.getString(context, filename, Constants.SP_NICKNAME, null);
        bio = SPUtils.getString(context, filename, Constants.SP_BIO, null);
        sex = SPUtils.getString(context, filename, Constants.SP_SEX, null);
        birth = SPUtils.getString(context, filename, Constants.SP_BIRTH, null);
        email = SPUtils.getString(context, filename, Constants.SP_EMAIL, null);
        phone_number = SPUtils.getString(context, filename, Constants.SP_PHONE_NUMBER, null);
        long timeNow = System.currentTimeMillis();
        SPUtils.putLong(context, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, timeNow);
        Log.i(TAG, "pre:" + timeNow);

        user.setNickname(nickname);
        user.setBio(bio);
        user.setBirth(birth);
        user.setUser_sex(sex);
        user.setEmail(email);
        user.setMobilePhoneNumber(phone_number);
        user.setTime(timeNow);
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
     * @param context
     * @param
     */
    public void getUserInfo(final Context context) throws Exception {
        String username = (String) BmobUser.getObjectByKey(Constants.SP_USERNAME);
        Log.i(TAG, "get username:" + username);
        if (username != null) {
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereEqualTo(username, "lucky");
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, "done");
                    } else {
                        Log.i(TAG, "fail");
                    }
                }
            });
        }
        nickname1 = (String) BmobUser.getObjectByKey(Constants.SP_NICKNAME);
        Log.i(TAG, "get nickname：" + nickname1);

        bio1 = (String) BmobUser.getObjectByKey(Constants.SP_BIO);
        Log.i(TAG, "get bio：" + bio1);

        sex1 = (String) BmobUser.getObjectByKey(Constants.SP_SEX);
        Log.i(TAG, "get sex：" + sex1);

        birth1 = (String) BmobUser.getObjectByKey(Constants.SP_BIRTH);
        Log.i(TAG, "get birth:"+ birth1);

        phone_number1 = (String) BmobUser.getObjectByKey(Constants.SP_PHONE_NUMBER);
        Log.i(TAG, "get phone:" + phone_number1);

        emailVerified = (Boolean) BmobUser.getObjectByKey(Constants.SP_EMAIL_VERIFIED);
        Log.i(TAG, "get email verified:" + emailVerified);

        phoneNumberVerified = (Boolean) BmobUser.getObjectByKey(Constants.SP_PHONE_NUMBER_VERIFIED);
        Log.i(TAG, "get phone number verified:" + phoneNumberVerified);

        String password = (String) BmobUser.getObjectByKey(Constants.SP_PASSWORD);
        Log.i(TAG, "get password:" + password);

        String username1 = (String) BmobUser.getObjectByKey(Constants.SP_USERNAME);
        Log.i(TAG, "get username:" + username);

        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, nickname1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_USERNAME, username1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIO, bio1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_SEX, sex1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIRTH, birth1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL, email1);
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, phone_number1);
        SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER_VERIFIED, phoneNumberVerified);
        SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL_VERIFIED, emailVerified);

        Log.i(TAG, "get userinfo");
    }

    public User getLoginInfo(Context context){
        try {
            getUserInfo(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User loginInfo = new User();
        //对数据进行读取
        loginInfo.setNickname(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, null));
        //loginInfo.setUsername(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_USERNAME, null));
        loginInfo.setBio(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_BIO, null));
        loginInfo.setUser_sex(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_SEX, null));
        loginInfo.setBirth(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_BIRTH, null));
        loginInfo.setEmail(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL, null));
        loginInfo.setMobilePhoneNumber(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, null));
        loginInfo.setAutoLogin(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, false));
        loginInfo.setObjectId(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null));
        loginInfo.setMobilePhoneNumberVerified(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER_VERIFIED, false));
        loginInfo.setEmailVerified(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL_VERIFIED, false));
        loginInfo.setTime(SPUtils.getLong(context, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, 0));
        loginInfo.setPwd(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null));
        return loginInfo;
    }

    public void saveLoginInfo (Context context, User loginInfo){

        //对更新后的数据进行保存
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, loginInfo.getNickname());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIO, loginInfo.getBio());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_SEX, loginInfo.getUser_sex());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIRTH, loginInfo.getBirth());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL, loginInfo.getEmail());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, loginInfo.getMobilePhoneNumber());
        SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, loginInfo.isAutoLogin());
        SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER_VERIFIED, loginInfo.getMobilePhoneNumberVerified());
        SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL_VERIFIED, loginInfo.getEmailVerified());
        SPUtils.putLong(context, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, loginInfo.getTime());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, loginInfo.getObjectId());
        SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, loginInfo.getPwd());
        //SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_USERNAME, loginInfo.getUsername());
        Log.i(TAG, "nickname:" + loginInfo.getNickname());
        Log.i(TAG, "pwd:" + loginInfo.getPwd());
        Log.i(TAG, "save user info done");

    }

}
