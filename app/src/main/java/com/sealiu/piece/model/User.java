package com.sealiu.piece.model;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public class User extends BmobObject {
    private Integer id;
    //昵称
    private String name;
    //邮箱
    private String email;
    //手机号
    private Integer phone;
    //密码
    private String password;
    //第三方登录qq返回值
    private String qq;
    //第三方登录微信返回值
    private String wechat;
    //头像
    private String picture;
    //个人简介
    private String bio;
    //用户类型
    private Integer type;
    //注册时间
    private Date regTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}
