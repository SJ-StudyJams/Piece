package com.sealiu.piece.controller.User;

import android.content.Context;
import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.util.EmptyStackException;
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
    private static final String TAG = "UserInfoSync";

    /**
     * 上传用户更改后用户信息
     *
     * @param context  上下文对象
     * @param objectId user 的objectId （bmob后台生成）
     * @param filename SP 文件名
     */
    public static void upload(final Context context, String objectId, final String filename) throws Exception {
        String nickname, bio, sex, birth, email, phone_number, picture;
        nickname = SPUtils.getString(context, filename, Constants.SP_NICKNAME, null);
        bio = SPUtils.getString(context, filename, Constants.SP_BIO, null);
        sex = SPUtils.getString(context, filename, Constants.SP_SEX, null);
        birth = SPUtils.getString(context, filename, Constants.SP_BIRTH, null);
        email = SPUtils.getString(context, filename, Constants.SP_EMAIL, null);
        phone_number = SPUtils.getString(context, filename, Constants.SP_PHONE_NUMBER, null);
        picture = SPUtils.getString(context, filename, Constants.SP_HEAD_PICTURE, null);

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
     * @param username 需要查询用户的用户名
     */
    public static void getUserInfo(final Context context, final String filename, String username) throws Exception {

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
                    boolean ev = user.getEmailVerified() == null ? false : user.getEmailVerified();
                    boolean pv = user.getMobilePhoneNumberVerified() == null ? false : user.getMobilePhoneNumberVerified();
                    String bio = user.getBio();
                    String birth = user.getBirth();
                    String sex = user.getUser_sex();
                    String avatar = user.getPicture();
                    Log.i("query", null + username1);
                    Log.i("query", null + nickname);
                    Log.i("query", null + email);
                    Log.i("query", null + phone);
                    Log.i("query", "" + ev);
                    Log.i("query", "" + pv);
                    Log.i("query", null + bio);
                    Log.i("query", null + sex);
                    Log.i("query", null + birth);
                    Log.i("query", null + avatar);
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

                } else {
                    e.printStackTrace();
                }
            }
        });

        Log.i(TAG, "getUserInfo success");
    }

    public static void saveLoginInfo(Context context, LoginUser loginInfo){
        try {
            if (loginInfo.getObjectId() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, loginInfo.getObjectId());
            }
            if (loginInfo.getUsername() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_USERNAME, loginInfo.getUsername());
            }
            if (loginInfo.getPassword() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, loginInfo.getPassword());
            }
            if (loginInfo.getNickname() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, loginInfo.getNickname());
            }
            if (loginInfo.getBio() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIO, loginInfo.getBio());
            }
            if (loginInfo.getBirth() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_BIRTH, loginInfo.getBirth());
            }
            if (loginInfo.getEmail() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL, loginInfo.getEmail());
            }
            if (loginInfo.getMobilePhone() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, loginInfo.getMobilePhone());
            }
            if (loginInfo.getAvatar() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_HEAD_PICTURE, loginInfo.getAvatar());
            }

            if (loginInfo.getSex() != null) {
                SPUtils.putString(context, Constants.SP_FILE_NAME, Constants.SP_SEX, loginInfo.getSex());
            }

            SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_EMAIL, loginInfo.isEmailVerified() );

            SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_PHONE_NUMBER, loginInfo.isMobilePhoneNumberVerified() );

            SPUtils.putBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, loginInfo.isAutoLogin() );

            if (loginInfo.getLoginTime() != 0) {
                SPUtils.putLong(context, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, loginInfo.getLoginTime());
            }
            Log.i(TAG, "saved");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static LoginUser getLoginInfo(Context context){
        LoginUser loginInfo = new LoginUser();

        loginInfo.setObjectId(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null));
        loginInfo.setUsername(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_USERNAME, null));
        loginInfo.setPassword(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null));
        loginInfo.setNickname(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, null));
        loginInfo.setBio(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_BIO, null));
        loginInfo.setBirth(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_BIRTH, null));
        loginInfo.setEmail(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_EMAIL, null));
        loginInfo.setMobilePhone(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, null));
        loginInfo.setSex(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_SEX, null));
        loginInfo.setAvatar(SPUtils.getString(context, Constants.SP_FILE_NAME, Constants.SP_HEAD_PICTURE, null));
        loginInfo.setEmailVerified(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_EMAIL, false));
        loginInfo.setMobilePhoneNumberVerified(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_VALID_PHONE_NUMBER, false));
        loginInfo.setAutoLogin(SPUtils.getBoolean(context, Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, false));
        loginInfo.setLoginTime(SPUtils.getLong(context, Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, 0));
        Log.i(TAG, "get LoginUser info");
        return loginInfo;
    }
}
