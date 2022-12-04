package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserSetActivity extends AppCompatActivity implements View.OnClickListener {
    Button setPassWord,set_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set);
        setPassWord = (Button)findViewById(R.id.btn_set);
        set_img = (Button)findViewById(R.id.btn_img);
        setPassWord.setOnClickListener(this);
        set_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_set){
            Intent intent = new Intent();
            intent.setClass(UserSetActivity.this,SetActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.btn_img) {
            Intent intent = new Intent();
            intent.setClass(UserSetActivity.this,SetActivity2.class);
            startActivity(intent);
        }
    }


}