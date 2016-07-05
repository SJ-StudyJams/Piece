package com.sealiu.piece.controller.LoginRegister;

import android.content.Context;
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
import com.sealiu.piece.service.Common.Sms;

import static com.sealiu.piece.controller.LoginRegister.Constants.CODE_ERROR;
import static com.sealiu.piece.controller.LoginRegister.Constants.CREDIT_INFO_MUST_VERIFY_OK;
import static com.sealiu.piece.controller.LoginRegister.Constants.LOGIN_DATA_REQUIRED;
import static com.sealiu.piece.controller.LoginRegister.Constants.MOBILE_PHONE_NUMBER_ALREADY_TAKEN;
import static com.sealiu.piece.controller.LoginRegister.Constants.MOBILE_SEND_MESSAGE_LIMITED;
import static com.sealiu.piece.controller.LoginRegister.Constants.NO_REMAINING_NUMBER_FOR_SEND_MESSAGES;
import static com.sealiu.piece.controller.LoginRegister.Constants.SMS_CONTENT_ILLEGAL;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class RegisterOneFragment extends Fragment {
    private static final String TAG = "RegisterOneFragment";
    /*
        手机号正确 ---> flag = 1
        邮箱正确 ---> flag = 2
         */
    private int flag = 0;
    private String phoneNumber = "";

    // 实例化一个sms类，用以共享其中的错误码和短信Id成员变量
    private Sms sms = new Sms();

    public interface NextStepListener {
        void onNextBtnClick(int flag);

        void onFetchCodeBtnClick(int flag, String phoneOrEmail);
    }

    NextStepListener nextStepListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            nextStepListener = (NextStepListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NextStepListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_one, container, false);

        Button nextBtn = (Button) view.findViewById(R.id.reg_next_button);
        final EditText regPhoneOrEmailET = (EditText) view.findViewById(R.id.reg_phone_or_email);
        final EditText validatingCodeET = (EditText) view.findViewById(R.id.validating_code);
        final Button fetchCodeBtn = (Button) view.findViewById(R.id.fetch_code);
        final NextStepListener listener = (NextStepListener) getActivity();

        //---------------------------------------------------------------------------------
        //点击获取验证码后，检验手机号/邮箱地址是否合法，合法则发送验证码/激活邮件
        fetchCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneOrEmail = regPhoneOrEmailET.getText().toString();

                //检测 regPhoneOrEmailET 是否为空
                if (phoneOrEmail.equals("")) {
                    Snackbar.make(view, "注册邮箱/手机号不能为空", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                //检测 手机号/邮箱地址格式是否合法
                String numberPattern = "[0-9\\+]+";
                String phonePattern = "(\\+\\d+)?1[3458]\\d{9}$";
                String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

                if (phoneOrEmail.matches(numberPattern)) {
                    if (!phoneOrEmail.matches(phonePattern)) {
                        Snackbar.make(view, "手机号格式错误", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    } else {
                        flag = 1;
                    }
                } else {
                    if (!phoneOrEmail.matches(emailPattern)) {
                        Snackbar.make(view, "邮箱格式错误", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    } else {
                        flag = 2;
                    }
                }

                switch (flag) {
                    case 1:
                        // 发送短信验证码
                        phoneNumber = phoneOrEmail.substring(phoneOrEmail.length() - 11, phoneOrEmail.length());
                        Integer errorCode = sms.sendSMSCode(getActivity(), phoneNumber);
                        //确认发送成功，再改变按钮状态
                        if (errorCode != 0) {
                            Snackbar.make(view, createErrorInfo(errorCode), Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            return;
                        } else {
                            Snackbar.make(view, "短信发送成功", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            regPhoneOrEmailET.setFocusable(false);
                            regPhoneOrEmailET.setClickable(false);
                            listener.onFetchCodeBtnClick(flag, phoneOrEmail);
                            break;
                        }
                    case 2:
                        // 发送邮箱验证码(后台自动发送)，不需要立即验证
                        validatingCodeET.setText("验证邮件已发送，注册后请及时验证邮箱！");
                        validatingCodeET.setFocusable(false);
                        validatingCodeET.setClickable(false);

                        // 避免发送验证码/激活邮件之后,修改手机号/邮箱. 虽然不会有影响.
                        regPhoneOrEmailET.setFocusable(false);
                        regPhoneOrEmailET.setClickable(false);
                        listener.onFetchCodeBtnClick(flag, phoneOrEmail);
                        break;
                    default:
                        Snackbar.make(view, "验证出现问题", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                }

                fetchCodeBtn.setText("已发送");
                fetchCodeBtn.setClickable(false);
            }
        });

        //---------------------------------------------------------------------
        // 验证完成手机号/邮箱，下一步
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (flag == 0) {
                    Snackbar.make(view, "请先验证邮箱/手机号", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                // 手机号注册，验证码不能为空，且要通过验证
                if (flag == 1) {

                    String code = validatingCodeET.getText().toString();

                    if (code.equals("")) {
                        Snackbar.make(view, "验证码不能为空", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    } else if (!phoneNumber.equals("")) {

                        Integer errorCode = sms.verifySMSCode(getActivity(), phoneNumber, code);

                        if (errorCode != 0) {
                            validatingCodeET.setText("");
                            Snackbar.make(view, createErrorInfo(errorCode), Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            return;
                        }
                    }// end-if 验证码为空验证
                }// end-if 手机号注册验证

                // 邮箱注册，放行！
                listener.onNextBtnClick(flag);
            }
        });
        return view;
    }

    public String createErrorInfo(Integer errorCode) {
        String errorInfo;
        switch (errorCode) {
            case LOGIN_DATA_REQUIRED:
                errorInfo = "缺少登录信息";
                break;
            case CODE_ERROR: //验证码输入错误，code error.
                errorInfo = "验证码不正确";
                break;
            case MOBILE_PHONE_NUMBER_ALREADY_TAKEN:
                errorInfo = "手机号码已经存在";
                break;
            case MOBILE_SEND_MESSAGE_LIMITED:
                errorInfo = "该手机号发送短信达到限制";
                break;
            case NO_REMAINING_NUMBER_FOR_SEND_MESSAGES:
                errorInfo = "开发者账户无可用的发送短信条数";
                break;
            case CREDIT_INFO_MUST_VERIFY_OK:
                errorInfo = "身份信息必须审核通过才能使用该功能";
                break;
            case SMS_CONTENT_ILLEGAL:
                errorInfo = "非法短信内容";
                break;
            default:
                errorInfo = "未知错误,请联系开发者";
                break;
        }
        return errorInfo;
    }
}
