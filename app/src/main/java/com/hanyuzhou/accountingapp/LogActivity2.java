package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LogActivity2 extends AppCompatActivity implements imgePassWord.OnPatternChangeListener{

    String pat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log2);
        imgePassWord lpv = (imgePassWord) findViewById(R.id.lock_pattern_view);
        lpv.setOnPatternChangeListener(this);
    }

    @Override
    public void onPatternChange(String patternPassword) {
        pat = get(LoginActivity.username);
        if(patternPassword==null){
            Toast.makeText(LogActivity2.this,"请至少3个点",Toast.LENGTH_SHORT).show();
        }else{

            if(patternPassword.equals(pat)){
                Toast.makeText(LogActivity2.this,"密码正确",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(LogActivity2.this,MainActivity.class);
                setResult(RESULT_OK, intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(LogActivity2.this,"密码错误",Toast.LENGTH_SHORT).show();
        }

    }

    public String  get(String name){
        @SuppressLint("CommitPrefEdits") SharedPreferences editor = getSharedPreferences("DATATYPE", Activity.MODE_PRIVATE);

        return editor.getString(name,null);
    }
    @Override
    public void onPatternStarted(boolean isStarted) {

    }
}