package com.sealiu.piece.service.Common;

import android.content.Context;
import android.util.Log;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by liuyang
 * on 2016/7/4.
 */
public class Sms {
    /**
     * 发送短信验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     */
    public static void sendSMSCode(Context context, String phoneNumber) {

        // smscode 为Bmob后台中Piece应用的发送验证码的短信模板名称
        BmobSMS.requestSMSCode(context, phoneNumber, "smscode", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    Log.i("bmob", "短信id：" + integer);//用于查询本次短信发送详情
                }
            }
        });
    }

    /**
     * 验证验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     * @param code        验证码
     */
    public static void verifySMSCode(Context context, String phoneNumber, String code) {
        BmobSMS.verifySmsCode(context, phoneNumber, code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    Log.i("bmob", "验证通过");
                } else {
                    Log.i("bmob", "验证失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage());
                }
            }
        });
    }

}
