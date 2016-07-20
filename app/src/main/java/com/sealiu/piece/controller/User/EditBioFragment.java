package com.sealiu.piece.controller.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
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
    // Use this instance of the interface to deliver action events
    EditBioDialogListener eListener;
    private String bioBefore;
    private TextView bioTV;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditBioDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
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

        bioBefore = getArguments().getString("bio");
        bioTV = (TextView) view.findViewById(R.id.edit_bio);

        if (bioBefore != null)
            bioTV.setText(bioBefore);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                String bioString = bioTV.getText().toString();
                // 执行bio输入内容的检查，以后要加上字数限制。
                if (bioString.equals(bioBefore)) {
                    Snackbar.make(EditActivity.layoutScroll, "填写的个人简介和之前一致，个人简介没有修改", Snackbar.LENGTH_LONG).show();
                    return;
                }

                EditBioDialogListener listener = (EditBioDialogListener) getActivity();
                listener.onEditBioDialogPositiveClick(
                        EditBioFragment.this,
                        bioString,
                        bioBefore
                );
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

    public interface EditBioDialogListener {
        void onEditBioDialogPositiveClick(DialogFragment dialog, String newBio, String oldBio);

        void onEditBioDialogNegativeClick(DialogFragment dialog);
    }
}
