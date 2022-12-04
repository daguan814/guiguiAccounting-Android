package com.hanyuzhou.accountingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {

    private LinkedList<RecordBean> records = new LinkedList<>();

    private LayoutInflater mInflater;
    private Context mContext;

    public ListViewAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<RecordBean> records){
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
        ViewHolder holder;
        if (convertView == null){

            convertView = mInflater.inflate(R.layout.cell_list_view,null);


            holder = new ViewHolder();

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecordBean recordBean = (RecordBean) getItem(position);
        holder.setViewHolder(convertView,recordBean);
        return convertView;
    }
}

class ViewHolder{
    TextView remarkTV;
    TextView amountTV;
    TextView timeTV;
    ImageView categoryIcon;
    TextView accountTV;
    TextView memberTV;

    public ViewHolder(){

    }

    public void setViewHolder(View itemView, RecordBean record){
        remarkTV = itemView.findViewById(R.id.textView_remark);
        amountTV = itemView.findViewById(R.id.textView_amount);
        timeTV = itemView.findViewById(R.id.textView_time);
        accountTV = itemView.findViewById(R.id.textView_account);
        categoryIcon = itemView.findViewById(R.id.imageView_category);
        memberTV = itemView.findViewById(R.id.textView_member);

        remarkTV.setText(record.getCategory() + " > " + record.getCategory2());

        if (record.getType() == 1){
            amountTV.setText("- "+record.getAmount());
        } else {
            amountTV.setText("+ "+record.getAmount());
        }
        accountTV.setText(record.getAccount());
        timeTV.setText(record.getDate());
        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
        memberTV.setText(record.getMember());
    }

}
class ViewHolder2{
    TextView accountTV;
    TextView costTV;
    TextView earnTV;
    TextView totalTV;

    public ViewHolder2(){

    }

    public void setViewHolder(View itemView, MainViewPagerAdapter pagerAdapter, String account){
        int index = pagerAdapter.getLatsIndex();
        accountTV = itemView.findViewById(R.id.accountTV);
        costTV = itemView.findViewById(R.id.costTV);
        earnTV = itemView.findViewById(R.id.earnTV);
        totalTV = itemView.findViewById(R.id.totalTV);
        String amount = String.valueOf(pagerAdapter.getAccountEarn(index,account).subtract(pagerAdapter.getAccountCost(index,account)));
        String cost = String.valueOf(pagerAdapter.getAccountCost(index, account));
        String earn = String.valueOf(pagerAdapter.getAccountEarn(index, account));
        accountTV.setText(account);
        costTV.setText("支出 " + cost);
        earnTV.setText("收入 " + earn);
        totalTV.setText(amount);

    }

}
