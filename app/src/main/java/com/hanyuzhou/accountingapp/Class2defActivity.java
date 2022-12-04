package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Class2defActivity extends AppCompatActivity implements View.OnClickListener{
    EditText str_edit;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class2def);
        str_edit = (EditText) findViewById(R.id.edit);
        TextView tv_f = (TextView) findViewById(R.id.tv_finish);
        tv_f.setOnClickListener(this);
        Intent intents = getIntent();
        str = intents.getStringExtra("str");
    }
    @Override
    public void onClick(View v) {
        String secondstr = str_edit.getText().toString();
        Intent intent = new Intent(Class2defActivity.this,AddRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("str",str);
        intent.putExtra("secondstr", secondstr);

        startActivity(intent);
        finish();

    }
}