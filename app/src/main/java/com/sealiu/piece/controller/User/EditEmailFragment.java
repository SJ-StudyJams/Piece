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
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

/**
 * Created by art2cat
 * on 7/7/2016.
 */
public class EditEmailFragment extends DialogFragment {
    // Use this instance of the interface to deliver action events
    EditEmailDialogListener eListener;
    private String emailBefore, password;
    private EditText emailET, passwordET;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditEmailDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditEmailDialogListener");
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
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_email, null);
        builder.setView(view);

        emailBefore = getArguments().getString("email");
        password = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null);

        emailET = (EditText) view.findViewById(R.id.edit_user_email);
        passwordET = (EditText) view.findViewById(R.id.edit_user_password);

        if (emailBefore != null) {
            emailET.setText(emailBefore);
            emailET.setSelection(emailBefore.length());
        }
        final EditEmailDialogListener listener = (EditEmailDialogListener) getActivity();
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String passwordString = passwordET.getText().toString();
                String emailAfter = emailET.getText().toString();

                if (!passwordString.equals(password)) {
                    Snackbar.make(EditActivity.layoutScroll, "密码不正确", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (emailBefore != null && emailBefore.equals(emailAfter)) {
                    Snackbar.make(EditActivity.layoutScroll, "填写的邮箱和之前一致，邮箱没有修改", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Snackbar.make(EditActivity.layoutScroll, "邮箱修改成功，激活邮件已发送", Snackbar.LENGTH_LONG).show();

                listener.onEditEmailDialogPositiveClick(
                        EditEmailFragment.this,
                        emailAfter
                );
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onEditEmailDialogNegativeClick(EditEmailFragment.this);
            }
        });

        return builder.create();
    }

    public interface EditEmailDialogListener {
        void onEditEmailDialogPositiveClick(DialogFragment dialog, String email);

        void onEditEmailDialogNegativeClick(DialogFragment dialog);
    }
}
