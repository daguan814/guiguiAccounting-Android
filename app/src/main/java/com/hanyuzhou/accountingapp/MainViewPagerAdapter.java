package com.hanyuzhou.accountingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    LinkedList<MainFragment> fragments = new LinkedList<>();

    LinkedList<String> dates = new LinkedList<>();

    int choice;

    public MainViewPagerAdapter(FragmentManager fm, int choice) {
        super(fm);
        //this.choice = choice;
        //initFragments(choice);
    }

    public void initFragments(int choice){
        //this.choice = choice;
        fragments.clear();
        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate(choice);

        if(choice == 2 && !dates.contains(DateUtil.getYear())){
            dates.addLast(DateUtil.getYear());
        }
        if(choice == 3 && !dates.contains(DateUtil.getMonth())){
            dates.addLast(DateUtil.getMonth());
        }
        if(choice == 4 && !dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }
        for (String date:dates){
            MainFragment fragment = new MainFragment(date, choice,null);
            fragments.add(fragment);
        }
    }

    public void initFragments2(int choice, String account){
        //this.choice = choice;
        fragments.clear();
        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate2(choice,account);

        if(choice == 2 && !dates.contains(DateUtil.getYear())){
            dates.addLast(DateUtil.getYear());
        }
        if(choice == 3 && !dates.contains(DateUtil.getMonth())){
            dates.addLast(DateUtil.getMonth());
        }
        if(choice == 4 && !dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }
        for (String date:dates){
            MainFragment fragment = new MainFragment(date, choice, account);
            fragments.add(fragment);
        }
    }

    public void reload(){
        for (MainFragment fragment :
                fragments) {
            fragment.reload();
        }
    }

    public int getLatsIndex(){
        return fragments.size()-1;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
    @Override
    public long getItemId(int position){
        return fragments.get(position).hashCode();
    }

    public String getDateStr(int index){
        return dates.get(index);
    }

    public BigDecimal getTotalCost(int index){
        return fragments.get(index).getTotalCost();
    }
    public BigDecimal getTotalEarn(int index){
        return fragments.get(index).getTotalEarn();
    }

    public BigDecimal getAccountCost(int index,String account){
        return fragments.get(index).getAccountCost(account);
    }
    public BigDecimal getAccountEarn(int index,String account){
        return fragments.get(index).getAccountEarn(account);
    }
    /*public double getAmount(int index){
        double total = 0;
        for(int i = 0; i <= index; i++){
            total -= fragments.get(i).getTotalCost();
            total += fragments.get(i).getTotalEarn();
        }
        return total;
    }*/
    public BigDecimal getCost(int index){
        BigDecimal total = new BigDecimal(0);
        for(int i = 0; i <= index; i++){
            total = total.add(fragments.get(i).getTotalCost());
        }
        return total;
    }
    public BigDecimal getEarn(int index){
        BigDecimal total = new BigDecimal(0);
        for(int i = 0; i <= index; i++){
            total = total.add(fragments.get(i).getTotalEarn());
        }
        return total;
    }
    public float getPie(int index, String category){
        float amount = 0;
        for(int i = 0; i <= index; i++){
            amount += fragments.get(i).getPie(category);
        }
        return amount;
    }
    public float getPie_earn(int index, String category){
        float amount = 0;
        for(int i = 0; i <= index; i++){
            amount += fragments.get(i).getPie_earn(category);
        }
        return amount;
    }
    public ArrayList<String> get_category(int index){
        ArrayList<String> categorys = new ArrayList<>();
        for(int i = 0; i <= index; i++){
            categorys.addAll(fragments.get(i).getcategory());
        }
        return categorys;
    }
    public ArrayList<String> get_category2(int index){
        ArrayList<String> categorys = new ArrayList<>();
        for(int i = 0; i <= index; i++){
            categorys.addAll(fragments.get(i).getcategory2());
        }
        return categorys;
    }
    public ArrayList<String> get_member(int index){
        ArrayList<String> member = new ArrayList<>();
        for(int i = 0; i <= index; i++){
            member.addAll(fragments.get(i).getmember());
        }
        return member;
    }
    public ArrayList<String> get_account(int index){
        ArrayList<String> member = new ArrayList<>();
        for(int i = 0; i <= index; i++){
            member.addAll(fragments.get(i).getaccount());
        }
        return member;
    }
}
