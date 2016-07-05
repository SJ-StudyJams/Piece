package com.sealiu.piece.controller.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.sealiu.piece.model.User;

/**
 * Created by art2cat on 7/5/2016.
 */
public class UserSharePreference {

    private Context context;
    private String fileName = "userInfo";

    public UserSharePreference(Context context) {
        super();
        this.context = context;
    }
    public boolean saveMessage(User loginInfo){
        User preInfo = getLoginInfo();
        boolean flag = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        //对数据进行编辑
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userName", loginInfo.getUsername());              //用户名
        editor.putString("password", loginInfo.getPwd());            //密码
        editor.putLong("preTime", loginInfo.getTime());
        editor.putString("birth", loginInfo.getBirth());
        editor.putBoolean("sex", loginInfo.isSex());
        flag = editor.commit();
        return flag;
    }
    public User getLoginInfo(){
        User loginInfo = new User();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,  Context.MODE_PRIVATE);
        //对数据进行读取
        loginInfo.setUsername(sharedPreferences.getString("userName", null));
        loginInfo.setPwd(sharedPreferences.getString("password", null));
        loginInfo.setTime(sharedPreferences.getLong("preTime", 0));
        loginInfo.setBirth(sharedPreferences.getString("birth", null));
        loginInfo.setSex(sharedPreferences.getBoolean("sex", false));
        return loginInfo;
    }
}
