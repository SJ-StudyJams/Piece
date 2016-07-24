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
import android.widget.Button;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;


/**
 * Created by art2cat
 * on 7/6/2016.
 */
public class EditPhoneFragment extends DialogFragment{
    // Use this instance of the interface to deliver action events
    EditPhoneDialogListener eListener;
    private String phoneBefore, password, smsCode, phoneAfter;
    private EditText phoneBfET, phoneAfET, passwordET, smsCodeET;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditPhoneDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_phone, null);
        builder.setView(view);

        phoneBefore = getArguments().getString("phone");
        password = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, null);


        //phoneBfET = (EditText) view.findViewById(R.id.edit_user_before_phone);
        phoneAfET = (EditText) view.findViewById(R.id.edit_user_after_phone);
        smsCodeET = (EditText) view.findViewById(R.id.edit_user_smsCode);
        passwordET = (EditText) view.findViewById(R.id.edit_user_password);

        if (phoneBefore != null) {
            phoneAfET.setText(phoneBefore);
            phoneAfET.setSelection(phoneBefore.length());
        }

        Button button = (Button) view.findViewById(R.id.send_v);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                phoneAfter = phoneAfET.getText().toString();
                BmobSMS.requestSMSCode(getContext(), phoneAfter, "smscode", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "验证码发送成功", Snackbar.LENGTH_LONG).show();
                        } else {
                            String content = Constants.createErrorInfo(e.getErrorCode());
                            Snackbar.make(view, "验证码发送失败:" + content, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                String passwordString = passwordET.getText().toString();
                phoneAfter = phoneAfET.getText().toString();
                smsCode = smsCodeET.getText().toString();

                if (!password.equals(passwordString)) {
                    Snackbar.make(EditActivity.layoutScroll, "密码不正确", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (phoneAfter.equals(phoneBefore)) {
                    Snackbar.make(EditActivity.layoutScroll, "填写的手机号和之前一致，手机号没有修改", Snackbar.LENGTH_LONG).show();
                    return;
                }

                EditPhoneDialogListener listener = (EditPhoneDialogListener) getActivity();
                listener.onEditPhoneDialogPositiveClick(
                        EditPhoneFragment.this,
                        phoneAfter, smsCode
                );

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

    public interface EditPhoneDialogListener {
        void onEditPhoneDialogPositiveClick(DialogFragment dialog, String phone, String smsCode);

        void onEditPhoneDialogNegativeClick(DialogFragment dialog);
    }

}
