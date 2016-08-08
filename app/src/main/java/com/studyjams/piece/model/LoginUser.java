package com.studyjams.piece.model;

/**
 * Created by art2cat
 * on 16-7-16.
 */
public class LoginUser {
    private String objectId;
    private String username;
    private String password;
    private String nickname;
    private String bio;
    private String birth;
    private String email;
    private String mobilePhone;
    private String sex;
    private String avatar;
    private int errorMsg;
    private boolean login;
    private boolean emailVerified;
    private boolean mobilePhoneNumberVerified;
    private boolean autoLogin;
    private long loginTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobilePhoneNumberVerified() {
        return mobilePhoneNumberVerified;
    }

    public void setMobilePhoneNumberVerified(boolean mobilePhoneNumberVerified) {
        this.mobilePhoneNumberVerified = mobilePhoneNumberVerified;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public int getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(int errorMsg) {
        this.errorMsg = errorMsg;
    }
}
