package com.hanyuzhou.accountingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class firstlogin extends AppCompatActivity {

    private EditText jihuoCode;

    public static String username = "guigui";
    String iemi = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);


        iemi = getDeviceiemi();

        Toast.makeText(this, "获取到设备码" + iemi, Toast.LENGTH_SHORT).show();
        Button jihuo_btn = (Button)findViewById(R.id.btn_jihuo);
        jihuoCode = (EditText)findViewById(R.id.fl_pwdcode);


            jihuo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String jihuoma = jihuoCode.getText().toString();
                    iemi = getDeviceiemi();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("iemi", iemi);
                        jsonObject.put("pwdcode", jihuoma);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://106.13.2.240:9000/codeUser/jihuo";

                    RequestQueue requestQueue = Volley.newRequestQueue(firstlogin.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override

                        public void onResponse(JSONObject jsonObject) {
                            try {
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                Log.d("code", code);
                                //如果查询到了这个机器码,不做任何跳转
                                if (code.equals("200")) {
                                    Toast.makeText(firstlogin.this, "激活成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(firstlogin.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //如果没有查到了这个机器码，跳转到激活页面
                                } else if (code.equals("100")) {
                                    Toast.makeText(firstlogin.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(firstlogin.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);


                }
            });
        }
    public String getDeviceiemi() {
        return android.os.Build.DEVICE;
    }
        }









