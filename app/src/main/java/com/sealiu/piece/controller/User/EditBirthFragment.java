package com.sealiu.piece.controller.User;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by art2cat on 7/7/2016.
 */
public class EditBirthFragment extends DialogFragment {
    private String birth;
    private int yearI, monthI, dayI;
    private BmobUser user1 = BmobUser.getCurrentUser();

    public interface EditBirthDialogListener {
        void onEditBirthDialogPositiveClick(DialogFragment dialog, String birth);

        void onEditBirthDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditBirthDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditBirthDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditBirthDialogListener");
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
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        String objectId = user1.getObjectId();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_birth, null);
        builder.setView(view);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_Picker);
        birth = SPUtils.getString(getActivity(), objectId, Constants.SP_BIRTH, null);
        try {
            //将字符串分割成字符串集合
            String[] birthS = birth.split("-");
            String year = birthS[0];
            String month = birthS[1];
            String day = birthS[2];
            //将字符串转型成int
            yearI = Integer.parseInt(year);
            monthI = Integer.parseInt(month);
            dayI = Integer.parseInt(day);
            Log.i("test", "" + yearI);
            Log.i("test", "" + monthI);
            Log.i("test", "" + dayI);
            int month2 = monthI - 1;
            datePicker.init(yearI, month2, dayI, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                    int month = monthOfYear + 1;
                    birth = year + "-" + month + "-" + dayOfMonth;
                    Log.i("test", birth);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditBirthDialogListener listener = (EditBirthDialogListener) getActivity();
                listener.onEditBirthDialogPositiveClick(
                        EditBirthFragment.this,
                        birth
                );
            }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            EditBirthDialogListener listener = (EditBirthDialogListener) getActivity();
            listener.onEditBirthDialogNegativeClick(EditBirthFragment.this);
        }
    }).setTitle("设置生日");

        return builder.create();
    }
}
