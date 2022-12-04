package com.hanyuzhou.accountingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_cost,tv_earn;
    private LinearLayout layout_cost,layout_earn;
    private View view_cost, view_earn;
    private ViewPager viewPager;
    private List<Fragment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        viewPager = (ViewPager)findViewById(R.id.inverterViewPager);
        tv_cost = (TextView)findViewById(R.id.cost_graph);
        tv_earn = (TextView)findViewById(R.id.earn_graph);
        layout_cost = (LinearLayout)findViewById(R.id.cost_fragment);
        layout_cost.setOnClickListener(this);
        layout_earn = (LinearLayout)findViewById(R.id.earn_fragment);
        layout_earn.setOnClickListener(this);
        view_cost = findViewById(R.id.view_cost);
        view_earn = findViewById(R.id.view_earn);
        list.add(new Graph_cost());
        list.add(new Graph_earn());
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), list);
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
                        tv_cost.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color11));
                        view_cost.setVisibility(View.VISIBLE);
                        tv_earn.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color6));
                        view_earn.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        tv_cost.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color6));
                        view_cost.setVisibility(View.INVISIBLE);
                        tv_earn.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color11));
                        view_earn.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.cost_fragment:
                viewPager.setCurrentItem(0,true);
                tv_cost.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color11));
                view_cost.setVisibility(View.VISIBLE);
                tv_earn.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color6));
                view_earn.setVisibility(View.INVISIBLE);
                break;

            case R.id.earn_fragment:
                viewPager.setCurrentItem(1,true);
                tv_cost.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color6));
                view_cost.setVisibility(View.INVISIBLE);
                tv_earn.setTextColor(ContextCompat.getColor(GraphActivity.this, R.color.color11));
                view_earn.setVisibility(View.VISIBLE);
                break;
        }
    }
    /**
     * 定义FragmentPager适配器类
     */
    class MyAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> fragments;//fragment管理员
        private FragmentManager fm;//fragment数组

        /**
         * 构造函数
         * @param fm fragment管理员对象
         * @param fragmentList fragment数组
         */
        public MyAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragments=fragmentList;
            this.fm=fm;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
