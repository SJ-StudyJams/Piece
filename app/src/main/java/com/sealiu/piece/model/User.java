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
}
