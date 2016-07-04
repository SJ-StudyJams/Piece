package com.sealiu.piece.controller;

import android.app.Application;
import android.content.Context;

/**
 * Created by Peach on 2016/7/4.
 */
public class PieceAPP extends Application{
    private static Context context;
    public static PieceAPP app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();

    }
    public static Context getContext() {
        return context;
    }
}
