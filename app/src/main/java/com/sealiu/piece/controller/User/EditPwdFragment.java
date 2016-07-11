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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.Md5Utils;
import com.sealiu.piece.utils.SPUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by liuyang
 * on 2016/7/4.
 */
public class EditPwdFragment extends DialogFragment {

    //private User user;
    private EditText old_pwd_ET, new_pwd_ET, repeat_pwd_ET;
    private String old_pwd, new_pwd, repeat_pwd;
    private String encryptOldPassword, encryptNewPassword;
    private Button change_Password;
    private static final String TAG = "EditPwdFragment";
    private UserInfoSync userInfoSync = new UserInfoSync();
    private User user2;
    private BmobUser user = new BmobUser();

    public interface EditPwdDialogListener {
        void onEditPwdDialogPositiveClick(DialogFragment dialog);

        void onEditPwdDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditPwdDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditPwdDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditPwdDialogListener");
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
        //user2 = userInfoSync.getLoginInfo(getContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_pwd, null);

        builder.setView(view);
        old_pwd_ET = (EditText) view.findViewById(R.id.edit_old_pwd);
        new_pwd_ET = (EditText) view.findViewById(R.id.edit_new_pwd);
        repeat_pwd_ET = (EditText) view.findViewById(R.id.edit_repeat_new_pwd);

        final EditPwdDialogListener listener = (EditPwdDialogListener) getActivity();

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                old_pwd = old_pwd_ET.getText().toString();
                Log.i(TAG, "old password:" + old_pwd);
                new_pwd = new_pwd_ET.getText().toString();
                Log.i(TAG, "new password:" + new_pwd);
                repeat_pwd = repeat_pwd_ET.getText().toString();
                Log.i(TAG, "repeat new password:" + repeat_pwd);

                final String password = user2.getPwd();

                // 原密码错误
                if (!password.equals(old_pwd)) {
                    Snackbar.make(EditActivity.layoutScroll, "原密码错误", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // 两次密码输入不一致
                if (!new_pwd.equals(repeat_pwd)) {
                    Snackbar.make(EditActivity.layoutScroll, "两次密码输入不一致", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // 修改密码
                try {
                    encryptOldPassword = Md5Utils.encode(old_pwd);
                    encryptNewPassword = Md5Utils.encode(new_pwd);
                    BmobUser.updateCurrentUserPassword(encryptOldPassword, encryptNewPassword, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                if (new_pwd != null) {
                                    SPUtils.putString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_PASSWORD, new_pwd);
                                    Log.i(TAG, "password update");
                                }
                            } else {
                                Log.e(TAG, e.toString());
                                String content = Constants.createErrorInfo(e.getErrorCode()) + " 错误码：" + e.getErrorCode();
                                Snackbar.make(EditActivity.layoutScroll, content, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Snackbar.make(EditActivity.layoutScroll, "密码修改成功，下次登录请使用新密码", Snackbar.LENGTH_LONG).show();
                listener.onEditPwdDialogPositiveClick(EditPwdFragment.this);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onEditPwdDialogNegativeClick(EditPwdFragment.this);
            }
        }).setTitle("修改密码");
        return builder.create();

    }
}
