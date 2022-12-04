package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

public  class  Class2Activity extends AppCompatActivity implements View.OnClickListener {
    private LinkedList<DAo> dAos = new LinkedList<>();
    private final LinkedList<Fragment> list = new LinkedList<>();
    private Button tv_finish;
    private TextView tv_cost,tv_earn;
    private LinearLayout layout_cost,layout_earn;
    private View view_cost, view_earn;
    private ViewPager viewPager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class2);
        viewPager = (ViewPager) findViewById(R.id.inverterViewPager);
        tv_cost = (TextView)findViewById(R.id.cost_graph);
        tv_earn = (TextView)findViewById(R.id.earn_graph);
        view_cost = findViewById(R.id.view_cost);
        view_earn = findViewById(R.id.view_earn);

        layout_cost = (LinearLayout)findViewById(R.id.cost_fragment);
        layout_cost.setOnClickListener(this);
        layout_earn = (LinearLayout)findViewById(R.id.earn_fragment);
        layout_earn.setOnClickListener(this);

        LinkedList<DAo> c1 = util.getInstance().dbHelper.readRecords_c();
        LinkedList<LinkedList<DAo>> bbb = new LinkedList<>();
        LinkedList<DAo> cord ;

        c1 = util.getInstance().dbHelper.readRecords_c();
        bbb.clear();
        int i =1;
        while (util.getInstance().dbHelper.readRecords_s("s"+i).size()>0){
            cord = util.getInstance().dbHelper.readRecords_s("s"+i);
            bbb.add((LinkedList<DAo>) cord.clone());
            i++;
        }
        list.add(new MyFragment(c1, bbb));

        LinkedList<LinkedList<DAo>> aaa = new LinkedList<>();
        c1 = util.getInstance().dbHelper.readRecords_e();
        aaa.clear();
        i =1;
        while (util.getInstance().dbHelper.readRecords_s("e"+i).size()>0){
            cord = util.getInstance().dbHelper.readRecords_s("e"+i);
            aaa.add((LinkedList<DAo>) cord.clone());
            i++;
        }
        list.add(new MyFragment_earn(c1,aaa));
        FragmentAdpater adapter = new FragmentAdpater(getFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            /**
             * pagerView被切换后自动执行的方法
             */
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_cost.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color11));
                        view_cost.setVisibility(View.VISIBLE);
                        tv_earn.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color6));
                        view_earn.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        tv_cost.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color6));
                        view_cost.setVisibility(View.INVISIBLE);
                        tv_earn.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color11));
                        view_earn.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(adapter.getLatsIndex());


        tv_finish = (Button) findViewById(R.id.tv_finish);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cost_fragment:
                viewPager.setCurrentItem(0,true);
                tv_cost.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color11));
                view_cost.setVisibility(View.VISIBLE);
                tv_earn.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color6));
                view_earn.setVisibility(View.INVISIBLE);
                break;

            case R.id.earn_fragment:
                viewPager.setCurrentItem(1,true);
                tv_cost.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color6));
                view_cost.setVisibility(View.INVISIBLE);
                tv_earn.setTextColor(ContextCompat.getColor(Class2Activity.this, R.color.color11));
                view_earn.setVisibility(View.VISIBLE);
                break;
        }
    }
}