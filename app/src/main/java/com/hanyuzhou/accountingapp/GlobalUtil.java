package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.LinkedList;

public class GlobalUtil {

    private static final String TAG = "GlobalUtil";

    @SuppressLint("StaticFieldLeak")
    private static GlobalUtil instance;

    public RecordDatabaseHelper databaseHelper;
    private Context context;
    public MainActivity mainActivity;
    public cash_account cash_account;

    public LinkedList<CategoryResBean> costRes = new LinkedList<>();
    public LinkedList<CategoryResBean> earnRes = new LinkedList<>();
    public LinkedList<String> accounts = new LinkedList<>();
    private int costnum = 10;
    private int [] costIconRes = {
            R.mipmap.icon,

    };
    private int [] costIconResBlack = {
            R.mipmap.icon,

    };
    private  String[] costTitle = {"餐饮"};
    private int earnnum = 6;
    private int[] earnIconRes = {

            //R.drawable.icon_reimburse_white,
            R.mipmap.icon,
    };

    private  int[] earnIconResBlack = {

            //R.drawable.icon_reimburse,
            R.mipmap.icon,
    };

    private String[] earnTitle = {"工资"};

    public Context getContext() {
        return context;
    }

    public void addRes(){
        for (int i = 0; i < costTitle.length; i++) {
            CategoryResBean res = new CategoryResBean();
            res.title = costTitle[i];
            res.resBlack = costIconResBlack[i];
            res.resWhite = costIconRes[i];
            costRes.add(res);
        }

        for (int i = 0; i < earnTitle.length; i++) {
            CategoryResBean res = new CategoryResBean();
            res.title = earnTitle[i];
            res.resBlack = earnIconResBlack[i];
            res.resWhite = earnIconRes[i];
            earnRes.add(res);
        }
    }

    public void init_account(String account){
        if(!accounts.contains(account)){
            accounts.add(account);
        }

    }

    public void addicon(){

    }

    public void setContext(Context context) {
        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context,RecordDatabaseHelper.DB_NAME,null,1);

        for (int i=0;i<costTitle.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = costTitle[i];
            res.resBlack = costIconResBlack[i];
            res.resWhite = costIconRes[i];
            costRes.add(res);
        }

        for (int i=0;i<earnTitle.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = earnTitle[i];
            res.resBlack = earnIconResBlack[i];
            res.resWhite = earnIconRes[i];
            earnRes.add(res);
        }

    }

    public static GlobalUtil getInstance(){
        if (instance==null){
            instance = new GlobalUtil();
        }
        return instance;
    }

    public int getResourceIcon(String category){
        for (CategoryResBean res :
                costRes) {
            if (res.title.equals(category)){
                return res.resWhite;
            }
        }

        for (CategoryResBean res :
                earnRes) {
            if (res.title.equals(category)){
                return res.resWhite;
            }
        }

        return costRes.get(0).resWhite;
    }
}
