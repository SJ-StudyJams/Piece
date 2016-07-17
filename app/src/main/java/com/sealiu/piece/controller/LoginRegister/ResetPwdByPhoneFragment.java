package com.sealiu.piece.controller.LoginRegister;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sealiu.piece.R;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat
 * on 16-7-16.
 */
public class ResetPwdByPhoneFragment extends Fragment implements View.OnClickListener{
    private EditText phoneET, smsCodeET, newPwdET;
    private Button send, reset;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd_phone, null);
        phoneET = (EditText) view.findViewById(R.id.resetPwd_Phone_ET);
        smsCodeET = (EditText) view.findViewById(R.id.resetPwd_sms_ET);
        newPwdET = (EditText) view.findViewById(R.id.resetPwd_pwd_ET);

        send = (Button) view.findViewById(R.id.send_v);
        reset = (Button) view.findViewById(R.id.resetPwd_phone_Btn);

        send.setOnClickListener(this);
        reset.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.send_v:
                String phone = phoneET.getText().toString();
                BmobSMS.requestSMSCode(getContext(), phone, "smscode", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "验证码发送成功", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(view, "验证码发送失败" + e, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.resetPwd_phone_Btn:
                String password = newPwdET.getText().toString();
                String smsCode = smsCodeET.getText().toString();
                BmobUser.resetPasswordBySMSCode(smsCode, password, new UpdateListener() {
                    @Override
                    public void done(cn.bmob.v3.exception.BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "密码重置成功", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(view, "重置失败：code = " + e.getErrorCode() + ",msg = "
                                    + e.getLocalizedMessage() + e, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                phoneET.setText("");
                smsCodeET.setText("");
                newPwdET.setText("");
                break;
            default:
                break;
        }
    }
}
