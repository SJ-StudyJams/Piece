package com.sealiu.piece.controller;

import android.content.Context;

import com.sealiu.piece.model.Constants;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by Peach
 * on 2016/7/4.
 */
public class PieceAPP extends android.support.multidex.MultiDexApplication{
    private static Context context;
    public static PieceAPP app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();
        // 初始化BmobSDK
        Bmob.initialize(this, Constants.BMOB_APP_ID);

        // 初始化短信SDK
        BmobSMS.initialize(this, Constants.BMOB_APP_ID);
    }
    public static Context getContext() {
        return context;
    }
}
