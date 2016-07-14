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
    private static final String TAG = "UserInfoSync";

    /**
     * 上传用户更改后用户信息
     *
     * @param context  上下文对象
     * @param objectId user 的objectId （bmob后台生成）
     * @param filename SP 文件名
     */
    public void upload(final Context context, String objectId, final String filename) throws Exception {
        String nickname, bio, sex, birth, email, phone_number, picture;
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
     * @param username 需要查询用户的用户名
     */
    public void getUserInfo(final Context context, final String filename, String username) throws Exception {

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

                } else {
                    e.printStackTrace();
                }
            }
        });

        Log.i(TAG, "getUserInfo success");
    }

}
