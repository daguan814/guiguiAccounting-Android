package com.hanyuzhou.accountingapp;

import android.content.Context;

public class util {
    private static final String TAG = "wow";

    private static util instance;

    public DBhelper dbHelper;
    private Context context;

    public static util getInstance(){
        if(instance == null){
            instance = new util();
        }
        return instance;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context){
        this.context = context;
        dbHelper = new DBhelper(context,DBhelper.DB_NAME,null,1);
    }
}
