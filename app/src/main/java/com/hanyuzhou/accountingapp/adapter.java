package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class adapter extends BaseExpandableListAdapter {

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

    private LinkedList<DAo> records = new LinkedList<>();
    private LinkedList<LinkedList<DAo>> subrecords = new LinkedList<>();

    private final LayoutInflater mInflater;
    private final Context mContext;

    public adapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<DAo> records,LinkedList<LinkedList<DAo>> subrecords){
        this.records = records;
        this.subrecords = subrecords;
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return records.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        LinkedList<DAo> tem = subrecords.get(groupPosition);
        return tem.size()+1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return records.get(groupPosition).getCategory();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subrecords.get(groupPosition).get(childPosition).getCategory();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            mconView = new conView();
            convertView = mInflater.inflate(R.layout.list_cell,null);
            mconView.remarkTV = (TextView)convertView.findViewById(R.id.textView_remark);
            mconView.imageTV = (ImageView)convertView.findViewById(R.id.plus);
            convertView.setTag(mconView);
        }else {
            mconView = (conView)convertView.getTag();
        }
        mconView.remarkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options={"删除"};
                final DAo selectedRecord = records.get(groupPosition);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.create();
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            String uuid = selectedRecord.getCategory();
                            util.getInstance().dbHelper.removeRecordFromCategory(uuid);
                            if(Activity.class.isInstance(mContext)){
                                Activity activity = (Activity)mContext;
                                activity.finish();
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });


        mconView.imageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog dialog = new CustomDialog(mContext,R.style.customdialogstyle);
                dialog.setOnKeyListener(keyListener);
                dialog.show();
                dialog.bt_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Log.d("s","sssss");
                        String str = dialog.et_input.getText().toString();
                        String CIU = "s"+(groupPosition+1);
                        DAo dAo = new DAo();
                        if(TextUtils.isEmpty(str)){
                            Toast.makeText(mContext,"请输入二级项目名称",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dAo.setType(CIU);
                            dAo.setCategory(str);
                            boolean adder = util.getInstance().dbHelper.adddao(dAo);
                            if(!adder) Toast.makeText(mContext,"该项目已存在",Toast.LENGTH_SHORT).show();
                            adapter.this.notifyDataSetChanged();
                            if(Activity.class.isInstance(mContext)){
                                Activity activity = (Activity)mContext;
                                activity.finish();
                            }
                        }

                    }
                });

            }
        });
        mconView.remarkTV.setText(getGroup(groupPosition));

        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            mconView = new conView();
            convertView = mInflater.inflate(R.layout.listview,null);
            mconView.listTV = (ListView)convertView.findViewById(R.id.list_view);
            convertView.setTag(mconView);
        }else {
            mconView = (conView) convertView.getTag();
        }
        mconView.listTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] options={"删除"};
                final DAo selectedRecord = subrecords.get(groupPosition).get(childPosition);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.create();
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            String uuid = selectedRecord.getCategory();
                            util.getInstance().dbHelper.removeRecordFromCategory(uuid);
                            if(mContext instanceof Activity){
                                Activity activity = (Activity)mContext;
                                activity.finish();
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
        listadpater ls = new listadpater(mContext);
        LinkedList<DAo> adas = new LinkedList<>();
        if(!isLastChild){
            adas.add(subrecords.get(groupPosition).get(childPosition));
            Log.d("ss", String.valueOf(subrecords.get(groupPosition).get(childPosition).getCategory()));
        }
        ls.setData(adas);


        mconView.listTV.setAdapter(ls);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    conView mconView;

    static class conView{
        ImageView imageTV;
        TextView remarkTV;
        ListView listTV;
    }
}
