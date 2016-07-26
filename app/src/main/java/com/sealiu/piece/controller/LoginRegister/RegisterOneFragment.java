package com.sealiu.piece.controller.LoginRegister;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Constants;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by liuyang
 * on 2016/7/2.
 */
public class RegisterOneFragment extends Fragment {
    private static final String TAG = "RegisterOneFragment";
    NextStepListener nextStepListener;
    private int flag = 0; // 手机号正确: 1  邮箱正确: 2
    private String phoneNumber = "";
    private View layoutView;
    private boolean isNotAskAgain = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            nextStepListener = (NextStepListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NextStepListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_one, container, false);

        layoutView = view;
        final EditText regPhoneOrEmailET = (EditText) view.findViewById(R.id.reg_phone_or_email);
        final Button fetchCodeBtn = (Button) view.findViewById(R.id.fetch_code);
        final EditText validatingCodeET = (EditText) view.findViewById(R.id.validating_code);
        Button nextBtn = (Button) view.findViewById(R.id.reg_next_button);

        final NextStepListener listener = (NextStepListener) getActivity();

        nextStepListener = listener;
        //---------------------------------------------------------------------------------
        //点击获取验证码后，检验手机号/邮箱地址是否合法，合法则发送验证码/激活邮件
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            fetchCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String phoneOrEmail = regPhoneOrEmailET.getText().toString();

                    checkEmailOrPhone(phoneOrEmail);

                    switch (flag) {
                        case 1:
                            // 发送短信验证码
                            phoneNumber = phoneOrEmail.substring(phoneOrEmail.length() - 11, phoneOrEmail.length());
                            sendSMSCode(getActivity(), phoneNumber);
                            break;
                        case 2:
                            // 发送邮箱验证码(后台自动发送)，不需要立即验证
                            validatingCodeET.setText("验证邮件已发送，注册后请及时验证邮箱！");
                            validatingCodeET.setFocusable(false);
                            validatingCodeET.setClickable(false);

                            // 避免发送验证码/激活邮件之后,修改手机号/邮箱. 虽然不会有影响.
                            regPhoneOrEmailET.setFocusable(false);
                            regPhoneOrEmailET.setClickable(false);
                            listener.onFetchCodeBtnClick(flag, phoneOrEmail);
                            break;
                        default:
                            Snackbar.make(view, "验证出现问题", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            return;
                    }

                    fetchCodeBtn.setText("已发送");
                    fetchCodeBtn.setClickable(false);
                }
            });
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        111);
            } else {
                Snackbar.make(view, "你拒绝了手机状态信息权限申请", Snackbar.LENGTH_LONG)
                        .setAction("授予权限", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 123);
                            }
                        }).show();
            }

            if (isNotAskAgain) {
                Snackbar.make(view, "你拒绝了手机状态信息权限申请", Snackbar.LENGTH_LONG)
                        .setAction("授予权限", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 123);
                            }
                        }).show();
            }
        }

        //---------------------------------------------------------------------
        // 验证完成手机号/邮箱，下一步
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (flag == 0) {
                    snackBarTips("请先验证邮箱/手机号");
                    return;
                }

                // 手机号注册，验证码不能为空，且要通过验证
                if (flag == 1) {
                    String code = validatingCodeET.getText().toString();
                    if (code.equals("")) {
                        snackBarTips("验证码不能为空");
                        return;
                    } else if (!phoneNumber.equals("")) {
                        verifySMSCode(getActivity(), phoneNumber, code);
                    }// end-if 验证码为空验证
                }// end-if 手机号注册验证

                // 邮箱注册，放行！
                if (flag == 2) {
                    listener.onNextBtnClick(flag);
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED &&
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    // 用户拒绝了带有“不再询问”的权限申请
                    isNotAskAgain = true;
                } else if (Manifest.permission.READ_PHONE_STATE.equals(permission)) {
                    // 用户第一次拒绝了权限申请
                    // 向用户解释我们为什么要申请这个权限
                    showRationale(permission, R.string.permission_denied_phone_state);
                }
            }
        }
    }

    private void showRationale(String permission, int permissionDenied) {

        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(permission)
                .setMessage(getString(permissionDenied) + "。请重新授权！")
                .setPositiveButton("重新授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                12321);
                    }
                })
                .setNegativeButton("仍然拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    /**
     * 识别输入的是手机号还是邮箱地址
     * 检测手机号/邮箱地址是否为空，格式是否正确。
     *
     * @param phoneOrEmail 待检测的手机号/邮箱
     */
    private void checkEmailOrPhone(String phoneOrEmail) {
        //检测 regPhoneOrEmailET 是否为空
        if (phoneOrEmail.equals("")) {
            snackBarTips("注册邮箱/手机号不能为空");
            return;
        }

        //检测 手机号/邮箱地址格式是否合法
        String numberPattern = "[0-9\\+]+";
        String phonePattern = "(\\+\\d+)?1[3458]\\d{9}$";
        String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

        if (phoneOrEmail.matches(numberPattern)) {
            if (!phoneOrEmail.matches(phonePattern)) {
                snackBarTips("手机号格式错误");
            } else {
                flag = 1;
            }
        } else {
            if (!phoneOrEmail.matches(emailPattern)) {
                snackBarTips("邮箱格式错误");
            } else {
                flag = 2;
            }
        }
    }

    /**
     * 发送短信验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     */
    private void sendSMSCode(Context context, final String phoneNumber) {

        // smscode 为Bmob后台中Piece应用的发送验证码的短信模板名称
        BmobSMS.requestSMSCode(context, phoneNumber, "smscode", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    snackBarTips("验证码发送成功");
                    nextStepListener.onFetchCodeBtnClick(flag, phoneNumber);
                } else {
                    String content = Constants.createErrorInfo(e.getErrorCode()) + "错误码：" + e.getErrorCode();
                    snackBarTips(content);
                }
            }
        });
    }

    /**
     * 验证验证码
     *
     * @param context     上下文
     * @param phoneNumber 接收短信手机号（11位）
     * @param code        验证码
     */
    public void verifySMSCode(Context context, String phoneNumber, String code) {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("正在验证短信验证码...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        BmobSMS.verifySmsCode(context, phoneNumber, code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                // TODO Auto-generated method stub
                progress.dismiss();
                if (e == null) {
                    snackBarTips("验证通过");
                    nextStepListener.onNextBtnClick(flag);
                } else {
                    String content = Constants.createErrorInfo(e.getErrorCode()) + " 错误码：" + e.getErrorCode();
                    snackBarTips(content);
                }
            }
        });
    }

    /**
     * 信息提示
     *
     * @param content 提示内容
     */
    private void snackBarTips(String content) {
        Snackbar.make(layoutView, content, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public interface NextStepListener {
        void onNextBtnClick(int flag);

        void onFetchCodeBtnClick(int flag, String phoneOrEmail);
    }

}
