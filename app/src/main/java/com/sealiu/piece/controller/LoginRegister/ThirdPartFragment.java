package com.sealiu.piece.controller.LoginRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sealiu.piece.R;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class ThirdPartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_part_login, container, false);
    }
}
