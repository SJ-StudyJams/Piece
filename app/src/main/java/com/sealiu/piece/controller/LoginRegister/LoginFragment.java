package com.sealiu.piece.controller.LoginRegister;


import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
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
    private EditText et_account, et_pwd;
    private User user = new User();
    private CheckBox cb_RememberPwd;
    private boolean isRememberPwd;

    private CheckBox cb_AutoLogin;
    private String username, pwd, password;
    private static final String TAG = "LoginFragment";


    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        et_account = (EditText) view.findViewById(R.id.login_phone_or_email);
        et_pwd = (EditText) view.findViewById(R.id.login_password);

        /**
         * 记住密码和自动登录的关系：
         * 取消记住密码 --> 自动取消 自动登录
         * 勾选自动登录 --> 自动勾选 记住密码
         */
        cb_RememberPwd = (CheckBox) view.findViewById(R.id.is_remember_password);
        cb_AutoLogin = (CheckBox) view.findViewById(R.id.is_auto_login);

        cb_RememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    cb_AutoLogin.setChecked(false);
                }
            }
        });

        cb_AutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    cb_RememberPwd.setChecked(true);
                }
            }
        });

        isRememberPwd = SPUtils.getBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_REMEMBER, false);
        if (isRememberPwd) {
            username = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USERNAME, null);
            password = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null);
            et_account.setText(username);
            et_pwd.setText(password);
            cb_RememberPwd.setChecked(true);
        }

        final Button thirdPartLoginBtn = (Button) view.findViewById(R.id.third_part_login_btn);
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
                if (username.equals("")) {
                    Snackbar.make(view, "请输入注册邮箱或手机号", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                } else if (pwd.equals("")) {
                    Snackbar.make(view, "请输入密码", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }
                user.setUsername(username);
                password = Md5Utils.encode(pwd);
                user.setPassword(password);

                // ProgressDialog
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setMessage("正在登录中...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User u, BmobException e) {
                        if (e == null) {
                            Listener listener = (Listener) getActivity();
                            listener.onSubmitLoginBtnClick();

                            Log.i(TAG, "登录成功，objectId：" + u.getObjectId());

                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USERNAME, username);
                            SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, true);
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, u.getObjectId());

                            //是否自动登录
                            if (cb_AutoLogin.isChecked()) {
                                SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, true);
                                SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_REMEMBER, true);

                                SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, pwd);
                            } else if (cb_RememberPwd.isChecked()) {
                                // 是否记住密码
                                SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_REMEMBER, true);
                                SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, pwd);
                            } else {
                                SPUtils.clear(getActivity(), Constants.SP_FILE_NAME);
                            }

                            progress.dismiss();
                        } else {
                            SPUtils.clear(getActivity(), Constants.SP_FILE_NAME);
                            Snackbar.make(view, "用户名或密码错误", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Log.e(TAG, e.toString());
                            progress.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }
}
