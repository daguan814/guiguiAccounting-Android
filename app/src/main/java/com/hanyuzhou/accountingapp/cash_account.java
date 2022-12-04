package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class cash_account extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG ="cash_account";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private TextView costText;
    private TextView earnText;
    private TextView amountText;
    private TextView accountText;
    private Spinner spinner;
    private String account;
    private int currentPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_account);
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().cash_account = this;
        //getSupportActionBar().setElevation(0);

        accountText = (TextView)findViewById(R.id.account_cash);
        amountText = (TextView) findViewById(R.id.amount_cash);
        earnText = (TextView)findViewById(R.id.earn_cash);
        costText=(TextView) findViewById(R.id.cost_cash);
        spinner = (Spinner)findViewById(R.id.account_spinner);
        account = getIntent().getStringExtra("account");
        viewPager = (ViewPager) findViewById(R.id.view_pager);


        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),4);
        initViewpager(4);
        updateHeader();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initViewpager(i+1);
                updateHeader();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 2){
            Toast.makeText(cash_account.this,data.getStringExtra("State"),Toast.LENGTH_SHORT).show();
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
        Log.d(TAG,"cost: "+pagerAdapter.getTotalCost(position));
        currentPagerPosition = position;
        updateHeader();
    }
    public void initViewpager(int choice){
        pagerAdapter.initFragments2(choice,account);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(pagerAdapter.getLatsIndex());
    }
    public void updateHeader(){
        String cost = String.valueOf(pagerAdapter.getTotalCost(currentPagerPosition));
        costText.setText("支出 "+cost);
        String earn = String.valueOf(pagerAdapter.getTotalEarn(currentPagerPosition));
        earnText.setText("收入 "+earn);
        String amount = String.valueOf(pagerAdapter.getTotalEarn(currentPagerPosition).subtract(pagerAdapter.getTotalCost(currentPagerPosition)));
        amountText.setText(amount);
        accountText.setText(account);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
