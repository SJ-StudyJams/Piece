package com.studyjams.piece.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
@IgnoreExtraProperties
public class User {

    //昵称
    public String username;
    //邮箱
    public String email;
    //头像
    public String photo;
    //用户类型(google, facebook, anonymous)
    public String type;

    public User() {
    }

    public User(String username, String email, String photo, String type) {
        this.username = username;
        this.email = email;
        if (photo != null)
            this.photo = photo;
        this.type = type;
    }
}
