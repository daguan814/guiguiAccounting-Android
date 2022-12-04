package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.LinkedList;

public class StatisticsActivity extends AppCompatActivity{
    private static final String TAG = "s";
    private MainViewPagerAdapter pagerAdapter;
    private TextView amountText, costText, earnText;
    private ListView listView;
    private LinkedList<RecordBean> recordBeans = new LinkedList<>();
    private String accountStr = "现金账户";
    private LinkedList<String> accounts = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        util.getInstance().setContext(StatisticsActivity.this);
        super.onCreate(savedInstanceState);
        GlobalUtil.getInstance().setContext(getApplicationContext());
        setContentView(R.layout.activity_statistics);
        //getSupportActionBar().setElevation(0);

        LinkedList<DAo> preinit = util.getInstance().dbHelper.readRecords_z();

        for(DAo dAo:preinit){
            GlobalUtil.getInstance().init_account(dAo.getCategory());
        }


        amountText = (TextView)findViewById(R.id.amount_num);
        costText = (TextView)findViewById(R.id.cost);
        earnText = (TextView)findViewById(R.id.earn);

        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),1);
        pagerAdapter.initFragments(1);
        pagerAdapter.notifyDataSetChanged();
        recordBeans = GlobalUtil.getInstance().databaseHelper.read_allRecords();

        accounts = GlobalUtil.getInstance().accounts;
        listView = (ListView)findViewById(R.id.account_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return accounts.size();
            }

            @Override
            public Object getItem(int i) {
                return accounts.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder2 holder;
                if(view == null){
                    LayoutInflater inflater = StatisticsActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.cell_account,null);
                    holder = new ViewHolder2();
                    view.setTag(holder);
                }
                else {
                    holder = (ViewHolder2) view.getTag();
                }
                String account = accounts.get(i);
                holder.setViewHolder(view,pagerAdapter,account);

                return view;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String account_click = accounts.get(i);
                Intent intent = new Intent();
                intent.setClass(StatisticsActivity.this,cash_account.class);
                intent.putExtra("account",account_click);
                startActivity(intent);
            }
        });

        update();
    }

    public void update(){
        int index = pagerAdapter.getLatsIndex();
        String amount = String.valueOf(pagerAdapter.getEarn(index).subtract(pagerAdapter.getCost(index)));
        String cost = String.valueOf(pagerAdapter.getCost(index));
        String earn = String.valueOf(pagerAdapter.getEarn(index));
        amountText.setText(amount);
        costText.setText("支出 "+cost);
        earnText.setText("收入 "+earn);
    }
}