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
import com.sealiu.piece.utils.SPUtils;


/**
 * Created by art2cat on 7/6/2016.
 */
public class EditPhoneFragment extends DialogFragment{
    private String phone, password;
    private TextView phoneTV, passwordTV;
    private UserInfoSync userInfoSync = new UserInfoSync();
    private User user2;

    public interface EditPhoneDialogListener {
        void onEditPhoneDialogPositiveClick(DialogFragment dialog, String phone);

        void onEditPhoneDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditPhoneDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditPhoneDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditPhoneDialogListener");
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
        //获取本地用户信息
        user2 = userInfoSync.getLoginInfo(getContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_phone, null);
        builder.setView(view);
        
        phone = user2.getMobilePhoneNumber();
        password = user2.getPwd();
        Log.i("test", "password" + password);
        Log.i("test", "phone" + phone);

        phoneTV = (TextView) view.findViewById(R.id.edit_user_phone);
        passwordTV = (TextView) view.findViewById(R.id.edit_user_phone_password);
        phoneTV.setText(phone);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String password1 = passwordTV.getText().toString();
                //String password2 = (String) User.getObjectByKey("password");
                //String password2 = "123456";

                if (password1.equals(password)) {
                    if (phoneTV != null) {
                        String phone = phoneTV.getText().toString();
                        EditPhoneDialogListener listener = (EditPhoneDialogListener) getActivity();
                        listener.onEditPhoneDialogPositiveClick(
                                EditPhoneFragment.this,
                                phone
                        );
                    }
                } else {
                    onStart();
                    Toast.makeText(getActivity(), "密码不正确", Toast.LENGTH_SHORT).show();
                }




            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditPhoneDialogListener listener = (EditPhoneDialogListener) getActivity();
                listener.onEditPhoneDialogNegativeClick(EditPhoneFragment.this);
            }
        });

        return builder.create();
    }

}
