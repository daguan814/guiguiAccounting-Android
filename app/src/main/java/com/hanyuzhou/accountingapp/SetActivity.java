package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class SetActivity extends AppCompatActivity {

    private String iemi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        iemi = getDeviceiemi();

        Button jihuo_btn = (Button)findViewById(R.id.btn_shifang);


        jihuo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iemi = getDeviceiemi();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("iemi", iemi);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "http://106.13.2.240:9000/codeUser/del";

                RequestQueue requestQueue = Volley.newRequestQueue(SetActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            Log.d("code", code);
                            //如果查询到了这个机器码，弹出提示信息，激活状态没了。
                            if (code.equals("200")) {
                                Toast.makeText(SetActivity.this, "释放激活码成功！", Toast.LENGTH_SHORT).show();

                                //如果没有查到了这个机器码，跳转到激活页面
                            } else if (code.equals("100")) {
                                Toast.makeText(SetActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SetActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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




