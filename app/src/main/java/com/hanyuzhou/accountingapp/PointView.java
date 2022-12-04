package com.hanyuzhou.accountingapp;

import android.graphics.Point;

public class PointView extends Point {
    public int index;


    public PointView(int x, int y) {
        super(x, y);
    }
    public int getIndex(){
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
