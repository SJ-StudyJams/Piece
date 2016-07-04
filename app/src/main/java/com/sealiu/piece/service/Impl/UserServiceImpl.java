package com.sealiu.piece.service.Impl;


import android.util.Log;

import com.sealiu.piece.model.User;
import com.sealiu.piece.service.UserService;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public class UserServiceImpl implements UserService {
    @Override
    public String signUp(final User user) {

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Log.i("UserServiceImpl", "注册成功:" + user.toString());
                } else {
                    Log.i("UserServiceImpl", e.toString());
                }
            }
        });
        return user.getObjectId();
    }

    @Override
    public String login(User user) {

        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    Log.i("UserServiceImpl","用户登陆成功");
                } else {
                    Log.i("UserServiceImpl", e.toString());
                }
            }
        });
        return user.getObjectId();
    }
}
