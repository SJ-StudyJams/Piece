package com.sealiu.piece.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Vince
 * @time 2016-07-02 下午 10:34
 * @updateAuthor $Author$
 * @updateDate $Date$
 */
public class SPUtils {
    public static void putBoolean(Context context, String key, boolean value) {    //添加保存数据
        SharedPreferences sp = context.getSharedPreferences(
                MyConstaints.SPFILMENAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
    public static void putString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstaints.SPFILMENAME, Context
                .MODE_PRIVATE);
        //保存数据
        sp.edit().putString(key, defValue).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstaints.SPFILMENAME, Context
                .MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(
                MyConstaints.SPFILMENAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, defValue);

    }
}
