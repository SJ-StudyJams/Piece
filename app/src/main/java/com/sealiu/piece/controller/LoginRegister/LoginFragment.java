package com.sealiu.piece.controller.LoginRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.MyConstaints;
import com.sealiu.piece.utils.SPUtils;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class LoginFragment extends Fragment {

    private EditText mEt_user;
    private EditText mEt_password;
    private View mView;

    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_login, container, false);
        //初始化视图
        initView();
        //初始化数据
        inintData();

        Button thirdPartLoginBtn = (Button) mView.findViewById(R.id.third_part_login_btn);
        Button backRegisterBtn = (Button) mView.findViewById(R.id.back_register_button);
        Button submitLoginBtn = (Button) mView.findViewById(R.id.submit_login_btn);

        thirdPartLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener listener = (Listener) getActivity();
                listener.onThirdPartLoginBtnClick();
            }
        });

        backRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener listener = (Listener) getActivity();
                listener.onBackRegisterBtnClick();
            }
        });


        submitLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断是否为空
                String user_name = mEt_user.getText().toString().trim();
                String user_password = mEt_password.getText().toString().trim();

                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_password)) {
                    Toast.makeText(getContext(), "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存用户名
                SPUtils.putString(getContext(), MyConstaints.NAME, mEt_user.getText().toString().trim());
                //保存加密后的密码
                //18658710313
                //123456
                SPUtils.putString(getContext(), MyConstaints.PASSWORD, Md5Utils.encode(mEt_password.getText().toString().trim()));
                Listener listener = (Listener) getActivity();
                listener.onSubmitLoginBtnClick();
            }
        });

        return mView;
    }

    private void initView() {
        mEt_user = (EditText) mView.findViewById(R.id.login_phone_or_email);
        mEt_password = (EditText) mView.findViewById(R.id.password);
    }

    private void inintData() {
        //获取本地用户名
          mEt_user.setText(SPUtils.getString(getContext(), MyConstaints.NAME, ""));


    }
}
