package com.sealiu.piece.model;

/**
 * Created by art2cat on 7/4/2016.
 */
public class Constants {
    /**
     *  此为Bmob的APP_ID
     */
    public static final String BMOB_APP_ID = "cc31d2e7036aa868bb7ed4401c1354fe";

    /**
     * 此为腾讯官方提供给开发者用于测试的APP_ID，个人开发者需要去QQ互联官网为自己的应用申请对应的AppId
     */
    public static final String QQ_APP_ID ="1105440739";

    /**
     * 根据错误码获取错误信息
     *
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static String createErrorInfo(Integer errorCode) {
        String errorInfo;
        switch (errorCode) {
            case 109:
                errorInfo = "缺少登录信息";
                break;
            case 207:
                errorInfo = "验证码不正确";
                break;
            case 209:
                errorInfo = "手机号码已经存在";
                break;
            case 202:
                errorInfo = "邮箱已经存在";
                break;
            case 10010:
                errorInfo = "该手机号发送短信达到限制";
                break;
            case 10011:
                errorInfo = "开发者账户无可用的发送短信条数";
                break;
            case 10012:
                errorInfo = "身份信息必须审核通过才能使用该功能";
                break;
            case 10013:
                errorInfo = "非法短信内容";
                break;
            default:
                errorInfo = "未知错误,请联系开发者";
                break;
        }
        return errorInfo;
    }

    /**
     *  User本地信息常量
     */

    // User信息本体保存文件
    public static final String SP_FILE_NAME = "userInfo";
    //用户名
    public static final String SP_USERNAME = "username";
    //密码（未加密）
    public static final String SP_PASSWORD = "password";
    //记住密码
    public static final String SP_IS_REMEMBER = "isRemember";
    //生日
    public static final String SP_BIRTH = "birth";
    //性别
    public static final String SP_SEX = "user_sex";
    //昵称
    public static final String SP_NICKNAME = "nickname";
    //个人简介
    public static final String SP_BIO = "bio";
    //登陆与否
    public static final String SP_IS_LOGIN = "isLogin";
    //自动登录
    public static final String SP_IS_AUTO_LOGIN = "isAutoLogin";
    //_user objectId
    public static final String SP_USER_OBJECT_ID = "userObjectId";
    //Email
    public static final String SP_EMAIL = "email";
    //Email 是否验证
    public static final String SP_IS_VALID_EMAIL = "isValidEmail";
    //手机号
    public static final String SP_PHONE_NUMBER = "mobilePhoneNumber";
    //手机号 是否验证
    public static final String SP_IS_VALID_PHONE_NUMBER = "isValidMobilePhoneNumber";
    //登录时间
    public static final String SP_LOGIN_TIME = "time";

    /**
     * RSA密钥内容 base64 code
     */

    //rsa_public_key
    public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFGMdDEV5C6AEwAGWD5olbyDhc\n" +
            "IQZ4xTwMNEBJi5kBRWYaiBMzzs2sTTVzxCPE20eAAxt8I6zXqa6rQzl78g/m6n+e\n" +
            "GRI/LlLenNgZatoj7GQGgnIPKx2HEQy22BzXaWs2AOBLX4US79hTpWY02eXKCXBd\n" +
            "ofyEm2LH2uWPPoQqKwIDAQAB";

    //rsa_private_key
    public static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMUYx0MRXkLoATAA" +
            "ZYPmiVvIOFwhBnjFPAw0QEmLmQFFZhqIEzPOzaxNNXPEI8TbR4ADG3wjrNeprqtD" +
            "OXvyD+bqf54ZEj8uUt6c2Blq2iPsZAaCcg8rHYcRDLbYHNdpazYA4EtfhRLv2FOl" +
            "ZjTZ5coJcF2h/ISbYsfa5Y8+hCorAgMBAAECgYEAkU8kUTjz0sXzYi1YqncDm8ww" +
            "aRPcDpvzGlhHcTFfO4WrsjHSXmbAUaCWoOPhLxgvTZx83ff7TQCHizJDpjKfL2ZD" +
            "WsxAJtwd37OByiI6kUNlMykOxNZycUGJ9vaXr57OEH1DDNkMkX7H9E4lb5z6bOwC" +
            "fuDM32zh/niQtSQP65kCQQDzCB0tlx1Hh14bhk2UBJM2tux/5P4NacxITHUzWHbK" +
            "rhR8hT23PLeUi72nP1NJ9GpIoVCws/J10ICKI8q+WKfPAkEAz50uBinog4bPV6wE" +
            "sLg0XXTR1ac8ZaQ3uBzwCnh7m3M/0QZu1W7TQvYkm5PdLaiN3MFdk4/7EjWDaV3j" +
            "3N6S5QJAXS+qSHXd8zRTgEhR7MSIUf13115Nj4UWoE44zjRIcFSpZEmOrXjph1rB" +
            "oKRmYkAGlMzN7MNC36vP7aflsHC7/wJAVc1i8P8u7fSwCk64XYSzd5BJDGCiUGtu" +
            "77Nd7SXgB924mR1sft7fhsQNWxLgDPelMX/kuZB+tgbRuaEpA+YklQJBAJh0CDaQ" +
            "qA4fcaG0b4KKhYROOcNn1L5XPIxiDZ6QjKwyOsQX6agu0pIetIUobOT7M/HgRTFC" +
            "pO6j8TaXggxr2mA=";

    /**
     * 此为AESKey（可修改）
     */
    public static final String AES_KEY = "0123456789abcdef";

    /**
     * AES偏移量 （可修改）
     */
    public static final String IV_PARAMERER = "1020304050607080";
}
