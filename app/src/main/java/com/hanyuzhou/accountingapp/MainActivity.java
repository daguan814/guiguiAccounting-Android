package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private TextView amountText;
    private TextView earnText;
    private TextView dateText;
    private Spinner spinner;
    private int currentPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;
        String iemi = "0";

        iemi = getDeviceiemi();

        //没有获取到设备码

        Toast.makeText(this, "获取到设备码" + iemi, Toast.LENGTH_SHORT).show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("iemi", iemi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://106.13.2.240:9000/codeUser/login";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject jsonObject) {
                try {
                    String code = jsonObject.getString("code");
                    Log.d("code", code);
                    //如果查询到了这个机器码,不做任何跳转
                    if (code.equals("200")) {

                        Toast.makeText(MainActivity.this, "欢迎您,您已激活", Toast.LENGTH_SHORT).show();

                        //如果没有查到了这个机器码，跳转到激活页面
                    } else if (code.equals("100")) {
                        Toast.makeText(MainActivity.this, "您没有激活本软件，请激活后使用", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, firstlogin.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

        //getSupportActionBar().setElevation(0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nav);
        spinner = (Spinner) findViewById(R.id.spinner_time);
        amountText = (TextView) findViewById(R.id.total_cost);
        earnText = (TextView) findViewById(R.id.total_earn);
        dateText = (TextView) findViewById(R.id.month);

        navigationView.setItemTextColor(null);
        navigationView.setItemIconTintList(null);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
        };

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), 4);
        initViewpager(4);
        updateHeader();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initViewpager(i + 1);
                updateHeader();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                String select = item.getTitle().toString();
                Intent intent = new Intent();
                switch (select) {
                    case "退出激活码":
                        intent.setClass(MainActivity.this, SetActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case "关于我":
                        intent.setClass(MainActivity.this, SetActivity2.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case "分类":
                        intent.setClass(MainActivity.this, Class2Activity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }


                return false;
            }
        });
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        Button sts_btn = (Button) findViewById(R.id.sts_btn);
        Button graph_btn = (Button) findViewById(R.id.graph_btn);
        sts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });
        graph_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        updateHeader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Toast.makeText(MainActivity.this, data.getStringExtra("State"), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload();
        updateHeader();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "cost: " + pagerAdapter.getTotalCost(position));
        currentPagerPosition = position;
        updateHeader();
    }

    public void initViewpager(int choice) {
        pagerAdapter.initFragments(choice);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(pagerAdapter.getLatsIndex());
    }

    public void updateHeader() {
        String cost = String.valueOf(pagerAdapter.getTotalCost(currentPagerPosition));
        amountText.setText("￥" + cost);
        String earn = String.valueOf(pagerAdapter.getTotalEarn(currentPagerPosition));
        earnText.setText("￥" + earn);
        String date = pagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(date);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_1,menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static MainActivity mainActivity;

    public ArrayList<String> pullcategory() {
        int index = pagerAdapter.getLatsIndex();
        ArrayList<String> str = pagerAdapter.get_category(index);
        return str;
    }

    public ArrayList<String> pullcategory2() {
        int index = pagerAdapter.getLatsIndex();
        ArrayList<String> str = pagerAdapter.get_category2(index);
        return str;
    }

    public ArrayList<String> pullmember() {
        int index = pagerAdapter.getLatsIndex();
        ArrayList<String> str = pagerAdapter.get_member(index);
        return str;
    }

    public ArrayList<String> pullaccount() {
        int index = pagerAdapter.getLatsIndex();
        ArrayList<String> str = pagerAdapter.get_account(index);
        return str;
    }

    public String getDeviceiemi() {
        return android.os.Build.DEVICE;
    }


}
