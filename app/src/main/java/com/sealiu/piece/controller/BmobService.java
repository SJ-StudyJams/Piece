package com.sealiu.piece.controller;

import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by art2cat
 * on 16-7-21.
 */
public class BmobService {
    private User user;
    private LoginUser loginUser;

    public BmobService(LoginUser loginUser) {
        super();
        user = new User();
        this.loginUser = loginUser;
    }

    public LoginUser login(String username, final String pwd) {
        user.setUsername(username);
        user.setPassword(pwd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //记录本次登录时间，设置登录标志位
                    loginUser.setLoginTime(System.currentTimeMillis());
                    loginUser.setAutoLogin(true);
                    loginUser.setObjectId(user.getObjectId());
                    loginUser.setUsername(user.getUsername());
                    loginUser.setPassword(pwd);
                    loginUser.setLogin(true);
                } else {
                    loginUser.setLogin(false);
                    loginUser.setErrorMsg(e.getErrorCode());
                }
            }
        });

        return loginUser;
    }
}
