package com.sealiu.piece.controller.LoginRegister;

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
}
