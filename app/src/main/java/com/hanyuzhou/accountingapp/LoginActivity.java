package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et_password;
    private String psw, psw_store, imagePsw;
    public static String username = "guigui";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init(){
        Button btn_login = (Button)findViewById(R.id.btn_login);
        TextView set_paw = (TextView)findViewById(R.id.set_psw);
        et_password = (EditText)findViewById(R.id.psw);

        if(readPsw(username) == null){
            set_paw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,SetActivity.class);

                    startActivity(intent);
                }
            });
        }else {
            set_paw.setText("图形密码");
            if(readimagePsw(username) == null){
                set_paw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LoginActivity.this,"未设置图形密码",Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                set_paw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,LogActivity2.class);
                        //LoginActivity.this.finish();
                        startActivity(intent);
                    }
                });
            }
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                psw = et_password.getText().toString().trim();
                psw_store = readPsw(username);

                if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else if(psw_store != null && !TextUtils.isEmpty(psw_store) && !psw.equals(psw_store)){
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
                else if(psw.equals(psw_store)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true, username);
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    setResult(RESULT_OK, data);
                    LoginActivity.this.finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "未设置密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String readPsw(String username){
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(username, null);
    }
    public String  readimagePsw(String name){
        @SuppressLint("CommitPrefEdits") SharedPreferences editor = getSharedPreferences("DATATYPE", Activity.MODE_PRIVATE);

        return editor.getString(name,null);
    }
    private void saveLoginStatus(boolean status, String username){
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", username);
        editor.apply();
    }
}