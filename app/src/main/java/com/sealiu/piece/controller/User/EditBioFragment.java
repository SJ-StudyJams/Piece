package com.sealiu.piece.controller.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sealiu.piece.R;

/**
 * Created by liuyang
 * on 2016/7/4.
 */
public class EditBioFragment extends DialogFragment {
    public interface EditBioDialogListener {
        void onEditBioDialogPositiveClick(DialogFragment dialog, String name);

        void onEditBioDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditBioDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditBioDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditBioDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow()
                    .setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_bio, null);
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                TextView bioTV = (TextView) view.findViewById(R.id.edit_bio);
                if (bioTV != null) {
                    String bio = bioTV.getText().toString();
                    Log.i("EditFrag", bio);
                    EditBioDialogListener listener = (EditBioDialogListener) getActivity();
                    listener.onEditBioDialogPositiveClick(
                            EditBioFragment.this,
                            bio
                    );
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditBioDialogListener listener = (EditBioDialogListener) getActivity();
                listener.onEditBioDialogNegativeClick(EditBioFragment.this);
            }
        });

        return builder.create();
    }
}