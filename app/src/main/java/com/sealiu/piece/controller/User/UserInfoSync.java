package com.sealiu.piece.controller.User;

import android.content.Context;

import android.util.Log;

import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.AESUtils;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat on 7/6/2016.
 */
public class UserInfoSync {
    private String nickname, password, bio, sex, birth, email, phone_number;
    private String nickname1, bio1, sex1, birth1, email1, phone_number1;
    private String nicknameE, bioE, sexE, birthE, emailE, phone_numberE;
    private String nicknameD, bioD, sexD, birthD, emailD, phone_numberD;
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

        encodeNickname();
        encodeBio();
        encodeSex();
        encodeBirth();
        encodeEmail();
        encodePhoneNumber();

        user.setNickname(nicknameE);
        user.setBio(bioE);
        user.setBirth(birthE);
        user.setUser_sex(sexE);
        user.setEmail(emailE);
        user.setMobilePhoneNumber(phone_numberE);
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
     * @param filename
     */
    public void getUserInfo(Context context, String filename) throws Exception {
        nickname1 = (String) User.getObjectByKey(Constants.SP_NICKNAME);
        bio1 = (String) User.getObjectByKey(Constants.SP_BIO);
        sex1 = (String) User.getObjectByKey(Constants.SP_SEX);
        birth1 = (String) User.getObjectByKey(Constants.SP_BIRTH);
        email1 = (String) User.getObjectByKey(Constants.SP_EMAIL);
        phone_number1 = (String) User.getObjectByKey(Constants.SP_PHONE_NUMBER);

        decodeNickname();
        decodeBio();
        decodeSex();
        decodeBirth();
        decodeEmail();
        decodePhoneNumber();

        SPUtils.putString(context, filename, Constants.SP_NICKNAME, nicknameD);
        SPUtils.putString(context, filename, Constants.SP_BIO, bioD);
        SPUtils.putString(context, filename, Constants.SP_SEX, sexD);
        SPUtils.putString(context, filename, Constants.SP_BIRTH, birthD);
        SPUtils.putString(context, filename, Constants.SP_EMAIL, emailD);
        SPUtils.putString(context, filename, Constants.SP_PHONE_NUMBER, phone_numberD);
    }

    private void encodeNickname() throws Exception {
        if (nickname != null) {
            nicknameE = AESUtils.encrypt(nickname);
            Log.i(TAG, "encrypt nickname done");
        }
    }

    private void decodeNickname() throws Exception {

        if (nickname1 != null) {
            nicknameD = AESUtils.decrypt(nickname1);
            Log.i(TAG, "decrypt nickname done");
        }
    }

    private void encodeBio() throws Exception {
        if (bio != null) {
            bioE = AESUtils.encrypt(bio);
            Log.i(TAG, "encrypt bio done");
        }
    }

    private void decodeBio() throws Exception {
        if (bio1 != null) {
            bioD = AESUtils.decrypt(bio1);
            Log.i(TAG, "decrypt bio done");
        }

    }

    private void encodeBirth() throws Exception {
        if (birth != null) {
            birthE = AESUtils.encrypt(birth);
            Log.i(TAG, "encrypt birth done");
        }

    }

    private void decodeBirth() throws Exception {
        if (birth1 != null) {
            birthD = AESUtils.decrypt(birth1);
            Log.i(TAG, "decrypt birth done");
        }

    }

    private void encodeSex() throws Exception {
        if (sex != null) {
            sexE = AESUtils.encrypt(sex);
            Log.i(TAG, "encrypt sex done");
        }
    }

    private void decodeSex() throws Exception {
        if (sex1 != null) {
            sexD = AESUtils.decrypt(sex1);
            Log.i(TAG, "decrypt sex done");
        }
    }

    private void encodeEmail() throws Exception {
        if (email != null) {
            emailE = AESUtils.encrypt(email);
            Log.i(TAG, "encrypt email done");
        }
    }

    private void decodeEmail() throws Exception {
        if (email1 != null) {
            emailD = AESUtils.decrypt(email1);
            Log.i(TAG, "decrypt email done");
        }
    }

    private void encodePhoneNumber() throws Exception {
        if (phone_number != null) {
            phone_numberE = AESUtils.encrypt(phone_number);
            Log.i(TAG, "encrypt phone number done");
        }
    }

    private void decodePhoneNumber() throws Exception {
        if (phone_number1 != null) {
            phone_numberD = AESUtils.decrypt(phone_number1);
            Log.i(TAG, "decrypt phone number done");
        }
    }

}
