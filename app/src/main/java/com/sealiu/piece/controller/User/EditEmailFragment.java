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
import android.widget.Toast;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.BmobUser;

/**
 * Created by art2cat on 7/7/2016.
 */
public class EditEmailFragment extends DialogFragment {
    private String email, password;
    private TextView emailTV, passwordTV;
    private BmobUser user1 = BmobUser.getCurrentUser();

    public interface EditEmailDialogListener {
        void onEditEmailDialogPositiveClick(DialogFragment dialog, String email);

        void onEditEmailDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditEmailDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditEmailDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
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

        String objectId = user1.getObjectId();

        email = SPUtils.getString(getActivity(), objectId, Constants.SP_EMAIL, null);
        password = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null);
        Log.i("test", "password" + password);
        Log.i("test", "Email" + email);

        emailTV = (TextView) view.findViewById(R.id.edit_user_email);
        passwordTV = (TextView) view.findViewById(R.id.edit_user_password);
        emailTV.setText(email);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String password1 = passwordTV.getText().toString();

                if (password1.equals(password)) {
                    if (emailTV != null) {
                        String email = emailTV.getText().toString();
                        EditEmailDialogListener listener = (EditEmailDialogListener) getActivity();
                        listener.onEditEmailDialogPositiveClick(
                                EditEmailFragment.this,
                                email
                        );
                    }
                } else {
                    Toast.makeText(getActivity(), "密码不正确", Toast.LENGTH_SHORT).show();
                }




            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditEmailDialogListener listener = (EditEmailDialogListener) getActivity();
                listener.onEditEmailDialogNegativeClick(EditEmailFragment.this);
            }
        });

        return builder.create();
    }
}
