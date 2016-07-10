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
     * SMS 错误码
     */

    // 登录信息是必需的，如邮箱和密码时缺少其中一个提示此信息
    public static final int LOGIN_DATA_REQUIRED = 109;
    // 验证码错误
    public static final int CODE_ERROR = 207;
    // 该手机号码已经存在
    public static final int MOBILE_PHONE_NUMBER_ALREADY_TAKEN = 209;
    // 该手机号发送短信达到限制(对于一个应用来说，一天给同一手机号发送短信不能超过10条，
    // 一小时给同一手机号发送短信不能超过5条，一分钟给同一手机号发送短信不能超过1条)
    public static final int MOBILE_SEND_MESSAGE_LIMITED = 10010;
    // 该账户无可用的发送短信条数
    public static final int NO_REMAINING_NUMBER_FOR_SEND_MESSAGES = 10011;
    // 身份信息必须审核通过才能使用该功能
    public static final int CREDIT_INFO_MUST_VERIFY_OK = 10012;
    // 非法短信内容
    public static final int SMS_CONTENT_ILLEGAL = 10013;

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
    //自动登录
    public static final String SP_IS_AUTO_LOGIN = "isAutoLogin";
    //_user objectId
    public static final String SP_USER_OBJECT_ID = "userObjectId";
    //Email
    public static final String SP_EMAIL = "email";
    //手机号
    public static final String SP_PHONE_NUMBER = "mobilePhoneNumber";
    //登录时间
    public static final String SP_LOGIN_TIME = "time";
    //邮箱是否验证
    public static final String SP_EMAIL_VERIFIED = "emailVerified";
    //手机号是否验证
    public static final String SP_PHONE_NUMBER_VERIFIED = "mobilePhoneNumberVerified";
    //头像
    public static final String SP_USER_AVATAR = "userAvatar";

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
