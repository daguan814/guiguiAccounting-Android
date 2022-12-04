package com.hanyuzhou.accountingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class listadpater extends BaseAdapter {
    private LinkedList<DAo> records = new LinkedList<>();

    private LayoutInflater mInflater;
    private Context mContext;

    public listadpater(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<DAo> records){
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder3 holder;
        if (convertView == null){

            convertView = mInflater.inflate(R.layout.list_cell2,null);


            holder = new ViewHolder3();

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder3) convertView.getTag();
        }
        DAo recordBean = (DAo) getItem(position);
        holder.setViewHolder(convertView,recordBean);
        return convertView;
    }
}

class ViewHolder3 {
    TextView remarkTV;
    ImageView categoryIcon;


    public ViewHolder3() {

    }
    public void setViewHolder(View itemView, DAo record){
        remarkTV = itemView.findViewById(R.id.textView_remark);
        categoryIcon = itemView.findViewById(R.id.imageView_category);

        remarkTV.setText(record.getCategory());


    }
}

