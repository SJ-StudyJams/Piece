package com.studyjams.piece.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by art2cat
 * on 16-7-15.
 */
public class PieceMainService extends Service {
    public static final String TAG = "PieceMainService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MainService Start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "MainService Stop");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
