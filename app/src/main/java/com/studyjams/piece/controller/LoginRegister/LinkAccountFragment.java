package com.studyjams.piece.controller.LoginRegister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.studyjams.piece.R;

/**
 * Created by liuyang
 * on 2016/8/6.
 */

public class LinkAccountFragment extends DialogFragment {
    LinkAccountFragment.LinkAccountListener linkAccountListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            linkAccountListener = (LinkAccountListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement LinkAccountFragment");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_link_account, null);

        TextView google = (TextView) view.findViewById(R.id.link_google);
        TextView fb = (TextView) view.findViewById(R.id.link_fb);

        final LinkAccountListener listener = (LinkAccountListener) getActivity();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGoogleClick();
                dismiss();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFBClick();
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    public interface LinkAccountListener {
        void onGoogleClick();

        void onFBClick();
    }
}
