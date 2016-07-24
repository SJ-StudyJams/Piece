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
import com.sealiu.piece.controller.BmobService;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private View view;
    private EditText et_account, et_pwd;
    private User user = new User();
    private LoginUser loginUser;
    private String username, pwd, encryptPassword;
    private ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        loginUser = new LoginUser();
        et_account = (EditText) view.findViewById(R.id.login_phone_or_email);
        et_pwd = (EditText) view.findViewById(R.id.login_password);

        Button thirdPartLoginBtn = (Button) view.findViewById(R.id.third_part_login_btn);
        Button backRegisterBtn = (Button) view.findViewById(R.id.back_register_button);
        Button submitLoginBtn = (Button) view.findViewById(R.id.submit_login_btn);
        Button resetPwdBtn = (Button) view.findViewById(R.id.find_pwd);

        thirdPartLoginBtn.setOnClickListener(this);
        backRegisterBtn.setOnClickListener(this);
        resetPwdBtn.setOnClickListener(this);


        submitLoginBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(final View view) {
        Listener listener = (Listener) getActivity();
        switch (view.getId()) {
            case R.id.third_part_login_btn:
                listener.onThirdPartLoginBtnClick();
                break;
            case R.id.back_register_button:
                listener.onBackRegisterBtnClick();
                break;
            case R.id.submit_login_btn:
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

                // ProgressDialog
                progress = new ProgressDialog(getActivity());
                progress.setMessage("正在登录中...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                loginUser = new LoginUser();
                BmobService bmobService = new BmobService(loginUser);
                loginUser = bmobService.login(username, pwd);

                //延时1秒执行判断是否登录成功
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (loginUser.isLogin()) {
                            progress.dismiss();
                            Listener listener = (Listener) getActivity();
                            listener.onSubmitLoginBtnClick();
                        } else {
                            progress.dismiss();
                            SPUtils.clear(getActivity(), Constants.SP_FILE_NAME);
                            String content = Constants.createErrorInfo(loginUser.getErrorMsg())
                                    + " 错误码：" + loginUser.getErrorMsg();
                            Snackbar.make(view, content, Snackbar.LENGTH_LONG).show();
                            Log.e(TAG, "登录失败" + loginUser.getErrorMsg());
                        }
                    }
                }, 1000);

                break;
            case R.id.find_pwd:
                listener.onResetPasswordBtnClick();
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        UserInfoSync.saveLoginInfo(getContext(), loginUser);
        super.onDestroyView();
    }

    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();

        void onResetPasswordBtnClick();
    }
}
