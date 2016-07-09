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
    private String username, pwd, encryptPassword;
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
                encryptPassword = Md5Utils.encode(pwd);
                user.setPassword(encryptPassword);

                Log.i(TAG, "user input pwd :" + encryptPassword);

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

                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USERNAME, u.getUsername());
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, pwd);
                            SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_AUTO_LOGIN, true);
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, u.getObjectId());

                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_EMAIL, u.getEmail());
                            SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_VALID_EMAIL, u.getEmailVerified());

                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PHONE_NUMBER, u.getMobilePhoneNumber());
                            SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_VALID_PHONE_NUMBER, u.getMobilePhoneNumberVerified());
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
