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

    private EditText et_account, et_pwd;
    private User user = new User();
    private String username, pwd, encryptPassword;
    private ProgressDialog progress;
    private static final String TAG = "LoginFragment";


    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

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
                Log.i(TAG, "" + encryptPassword);
                user.setPassword(encryptPassword);

                // ProgressDialog
                progress = new ProgressDialog(getActivity());
                progress.setMessage("正在登录中...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User u, BmobException e) {
                        if (e == null) {
                            //记录本次登录时间，设置登录标志位
                            SPUtils.putLong(getActivity(), Constants.SP_FILE_NAME, Constants.SP_LOGIN_TIME, System.currentTimeMillis());
                            SPUtils.putBoolean(getActivity(), Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, true);
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, u.getObjectId());
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_USERNAME, u.getUsername());
                            SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, pwd);
                            progress.dismiss();
                            Listener listener = (Listener) getActivity();
                            listener.onSubmitLoginBtnClick();
                        } else {
                            SPUtils.clear(getActivity(), Constants.SP_FILE_NAME);
                            String content = Constants.createErrorInfo(e.getErrorCode()) + " 错误码：" + e.getErrorCode();
                            Snackbar.make(view, content, Snackbar.LENGTH_LONG).show();
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
