package com.sealiu.piece.controller.Piece;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sealiu.piece.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liuyang
 * on 2016/7/17.
 */
public class UrlFragment extends DialogFragment {

    private static final String REQUIRED = "Required";
    private static final String INVALID = "Invalid URL";

    EditText urlEditText;
    ImageButton clearBtn;
    UrlListener uListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            uListener = (UrlListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UrlListener");
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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_url, null);

        urlEditText = (EditText) view.findViewById(R.id.url_edit_text);
        clearBtn = (ImageButton) view.findViewById(R.id.clear);

        urlEditText.setText("Http://");
        urlEditText.setSelection(urlEditText.getText().length());

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlEditText.setText("Http://");
                urlEditText.setSelection(urlEditText.getText().length());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);


        final UrlListener listener = (UrlListener) getActivity();
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String content = urlEditText.getText().toString();

                if (content.equals("Http://") || TextUtils.isEmpty(content)) {
                    urlEditText.setError(REQUIRED);
                    return;
                }

                if (!isValidURL(content)) {
                    urlEditText.setError(INVALID);
                    return;
                }

                listener.onUrlPositiveClick(content);
                dismiss();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onUrlNegativeClick();
                dismiss();
            }
        }).setTitle(R.string.share_url);

        return builder.create();
    }

    private boolean isValidURL(String urlStr) {
        try {
            new URL(urlStr);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface UrlListener {
        void onUrlPositiveClick(String url);

        void onUrlNegativeClick();
    }
}
