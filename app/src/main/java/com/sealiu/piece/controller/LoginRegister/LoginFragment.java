package com.sealiu.piece.controller.LoginRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sealiu.piece.R;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class LoginFragment extends Fragment {

    public interface Listener {
        void onSubmitLoginBtnClick();

        void onBackRegisterBtnClick();

        void onThirdPartLoginBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                Listener listener = (Listener) getActivity();
                listener.onSubmitLoginBtnClick();
            }
        });

        return view;
    }
}
