package com.sealiu.piece.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.sealiu.piece.model.Constants;

/**
 * @author Vince
 * @time 2016-07-02 下午 10:34
 * @updateAuthor $Author$
 * @updateDate $Date$
 */
public class SPUtils {
    public static void putBoolean(Context context, String key, boolean value) {    //添加保存数据
        SharedPreferences sp = context.getSharedPreferences(
                Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
    public static void putString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context
                .MODE_PRIVATE);
        //保存数据
        sp.edit().putString(key, defValue).apply();
    }

    public static String getString(Context context, String key, @Nullable String defValue) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context
                .MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(
                Constants.SP_FILE_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, defValue);
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context
                .MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
