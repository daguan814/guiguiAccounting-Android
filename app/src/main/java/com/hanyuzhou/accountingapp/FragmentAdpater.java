package com.hanyuzhou.accountingapp;



import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;


import java.util.LinkedList;

public class FragmentAdpater extends FragmentPagerAdapter {
    LinkedList<Fragment> fragments = new LinkedList<>();
    private FragmentManager fm;

    public FragmentAdpater(FragmentManager fm, LinkedList<Fragment> fragmentList) {
        super(fm);
        this.fragments=fragmentList;
        this.fm=fm;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        return POSITION_NONE;
    }
    @Override
    public long getItemId(int position){
        return fragments.get(position).hashCode();
    }

    public int getLatsIndex(){
        return fragments.size()-1;
    }
}
