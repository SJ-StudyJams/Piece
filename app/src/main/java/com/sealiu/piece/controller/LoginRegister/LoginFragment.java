package com.sealiu.piece.controller.LoginRegister;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class LoginFragment extends Fragment {

    private View view;
    private EditText et_account,et_pwd;
    private User user = new User();
    private CheckBox cb_RememberPwd;
    private boolean isRememberPwd;
    private String fileName = "loginInfo";
    private String username, pwd, password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TAG = "LoginFragment";


    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        et_account = (EditText) view.findViewById(R.id.login_phone_or_email);
        et_pwd = (EditText) view.findViewById(R.id.login_password);
        cb_RememberPwd = (CheckBox) view.findViewById(R.id.is_remember_password);
        editor = sharedPreferences.edit();
        isRememberPwd = sharedPreferences.getBoolean("remember_password", false);
        if (isRememberPwd) {
            username = sharedPreferences.getString("userName", null);
            password = sharedPreferences.getString("password", null);
            et_account.setText(username);
            et_pwd.setText(password);
            cb_RememberPwd.setChecked(true);
        }

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
            public void onClick(final View view) {
                username = et_account.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if(username.equals("")){
                    Snackbar.make(view, "请输入注册邮箱或手机号", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                } else if(pwd.equals("")){
                    Snackbar.make(view, "请输入密码", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }
                user.setUsername(username);
                password = Md5Utils.encode(pwd);
                user.setPassword(password);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e == null){
                            Listener listener = (Listener) getActivity();
                            listener.onSubmitLoginBtnClick();
                            if (cb_RememberPwd.isChecked()) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("userName", username);              //用户名
                                editor.putString("password", pwd);            //密码
                                SPUtils.putBoolean(getActivity(),"isLogin",true);
                                Log.i(TAG, "方法一");
                            } else {
                                editor.clear();
                            }
                        } else {
                            editor.clear();
                            Snackbar.make(view, "用户名或密码错误", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Log.i(TAG, e.toString());
                        }
                        editor.apply();
                    }
                });
            }
        });

        return view;
    }
}
