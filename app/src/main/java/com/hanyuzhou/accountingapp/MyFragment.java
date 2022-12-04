package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class MyFragment extends Fragment implements View.OnClickListener {
    private LinkedList<DAo> records = new LinkedList<>();
    private LinkedList<LinkedList<DAo>> subrecords = new LinkedList<>();
    private View rootView;
    private ExpandableListView listView;
    private adapter adapter;
    private TextView day_text;

    public MyFragment(LinkedList<DAo> record,LinkedList<LinkedList<DAo>> subrecords ){
        this.records = record;
        this.subrecords = subrecords;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my,container,false);
        initView();
        return rootView;
    }
    private void initView() {

        listView = (ExpandableListView) rootView.findViewById(R.id.listView);
        day_text = (TextView)rootView.findViewById(R.id.add_text);
        day_text.setOnClickListener(this);
        adapter = new adapter(getContext());
        adapter.setData(records,subrecords);
        listView.setAdapter(adapter);
    }


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

    @Override
    public void onClick(View v) {
        final CustomDialog2 dialog = new CustomDialog2(rootView.getContext(),R.style.customdialogstyle);
        dialog.setOnKeyListener(keyListener);
        dialog.show();
        dialog.bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d("s","sssss");
                String str = dialog.et_input1.getText().toString();
                String str1 = dialog.et_input.getText().toString()==null?"default":dialog.et_input.getText().toString();
                String CIU = "c";
                DAo dAo = new DAo();
                dAo.setType(CIU);
                dAo.setCategory(str);
                boolean adder = util.getInstance().dbHelper.adddao(dAo);
                int i =1;
                do{
                    i++;
                }while (util.getInstance().dbHelper.readRecords_s("s"+i).size()>0);
                DAo dAo1 = new DAo();
                dAo1.setType("s"+i);
                dAo1.setCategory(str1);
                util.getInstance().dbHelper.adddao(dAo1);

                if(!adder) Toast.makeText(rootView.getContext(),"该项目已存在",Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                if(rootView.getContext() instanceof Activity){
                    Activity activity = (Activity)rootView.getContext();
                    activity.finish();
                }
            }
        });
    }
}