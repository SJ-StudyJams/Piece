package com.sealiu.piece.controller;

import android.content.Context;

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
        Bmob.initialize(this, "cc31d2e7036aa868bb7ed4401c1354fe");

        // 初始化短信SDK
        BmobSMS.initialize(this, "cc31d2e7036aa868bb7ed4401c1354fe");
    }
    public static Context getContext() {
        return context;
    }
}
