package com.sealiu.piece.service.Common;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.bean.BmobSmsState;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.QuerySMSStateListener;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by liuyang
 * on 2016/7/4.
 */

/**
 * 不再使用，先留着吧
 */
public class Sms {

    // 发送的验证码短信ID
    public Integer smsId;
    // 短信的错误码（默认为：0，没错）
    // 另外这个错误码，包括发送短信和验证验证码
    // 其他错误码：http://docs.bmob.cn/sms/Android/g_errorcode/doc/index.html
    public Integer errorCode = 0;

    // 查询短信发送状态和验证状态
    // index0：smsState（短信状态） :SUCCESS（发送成功）、FAIL（发送失败）、SENDING(发送中)。
    // index1：verifyState（验证状态）:true(已验证)、false(未验证)。
    public List<String> statusList;
    /**
     * 发送短信验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     * @return 发送短信的错误码，如果没有就是0
     */
    public Integer sendSMSCode(Context context, String phoneNumber) {

        // smscode 为Bmob后台中Piece应用的发送验证码的短信模板名称
        BmobSMS.requestSMSCode(context, phoneNumber, "smscode", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    smsId = integer;
                    errorCode = 0;
                    Log.i("bmob", "短信id：" + integer);//用于查询本次短信发送详情
                } else {
                    errorCode = e.getErrorCode();
                    Log.i("bmob", "errorCode = " + e.getErrorCode() + ",errorMsg = " + e.getLocalizedMessage());
                }
            }
        });

        return errorCode;
    }

    /**
     * 查询短信发送状态和验证状态
     * smsState（短信状态） :SUCCESS（发送成功）、FAIL（发送失败）、SENDING(发送中)。
     * verifyState（验证状态）:true(已验证)、false(未验证)。
     *
     * @param context 上下文
     */
    public List<String> querySMSState(Context context) {
        BmobSMS.querySmsState(context, smsId, new QuerySMSStateListener() {
            @Override
            public void done(BmobSmsState bmobSmsState, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "短信状态：" + bmobSmsState.getSmsState() + ",验证状态：" + bmobSmsState.getVerifyState());
                    statusList.add(bmobSmsState.getSmsState());
                    statusList.add(bmobSmsState.getVerifyState());
                }
            }

        });
        return statusList;
    }


    /**
     * 验证验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     * @param code        验证码
     * @return 发送短信的错误码，如果没有就是0
     */
    public Integer verifySMSCode(Context context, String phoneNumber, String code) {
        BmobSMS.verifySmsCode(context, phoneNumber, code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    errorCode = 0;
                    Log.i("bmob", "验证通过");
                } else {
                    errorCode = e.getErrorCode();
                    Log.i("bmob", "验证失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage());
                }
            }
        });
        return errorCode;
    }


}
