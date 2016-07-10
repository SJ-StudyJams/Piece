package com.sealiu.piece.controller.User;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sealiu.piece.R;

public class PickPictureFragment extends DialogFragment {

    public interface PickPictureListener {
        void onCameraClick();

        void onAlbumClick();
    }

    // Use this instance of the interface to deliver action events
    PickPictureListener pListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            pListener = (PickPictureListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement PickPictureListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pick_picture, null);

        TextView camera = (TextView) view.findViewById(R.id.capture_photo);
        TextView album = (TextView) view.findViewById(R.id.album_choose);

        final PickPictureListener listener = (PickPictureListener) getActivity();
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

        return new Builder(getActivity()).setView(view).create();
    }
}
