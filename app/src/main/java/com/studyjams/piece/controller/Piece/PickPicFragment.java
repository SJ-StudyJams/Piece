package com.studyjams.piece.controller.Piece;

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
 * on 2016/8/5.
 */

public class PickPicFragment extends DialogFragment {

    PickPicListener pickPicListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            pickPicListener = (PickPicListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement PickPicFragment");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pick_pic, null);

        TextView camera = (TextView) view.findViewById(R.id.camera);
        TextView album = (TextView) view.findViewById(R.id.album);

        final PickPicListener listener = (PickPicListener) getActivity();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCameraClick();
                dismiss();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAlbumClick();
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    public interface PickPicListener {
        void onCameraClick();

        void onAlbumClick();
    }
}
