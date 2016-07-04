package com.sealiu.piece.service;

import com.sealiu.piece.model.User;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public interface UserService {
    // 注册
    String signUp(User user);

    //登陆
    String login(User user);
}
