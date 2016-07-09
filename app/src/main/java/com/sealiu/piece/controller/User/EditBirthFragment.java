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
import android.widget.DatePicker;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

/**
 * Created by art2cat
 * on 7/7/2016.
 */
public class EditBirthFragment extends DialogFragment {
    private String birthBefore, birthAfter;
    private int yearBefore, monthBefore, dayBefore;

    public interface EditBirthDialogListener {
        void onEditBirthDialogPositiveClick(DialogFragment dialog, String birthAfter, String birthBefore);

        void onEditBirthDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditBirthDialogListener eListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eListener = (EditBirthDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_edit_birth, null);
        builder.setView(view);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_Picker);
        birthBefore = SPUtils.getString(getActivity(), Constants.SP_FILE_NAME, Constants.SP_BIRTH, "");
        if (!birthBefore.equals("")) {
            try {
                //将字符串分割成字符串集合
                String[] birthS = birthBefore.split("-");
                String year = birthS[0];
                String month = birthS[1];
                String day = birthS[2];

                //将字符串转型成int
                yearBefore = Integer.parseInt(year);
                monthBefore = Integer.parseInt(month) - 1;
                dayBefore = Integer.parseInt(day);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            yearBefore = 1970;
            monthBefore = 6;
            dayBefore = 30;
        }

        // 初始化birthAfter，否则没有选择日期，直接点击确定时，birthAfter 为null
        birthAfter = birthBefore;

        datePicker.init(yearBefore, monthBefore, dayBefore, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                int month = monthOfYear + 1;
                birthAfter = year + "-" + month + "-" + dayOfMonth;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (birthAfter.equals(birthBefore)) {
                    Snackbar.make(EditActivity.layoutScroll, "填写的生日和之前一致，生日没有修改", Snackbar.LENGTH_LONG).show();
                    return;
                }

                EditBirthDialogListener listener = (EditBirthDialogListener) getActivity();
                listener.onEditBirthDialogPositiveClick(
                        EditBirthFragment.this,
                        birthAfter,
                        birthBefore
                );
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditBirthDialogListener listener = (EditBirthDialogListener) getActivity();
                listener.onEditBirthDialogNegativeClick(EditBirthFragment.this);
            }
        });

        return builder.create();
    }
}
