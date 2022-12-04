package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import static com.hanyuzhou.accountingapp.R.drawable.baseline_attach_money_24;

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener {

    DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER){
                dialog.dismiss();
                Log.d("s","cccc");
                return false;
            }else if(keyCode == KeyEvent.KEYCODE_DEL){
                return false;
            }else{
                return true;

            }

        }
    };

    private static String TAG = "AddRecordActivity";
    private final ArrayList<String> options1Items = new ArrayList<>();
    private final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private TimePickerView pvCustomLunar;
    private LinearLayout layout_class,layout_account,layout_time,layout_member;
    private DAo dao = new DAo();
    private LinkedList<DAo> items = new LinkedList<>();

    private final ArrayList<String> fop = new ArrayList<>();
    private final ArrayList<String> num1= new ArrayList<>();
    private final ArrayList<String> num2= new ArrayList<>();
    private final ArrayList<String> num3= new ArrayList<>();
    private final ArrayList<String> num4= new ArrayList<>();
    private final ArrayList<String> num5= new ArrayList<>();



    private TextView tv_class, tv_time,tv_account,tv_member;
    private TextView amountText;
    private String userInput = "";

    private String category = "餐饮";
    private String category2 = "三餐";
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String DATATYPE = "c";
    private final String remark = category;
    private String time = "今天";
    private String Date = "1970-01-01";
    private String year = "1970";
    private String month = "1970-01";
    private String account = "现金账户";
    private String member = "本人";

    RecordBean record = new RecordBean();

    private boolean inEdit = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        util.getInstance().setContext(getApplicationContext());

        SharedPreferences preferences = getSharedPreferences("isCreated", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String Getstart = preferences.getString("Iscreated","false");


        if(Getstart.equals("false")){
            getOptionData();
        }
        editor.putString("Iscreated","true");
        editor.apply();

        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);

        layout_account=(LinearLayout) findViewById(R.id.layout_account);
        layout_time=(LinearLayout) findViewById(R.id.layout_time);
        layout_member=(LinearLayout) findViewById(R.id.layout_member);
        layout_member.setOnClickListener(this);
        layout_time.setOnClickListener(this);
        layout_account.setOnClickListener(this);

        //getSupportActionBar().setElevation(0);

        amountText = (TextView) findViewById(R.id.textView_amount);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_time.setText(DateUtil.getFormattedDate());
        tv_account = (TextView)findViewById(R.id.tv_account);
        tv_account.setText(account);
        tv_member = (TextView)findViewById(R.id.tv_member);
        tv_member.setText(member);
        Date = DateUtil.getFormattedDate();
        year = DateUtil.getYear();
        month = DateUtil.getMonth();
        tv_class.setText(category + " > " + category2);

        handleBackspace();
        handleDone();
        handleDot();
        handleTypeChange();

        initOptionPicker();
        initLunarPicker();
        GlobalUtil.getInstance().addRes();

        RecordBean record = (RecordBean) getIntent().getSerializableExtra("record");
        if (record != null) {
            inEdit = true;
            this.record = record;
        }
        layout_class=(LinearLayout)findViewById(R.id.layout_class);
        layout_class.setOnClickListener(this);
    }

    private void handleDot() {
        findViewById(R.id.keyboard_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.contains(".")) {
                    userInput += ".";
                }
            }
        });
    }

    private void handleTypeChange() {
        findViewById(R.id.keyboard_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = findViewById(R.id.keyboard_type);
                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                    button.setText("收入");
                    tv_class.setText("职业收入 > 工资");
                    record.setRemark(tv_class.getText().toString());
                    category = "职业收入";
                    category2 = "工资";
                    amountText.setTextColor(Color.rgb(250,5,53));
                    DATATYPE = "e";
                } else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                    button.setText("支出");
                    tv_class.setText("餐饮 > 三餐");
                    record.setRemark(tv_class.getText().toString());
                    category = "餐饮";
                    category2 = "餐饮";
                    amountText.setTextColor(Color.rgb(18,138,58));
                    DATATYPE = "c";
                }


            }
        });
    }

    private void handleBackspace() {
        findViewById(R.id.keyboard_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.length() > 0) {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                if (userInput.length() > 0 && userInput.charAt(userInput.length() - 1) == '.') {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                updateAmountText();
            }
        });
    }

    private void handleDone() {
        findViewById(R.id.keyboard_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.equals("")) {
                    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(userInput));
                    record.setAmount(amount);
                    if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                        record.setType(1);
                    } else {
                        record.setType(2);
                    }
                    record.setCategory(category);
                    record.setCategory2(category2);
                    record.setRemark(tv_class.getText().toString());
                    record.setDate(Date);
                    record.setYear(year);
                    record.setMonth(month);
                    record.setMember(member);
                    record.setAccount(account);
                    if (inEdit) {
                        GlobalUtil.getInstance().databaseHelper.editRecord(record.getUuid(), record);
                    } else {
                        GlobalUtil.getInstance().databaseHelper.addRecord(record);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("State","记账成功");
                    setResult(2,intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "金额不能为0", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(AddRecordActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                tv_time.setText(getTime(date));
                Date = getTime(date);
                year = getYear(date);
                month = getMonth(date);
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
    private String getYear(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }
    private String getMonth(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }


    private void initOptionPicker(){
        if(DATATYPE == "c"){
            items = util.getInstance().dbHelper.readRecords_c();
            options1Items.clear();
            for(DAo s:items){
                options1Items.add(s.getCategory());
                Log.d(TAG,s.getCategory());
            }
            options2Items.clear();
            int index;
            int i = 1;
            do{
                num1.clear();
                items = util.getInstance().dbHelper.readRecords_s("s"+i);
                for(DAo s:items){
                    num1.add(s.getCategory());
                }
                options2Items.add((ArrayList<String>) num1.clone());
                i++;
                index =util.getInstance().dbHelper.readRecords_s("s"+(i)).size();
                Log.d(TAG, String.valueOf(index));
            }while (index > 0);
        }else{
            items = util.getInstance().dbHelper.readRecords_e();
            options1Items.clear();
            for(DAo s:items){
                options1Items.add(s.getCategory());
                Log.d(TAG,s.getCategory());
            }
            options2Items.clear();
            int index;
            int i = 1;
            do{
                num1.clear();
                items = util.getInstance().dbHelper.readRecords_s("e"+i);
                for(DAo s:items){
                    num1.add(s.getCategory());
                }
                options2Items.add((ArrayList<String>) num1.clone());
                i++;
                index =util.getInstance().dbHelper.readRecords_s("e"+(i)).size();
                Log.d(TAG, String.valueOf(index));
            }while (index > 0);
        }

        Log.d(TAG, String.valueOf(options2Items));
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx1 = options1Items.get(options1);
                String tx2 = options2Items.get(options1).get(options2);
                category = tx1;
                category2 = tx2;
                tv_class.setText(tx1 + " > " + tx2);
            }
        })
                .setLayoutRes(R.layout.edit, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvde = (TextView) v.findViewById(R.id.tv_de);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                pvOptions.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                        tvde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences preferences = getSharedPreferences("isCreated", Activity.MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Isintent", "true");
                                editor.apply();
                                int flag = 1;
                                Intent intent = new Intent(AddRecordActivity.this,Class2Activity.class);
                                pvOptions.dismiss();
                                intent.putExtra("flag",flag);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .build();
        if(options1Items.size() != 0 && options2Items.size() != 0){
            pvOptions.setPicker(options1Items, options2Items);
        }

    }
    private void initOptionPicker1() {
        final ArrayList<String> op = new ArrayList<>();
        items=util.getInstance().dbHelper.readRecords_z();
        for(DAo dao:items){
            op.add(dao.getCategory());
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                String tx1 = op.get(options1);
                account = tx1;
                tv_account.setText(tx1);
            }
        })
                .setLayoutRes(R.layout.edit2, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvde = (TextView) v.findViewById(R.id.tv_de);
                        final TextView tvde2 = (TextView) v.findViewById(R.id.tv_de1);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                pvOptions.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                        tvde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CustomDialog dialog = new CustomDialog(AddRecordActivity.this,R.style.customdialogstyle);
                                dialog.setOnKeyListener(keyListener);
                                dialog.show();
                                dialog.bt_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        util.getInstance().dbHelper.removeRecordFromCategory(dialog.et_input.getText().toString());
                                        pvOptions.dismiss();
                                    }
                                });
                            }
                        });
                        tvde2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CustomDialog dialog = new CustomDialog(AddRecordActivity.this,R.style.customdialogstyle);
                                dialog.setOnKeyListener(keyListener);
                                dialog.show();
                                dialog.bt_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        DAo dAo = new DAo();
                                        dAo.setType("z");
                                        dAo.setCategory(dialog.et_input.getText().toString());
                                        util.getInstance().dbHelper.adddao(dAo);
                                        pvOptions.dismiss();
                                    }
                                });
                            }
                        });

                    }
                }).build();
        pvOptions.setPicker(op);
    }


    private void initOptionPicker2() {
        final ArrayList<String> op = new ArrayList<>();
        items=util.getInstance().dbHelper.readRecords_m();
        for(DAo dao:items){
            op.add(dao.getCategory());
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx1 = op.get(options1);
                member = tx1;
                tv_member.setText(tx1);
            }
        })
                .setLayoutRes(R.layout.edit2, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvde = (TextView) v.findViewById(R.id.tv_de);
                        final TextView tvde2 = (TextView) v.findViewById(R.id.tv_de1);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                pvOptions.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                        tvde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CustomDialog dialog = new CustomDialog(AddRecordActivity.this,R.style.customdialogstyle);
                                dialog.setOnKeyListener(keyListener);
                                dialog.show();
                                dialog.bt_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        util.getInstance().dbHelper.removeRecordFromCategory(dialog.et_input.getText().toString());
                                        pvOptions.dismiss();
                                    }
                                });
                            }
                        });
                        tvde2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CustomDialog dialog = new CustomDialog(AddRecordActivity.this,R.style.customdialogstyle);
                                dialog.setOnKeyListener(keyListener);
                                dialog.show();
                                dialog.bt_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        DAo dAo = new DAo();
                                        dAo.setType("m");
                                        dAo.setCategory(dialog.et_input.getText().toString());
                                        util.getInstance().dbHelper.adddao(dAo);
                                        pvOptions.dismiss();
                                    }
                                });
                            }
                        });

                    }
                }).build();
        pvOptions.setPicker(op);
    }

    private void getOptionData() {

        additem("c","餐饮");
        additem("s1","三餐");
        additem("s1","下午茶");
        additem("s1","水果");
        additem("s1","零食");


        additem("c","交通");
        additem("s2","公交地铁");
        additem("s2","打车");
        additem("s2","飞机火车");

        additem("c","购物");
        additem("s3","日用品");
        additem("s3","衣物");
        additem("s3","护肤美妆");
        additem("s3","饰品");
        additem("s3","电器");

        additem("e","职业收入");
        additem("e1","工资");
        additem("e1","兼职");
        additem("e1","奖金");
        additem("e","其他收入");
        additem("e2","红包");
        additem("e2","中奖");

        additem("z","现金账户");
        additem("z","金融账户");
        additem("z","虚拟账户");

        additem("m","本人");
        additem("m","父母");
        additem("m","子女");
    }
    private void additem(String type,String category){
        dao.setType(type);
        dao.setCategory(category);
        util.getInstance().dbHelper.adddao(dao);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.layout_class&&pvOptions!=null){
            initOptionPicker();

            pvOptions.show();
        }
        else if(v.getId()==R.id.layout_time){
            pvCustomLunar.show();
        }
        else if(v.getId()==R.id.layout_account){
            initOptionPicker1();
            pvOptions.show();
        }
        else if(v.getId()==R.id.layout_member){
            initOptionPicker2();
            pvOptions.show();
        }

        else {
            Button button = (Button) v;
            String input = button.getText().toString();
            if (userInput.contains("."))
            {
                if (userInput.split("\\.").length == 1 || userInput.split("\\.")[1].length() < 2)
                {
                    userInput += input;
                }
            }
            else{
                userInput += input;
            }
            updateAmountText();
        }


    }

    private void updateAmountText() {
        if (userInput.contains(".")) {
            if (userInput.split("\\.").length == 1) {
                amountText.setText(userInput + "00");
            } else if (userInput.split("\\.")[1].length() == 1) {
                amountText.setText(userInput + "0");
            } else if (userInput.split("\\.")[1].length() == 2) {
                amountText.setText(userInput);
            }
        } else {
            if (userInput.equals("")) {
                amountText.setText("0.00");
            } else {
                amountText.setText(userInput + ".00");
            }
        }
    }
    public void add(String name,String text){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences(DATATYPE,Activity.MODE_PRIVATE)
                .edit();

        editor.putString(name,text);
        editor.apply();
    }
    public void add(String name,int text){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences(DATATYPE,Activity.MODE_PRIVATE)
                .edit();

        editor.putInt(name,text);
        editor.apply();
    }
    public void add(String pacname,String name,int text){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences(pacname,Activity.MODE_PRIVATE)
                .edit();

        editor.putInt(name,text);
        editor.apply();
    }
    public void add(String pacname,String name,String text){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences(pacname,Activity.MODE_PRIVATE)
                .edit();

        editor.putString(name,text);
        editor.apply();
    }
    public String  get(String name){
        @SuppressLint("CommitPrefEdits") SharedPreferences editor = getSharedPreferences(DATATYPE,Activity.MODE_PRIVATE);

        return editor.getString(name,null);
    }
    public int  getint(String name){
        @SuppressLint("CommitPrefEdits") SharedPreferences editor = getSharedPreferences(DATATYPE,Activity.MODE_PRIVATE);

        return editor.getInt(name,0);
    }
    private void delfirst(){

    }




}
