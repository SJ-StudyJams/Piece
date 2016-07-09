package com.sealiu.piece.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public class User extends BmobUser {

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
    //登陆时间
    private long loginTime;
    //性别
    private String user_sex;
    //密码
    private String pwd;
    //生日
    private String birth;
    //昵称
    private String nickname;

    @Override
    public String getObjectId() {
        return super.getObjectId();
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

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getUser_sex() {
        return user_sex;
    }
    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
