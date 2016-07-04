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

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class RegisterOneFragment extends Fragment {

    public interface NextStepListener {
        void onNextBtnClick();

        void onFetchCodeBtnClick();
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
        final Button fetchCodeBtn = (Button) view.findViewById(R.id.fetch_code);
        final NextStepListener listener = (NextStepListener) getActivity();

        //点击获取验证码后，检验手机号/邮箱地址是否合法，合法则发送验证码
        fetchCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检测 regPhoneOrEmailET 是否为空
                if (regPhoneOrEmailET.getText().toString().equals("")) {
                    Snackbar.make(view, "regPhoneOrEmail is null", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                //检测 手机号/邮箱地址格式是否合法

                fetchCodeBtn.setText("已发送");
                listener.onFetchCodeBtnClick();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onNextBtnClick();
            }
        });
        return view;
    }
}
