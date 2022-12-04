package com.hanyuzhou.accountingapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CustomDialog extends Dialog {
    EditText et_input;
    Button bt_send;

    public CustomDialog(@NonNull Context context) {
        this(context,0);
    }
    public CustomDialog(@NonNull Context context,int themeResID) {
        super(context,themeResID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        et_input = (EditText)findViewById(R.id.et_input);
        bt_send = (Button)findViewById(R.id.bt_send);
    }
}
