package com.sealiu.piece.controller.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.PieceAPP;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class LoginFragment extends Fragment {

    private EditText et_account,et_pwd;
    private User user = new User();

    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        et_account = (EditText)view.findViewById(R.id.login_phone_or_email);
        et_pwd = (EditText)view.findViewById(R.id.login_password);

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
                String account,pwd;
                account = et_account.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if(account.equals("")){
                    toast("请输入注册手机号或邮箱");
                    return;
                }

                if(pwd.equals("")){
                    toast("请输入你的密码");
                    return;
                }
                user.setUsername(account);
                String password = Md5Utils.encode(pwd);
                user.setPassword(password);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e == null){
                            Listener listener = (Listener) getActivity();
                            listener.onSubmitLoginBtnClick();
                        } else {
                            Log.i("UserServiceImpl", e.toString());
                        }
                    }
                });
            }
        });

        return view;
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
