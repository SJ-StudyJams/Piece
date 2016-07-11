package com.sealiu.piece.controller.User;

import android.content.Context;
import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat
 * on 7/6/2016.
 */
public class UserInfoSync {
    private String nickname, bio, sex, birth, email, phone_number, picture;
    private boolean flag;
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
        picture = SPUtils.getString(context, filename, Constants.SP_HEAD_PICTURE, "");

        long loginTime = SPUtils.getLong(context, filename, Constants.SP_LOGIN_TIME, 0);

        User user = new User();
        user.setNickname(nickname);
        user.setBio(bio);
        user.setPicture(picture);
        user.setBirth(birth);
        user.setUser_sex(sex);
        user.setEmail(email);
        user.setMobilePhoneNumber(phone_number);
        user.setLoginTime(loginTime);

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
    public void getUserInfo(Context context, String filename, String username) throws Exception {

        query(context, filename, username);
        Log.i(TAG, "query success" + isFlag());


        if (isFlag()) {
            Log.i(TAG, "query success");
        } else {
            nickname = (String) User.getObjectByKey(Constants.SP_NICKNAME);
            bio = (String) User.getObjectByKey(Constants.SP_BIO);
            sex = (String) User.getObjectByKey(Constants.SP_SEX);
            birth = (String) User.getObjectByKey(Constants.SP_BIRTH);
            email = (String) User.getObjectByKey(Constants.SP_EMAIL);
            phone_number = (String) User.getObjectByKey(Constants.SP_PHONE_NUMBER);
            picture = (String) User.getObjectByKey(Constants.SP_HEAD_PICTURE);

            boolean is_valid_email = false;
            boolean is_valid_phone = false;

            if (User.getObjectByKey(Constants.SP_IS_VALID_EMAIL) != null) {
                is_valid_email = (boolean) User.getObjectByKey(Constants.SP_IS_VALID_EMAIL);
            }

            if (User.getObjectByKey(Constants.SP_IS_VALID_PHONE_NUMBER) != null) {
                is_valid_phone = (boolean) User.getObjectByKey(Constants.SP_IS_VALID_PHONE_NUMBER);
            }

            SPUtils.putString(context, filename, Constants.SP_NICKNAME, nickname);
            SPUtils.putString(context, filename, Constants.SP_BIO, bio);
            SPUtils.putString(context, filename, Constants.SP_SEX, sex);
            SPUtils.putString(context, filename, Constants.SP_BIRTH, birth);
            SPUtils.putString(context, filename, Constants.SP_EMAIL, email);
            SPUtils.putString(context, filename, Constants.SP_PHONE_NUMBER, phone_number);
            SPUtils.putString(context, filename, Constants.SP_HEAD_PICTURE, picture);
            SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_EMAIL, is_valid_email);
            SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_PHONE_NUMBER, is_valid_phone);

            Log.i(TAG, "getUserInfo success");
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void query(final Context context, final String filename, String username) {

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo(Constants.SP_USERNAME, username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user = list.get(0);
                    String username1 = user.getUsername();
                    String nickname = user.getNickname();
                    String email = user.getEmail();
                    String phone = user.getMobilePhoneNumber();
                    boolean ev = user.getEmailVerified();
                    boolean pv = user.getMobilePhoneNumberVerified();
                    String bio = user.getBio();
                    String birth = user.getBirth();
                    String sex = user.getUser_sex();
                    String avatar = user.getPicture();
                    Log.i("query", "" + username1);
                    Log.i("query", "" + nickname);
                    Log.i("query", "" + email);
                    Log.i("query", "" + phone);
                    Log.i("query", "" + ev);
                    Log.i("query", "" + pv);
                    Log.i("query", "" + bio);
                    Log.i("query", "" + sex);
                    Log.i("query", "" + birth);
                    Log.i("query", "" + avatar);
                    SPUtils.putString(context, filename, Constants.SP_USERNAME, username1);
                    SPUtils.putString(context, filename, Constants.SP_NICKNAME, nickname);
                    SPUtils.putString(context, filename, Constants.SP_BIO, bio);
                    SPUtils.putString(context, filename, Constants.SP_SEX, sex);
                    SPUtils.putString(context, filename, Constants.SP_BIRTH, birth);
                    SPUtils.putString(context, filename, Constants.SP_EMAIL, email);
                    SPUtils.putString(context, filename, Constants.SP_PHONE_NUMBER, phone);
                    SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_EMAIL, ev);
                    SPUtils.putBoolean(context, filename, Constants.SP_IS_VALID_PHONE_NUMBER, pv);
                    SPUtils.putString(context, filename, Constants.SP_HEAD_PICTURE, avatar);
                    setFlag(true);
                } else {
                    e.printStackTrace();
                    setFlag(false);
                }
            }
        });
    }
}
