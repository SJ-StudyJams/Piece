package com.sealiu.piece.controller.LoginRegister;

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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by art2cat
 * on 16-7-16.
 */
public class ResetPwdByEmailFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText editText;
    private Button button;
    private String email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resetpwd_email, null);
        editText = (EditText) view.findViewById(R.id.resetPwd_email);

        button = (Button) view.findViewById(R.id.fetch_code);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(final View view) {
        email = editText.getText().toString();
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "重置密码失败:" + e, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
