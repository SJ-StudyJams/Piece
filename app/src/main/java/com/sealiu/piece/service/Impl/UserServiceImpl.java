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
    public boolean login() {
        User bu = new User();
        bu.setName("sendi");
        bu.setPassword("123456");
        bu.setEmail("sendi@163.com");
        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    Log.e("User register","注册成功:" +s.toString());
                }else{
                    Log.e("User register",e.toString());
                }
            }
        });
        return false;
    }
}
