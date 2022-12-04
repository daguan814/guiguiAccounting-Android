package com.hanyuzhou.accountingapp;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph_cost extends Fragment implements View.OnClickListener {
    private PieChart pieChart;
    private TextView selected_pie;
    private ListView listView;
    private Spinner spinner;
    private TextView start_tv,end_tv;
    private View rootView;
    private TimePickerView pvCustomLunar;
    private String start_date = "2020-10-04",end_date = "2020-10-06";
    private LinkedList<RecordBean> recordBeans = new LinkedList<>();
    private LinkedList<RecordBean> recordBeans1 = new LinkedList<>();
    private ArrayList<String> str = MainActivity.getMainActivity().pullcategory();
    private LinkedList<String> date_list = GlobalUtil.getInstance().databaseHelper.getAvailableDate(4);
    List<PieEntry> list;
    private int select = 0;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start_tv.setText(date_list.get(0));
        end_tv.setText(DateUtil.getFormattedDate());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String select_item = adapterView.getItemAtPosition(i).toString();
                switch (select_item){
                    case "一级分类":
                        str = MainActivity.getMainActivity().pullcategory();
                        select = 0;
                        init();
                        break;
                    case "二级分类":
                        str = MainActivity.getMainActivity().pullcategory2();
                        select = 1;
                        init();
                        break;
                    case "成员":
                        str = MainActivity.getMainActivity().pullmember();
                        select = 2;
                        init();
                        break;
                    case "账户":
                        str = MainActivity.getMainActivity().pullaccount();
                        select = 3;
                        init();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_graph_cost,null);
        pieChart = (PieChart)rootView.findViewById(R.id.pie_chart);
        selected_pie = (TextView)rootView.findViewById(R.id.selected_pie);
        listView = (ListView)rootView.findViewById(R.id.cost_listview);
        spinner = (Spinner)rootView.findViewById(R.id.cost_spinner);
        start_tv = (TextView)rootView.findViewById(R.id.start);
        start_tv.setOnClickListener(this);
        end_tv = (TextView)rootView.findViewById(R.id.end);
        end_tv.setOnClickListener(this);
        return rootView;
    }

    public static ArrayList getSingle(ArrayList list) {
        ArrayList newList = new ArrayList();     //创建新集合
        Iterator it = list.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            Object obj = it.next();       //记录每一个元素
            if (!newList.contains(obj)) {      //如果新集合中不包含旧集合中的元素
                newList.add(obj);       //将元素添加
            }
        }
        return newList;
    }

    public void init(){
        BigDecimal amount = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        LinkedList<RecordBean> total_record = new LinkedList<>();
        start_date = start_tv.getText().toString();
        end_date = end_tv.getText().toString();
        total_record = GlobalUtil.getInstance().databaseHelper.read_cost_date(start_date,end_date);

        for(RecordBean recordBean:total_record){
            total = total.add(recordBean.getAmount());
        }
        float total_float = Float.valueOf(String.valueOf(total));
        float amount_float = 0;

        float rate = 0;
        str = getSingle(str);
        list = new ArrayList<>();

        //颜色集合
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(c);
        }
        for (int c: ColorTemplate.COLORFUL_COLORS){
            colors.add(c);
        }

        //数据
        for(int i = 0; i < str.size(); i++){
            amount = new BigDecimal(0);
            if(select == 0){
                recordBeans = GlobalUtil.getInstance().databaseHelper.select_by_date_cost(str.get(i),start_date,end_date);
            }
            else if(select == 1){
                recordBeans = GlobalUtil.getInstance().databaseHelper.select_by_date_cost2(str.get(i),start_date,end_date);
            }
            else if(select == 2){
                recordBeans = GlobalUtil.getInstance().databaseHelper.read_by_member_cost(str.get(i),start_date,end_date);
            }
            else {
                recordBeans = GlobalUtil.getInstance().databaseHelper.select_by_account_cost(str.get(i),start_date,end_date);
            }
            for(RecordBean record:recordBeans){
                amount = amount.add(record.getAmount());
            }
            amount_float = Float.valueOf(String.valueOf(amount));
            if(amount_float != 0){
                rate = 1000f*amount_float/1000f*total_float;
                PieEntry pieEntry = new PieEntry(rate,str.get(i));
                pieEntry.setX(amount_float);
                list.add(pieEntry);
            }
        }
        //中心文字
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("共计：￥" + total);
        pieChart.setCenterTextColor(Color.DKGRAY);
        pieChart.setCenterTextSize(20);

        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.setEntryLabelTextSize(15);
        //不显示description
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        PieDataSet pieDataSet = new PieDataSet(list, "");
        //不显示图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        //设置颜色
        pieDataSet.setColors(colors);
        //设置饼块之间的间距
        pieDataSet.setSliceSpace(2f);
        //选中突出
        pieDataSet.setHighlightEnabled(true);

        PieData pieData = new PieData(pieDataSet);
        //设置字体颜色大小
        pieData.setDrawValues(true);
        pieData.setValueTextSize(15);
        pieData.setValueTextColor(Color.DKGRAY);
        pieData.setValueFormatter(new PercentFormatter());

        //动画
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        pieChart.setData(pieData);
        pieChart.setExtraOffsets(25,5,25,5);
        pieChart.postInvalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pieEntry = (PieEntry) e;
                String category = pieEntry.getLabel();
                float amount = pieEntry.getX();
                selected_pie.setText(category + ":￥" + amount);
                if(select == 0){
                    recordBeans1 = GlobalUtil.getInstance().databaseHelper.select_by_date_cost(category,start_date,end_date);
                }
                else if(select == 1){
                    recordBeans1 = GlobalUtil.getInstance().databaseHelper.select_by_date_cost2(category,start_date,end_date);
                }
                else if(select == 2){
                    recordBeans1 = GlobalUtil.getInstance().databaseHelper.read_by_member_cost(category,start_date,end_date);
                }
                else {
                    recordBeans1 = GlobalUtil.getInstance().databaseHelper.select_by_account_cost(category,start_date,end_date);
                }
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return recordBeans1.size();
                    }

                    @Override
                    public Object getItem(int i) {
                        return recordBeans1.get(i);
                    }

                    @Override
                    public long getItemId(int i) {
                        return i;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        ViewHolder holder;
                        if(view == null){
                            LayoutInflater inflater = Graph_cost.this.getLayoutInflater();
                            view = inflater.inflate(R.layout.cell_list_view,null);
                            holder = new ViewHolder();
                            view.setTag(holder);
                        }
                        else {
                            holder = (ViewHolder) view.getTag();
                        }
                        RecordBean recordBean = recordBeans1.get(i);
                        holder.setViewHolder(view,recordBean);
                        TextView day = (TextView)view.findViewById(R.id.textView_time);
                        day.setText(recordBean.getDate());
                        return view;
                    }
                });
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.start){
            initLunarPicker(1);
            pvCustomLunar.show();
            //init();
        }
        if(view.getId() == R.id.end){
            initLunarPicker(2);
            pvCustomLunar.show();
            //init();
        }
    }

    private void initLunarPicker(final int choice) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(rootView.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(rootView.getContext(), getTime(date), Toast.LENGTH_SHORT).show();
                if(choice == 1){
                    start_tv.setText(getTime(date));
                }
                if(choice == 2) {
                    end_tv.setText(getTime(date));
                }
                init();
                //tv_time.setText(getTime(date));

            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new CustomListener() {

                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();
                                pvCustomLunar.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });
                        //公农历切换
                        CheckBox cb_lunar = (CheckBox) v.findViewById(R.id.cb_lunar);
                        cb_lunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                pvCustomLunar.setLunarCalendar(!pvCustomLunar.isLunarCalendar());
                                //自适应宽
                                setTimePickerChildWeight(v, isChecked ? 0.8f : 1f, isChecked ? 1f : 1.1f);
                            }
                        });

                    }

                    /**
                     * 公农历切换后调整宽
                     * @param v
                     * @param yearWeight
                     * @param weight
                     */
                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
                        ViewGroup timePicker = (ViewGroup) v.findViewById(R.id.timepicker);
                        View year = timePicker.getChildAt(0);
                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
                        lp.weight = yearWeight;
                        year.setLayoutParams(lp);
                        for (int i = 1; i < timePicker.getChildCount(); i++) {
                            View childAt = timePicker.getChildAt(i);
                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt.getLayoutParams());
                            childLp.weight = weight;
                            childAt.setLayoutParams(childLp);
                        }
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}