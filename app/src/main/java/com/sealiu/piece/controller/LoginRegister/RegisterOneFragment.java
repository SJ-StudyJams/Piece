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

        //点击获取验证码后，检验手机号/邮箱地址是否合法，合法则发送验证码
        fetchCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneOrEmail = regPhoneOrEmailET.getText().toString();

                //检测 regPhoneOrEmailET 是否为空
                if (phoneOrEmail.equals("")) {
                    Snackbar.make(view, "regPhoneOrEmail is null", Snackbar.LENGTH_SHORT)
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
                        Sms.sendSMSCode(getActivity(), phoneNumber);
                        break;
                    case 2:
                        // 发送邮箱验证码(后台自动发送)，不需要立即验证
                        validatingCodeET.setText("验证邮件已发送，注册后请及时验证邮箱！");
                        validatingCodeET.setFocusable(false);
                        validatingCodeET.setClickable(false);
                        break;
                    default:
                        Snackbar.make(view, "验证出现问题", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                }

                fetchCodeBtn.setText("已发送");
                fetchCodeBtn.setClickable(false);
                listener.onFetchCodeBtnClick(flag, phoneOrEmail);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        if (!Sms.verifySMSCode(getActivity(), phoneNumber, code)) {
                            validatingCodeET.setText("");
                            Snackbar.make(view, "验证码不正确", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            return;
                        }
                    }
                }

                // 邮箱注册，放行！
                listener.onNextBtnClick(flag);
            }
        });
        return view;
    }
}
