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
public class RegisterTwoFragment extends Fragment {

    public interface CompleteRegisterListener {
        void onCompleteRegisterBtnClick(String conformedPwd);
    }

    CompleteRegisterListener completeRegisterListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            completeRegisterListener = (CompleteRegisterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CompleteRegisterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_two, container, false);

        final EditText pwdET = (EditText) view.findViewById(R.id.reg_password);
        final EditText repeatPwdET = (EditText) view.findViewById(R.id.repeat_pwd);

        Button completeRegisterBtn = (Button) view.findViewById(R.id.reg_complete_button);
        completeRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteRegisterListener listener = (CompleteRegisterListener) getActivity();

                String pwd = pwdET.getText().toString();
                String repeatPwd = repeatPwdET.getText().toString();

                if (pwd.equals("")) {
                    Snackbar.make(v, "密码不能为空", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                if (repeatPwd.equals("")) {
                    Snackbar.make(v, "第二次输入不能为空", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                if (!pwd.equals(repeatPwd)) {
                    Snackbar.make(v, "两次输入不一致", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                listener.onCompleteRegisterBtnClick(repeatPwd);
            }
        });

        return view;
    }
}
