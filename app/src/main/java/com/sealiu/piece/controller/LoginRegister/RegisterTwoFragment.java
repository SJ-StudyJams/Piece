package com.sealiu.piece.controller.LoginRegister;

import android.content.Context;
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
public class RegisterTwoFragment extends Fragment {

    public interface CompleteRegisterListener {
        void onCompleteRegisterBtnClick();
    }

    CompleteRegisterListener completeRegisterListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            completeRegisterListener = (CompleteRegisterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CompleteRegisterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_two, container, false);

        Button completeRegisterBtn = (Button) view.findViewById(R.id.reg_complete_button);
        completeRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteRegisterListener listener = (CompleteRegisterListener) getActivity();
                listener.onCompleteRegisterBtnClick();
            }
        });

        return view;
    }
}
