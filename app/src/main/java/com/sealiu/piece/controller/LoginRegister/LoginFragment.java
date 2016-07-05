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
    private EditText mEt_pasword;

    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化数据
        inintData();

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button thirdPartLoginBtn = (Button) view.findViewById(R.id.third_part_login_btn);
        Button backRegisterBtn = (Button) view.findViewById(R.id.back_register_button);
        Button submitLoginBtn = (Button) view.findViewById(R.id.submit_login_btn);

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
                mEt_user = (EditText) view.findViewById(R.id.login_phone_or_email);
                mEt_pasword = (EditText) view.findViewById(R.id.password);
                //保存用户名
                SPUtils.putString(getContext(), MyConstaints.NAME, mEt_user.getText().toString().trim());
                //保存加密后的密码
                if(TextUtils.isEmpty(mEt_user.getText())||TextUtils.isEmpty(mEt_pasword.getText())){
                    Toast.makeText(getContext(),"用户名或者密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                SPUtils.putString(getContext(),MyConstaints.PASSWORD, Md5Utils.encode(mEt_pasword.getText().toString().trim()));
                Listener listener = (Listener) getActivity();
                listener.onSubmitLoginBtnClick();
            }
        });

        return view;
    }

    private void inintData() {
        //获取用户名
        mEt_user.setText(SPUtils.getString(getContext(),MyConstaints.NAME,""));
    }
}
