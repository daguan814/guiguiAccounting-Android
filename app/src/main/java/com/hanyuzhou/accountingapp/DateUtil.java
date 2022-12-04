package com.hanyuzhou.accountingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class DateUtil {

    //unix time -> 11:11
    public static String getFormattedTime(long timeStamp){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timeStamp));
    }

    //2017-06-15
    public static String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    private static Date strToDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getWeekDay(String date){
        String[] weekdays = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int index = calendar.get(Calendar.DAY_OF_WEEK)-1;
        return  weekdays[index];
    }

    public static String getDateTitle(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year)+ "-" + String.valueOf(month) + "-"+String.valueOf(day);
    }

    public static String getYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return String.valueOf(year);
    }
    public static String getMonth(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        return String.valueOf(year) + "-" + String.valueOf(month);
    }

    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.valueOf(hour) + ":" + String.valueOf(minute);

    }
}
