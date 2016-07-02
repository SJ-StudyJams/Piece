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
public class RegisterOneFragment extends Fragment {

    public interface NextStepListener {
        void onNextBtnClick();
    }

    NextStepListener nextStepListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            nextStepListener = (NextStepListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NextStepListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_one, container, false);
        Button nextBtn = (Button) view.findViewById(R.id.reg_next_button);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextStepListener listener = (NextStepListener) getActivity();
                listener.onNextBtnClick();
            }
        });
        return view;
    }
}
