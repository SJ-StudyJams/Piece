package com.sealiu.piece.controller.LoginRegister;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sealiu.piece.R;

/**
 * Created by art2cat
 * on 16-7-16.
 */
public class ResetPwdByPhoneFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd_phone, null);
        return view;
    }
}
