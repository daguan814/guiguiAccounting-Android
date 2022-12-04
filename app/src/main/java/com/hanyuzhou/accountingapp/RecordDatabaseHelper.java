package com.hanyuzhou.accountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.YuvImage;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class RecordDatabaseHelper extends SQLiteOpenHelper implements account {

    private String TAG ="RecordDatabaseHelper";

    public static String DB_NAME = "Record";

    private static final String CREATE_RECORD_DB = "create table Record ("
        + "id integer primary key autoincrement, "
        + "uuid text, "
        + "type integer, "
        + "category, "
        + "category2,"
        + "remark text, "
        + "amount double, "
        + "time integer, "
        + "date date, "
        + "year, "
        + "account, "
        + "member, "
        + "month  ) ";

    public RecordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRecord(RecordBean bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid",bean.getUuid());
        values.put("type",bean.getType());
        values.put("category",bean.getCategory());
        values.put("category2",bean.getCategory2());
        values.put("remark",bean.getRemark());
        values.put("amount",String.valueOf(bean.getAmount()));
        values.put("date",bean.getDate());
        values.put("time",bean.getTimeStamp());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("account",bean.getAccount());
        values.put("member",bean.getMember());
        db.insert(DB_NAME,null,values);
        values.clear();
        Log.d(TAG,bean.getUuid()+"added");
    }

    public void  removeRecord(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"uuid = ?",new String[]{uuid});
    }

    public void editRecord(String uuid,RecordBean record){
        removeRecord(uuid);
        record.setUuid(uuid);
        addRecord(record);
    }

    @Override
    public void setDbName(String db_NAME){
        DB_NAME = db_NAME;
    }

    @Override
    public void DeletDatabase(String db_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(db_NAME,null,null);
        db.close();
        File file1 = new File("/data/data/cn.qiuzhping.study/databases/"+db_NAME);
        deleteFile(file1);
        File file2 = new File("/data/data/cn.qiuzhping.study/databases/"+db_NAME+"-journal");
        deleteFile(file2);
    }

    public void deleteFile(File file){
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                // 设置属性:让文件可执行，可读，可写
                file.setExecutable(true, false);
                file.setReadable(true, false);
                file.setWritable(true, false);
                file.delete(); // delete()方法
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.setExecutable(true, false);
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.delete();
            Log.i("deleteFile", file.getName() + "成功删除！！");
        } else {
            Log.i("deleteFile", file.getName() + "不存在！！！");
        }

    }

    public LinkedList<RecordBean> readRecords(String dateStr, String accountStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if(accountStr == null){
            cursor = db.rawQuery("select DISTINCT * from Record where date = ? order by date desc",new String[]{dateStr});
        }
        else {
            cursor = db.rawQuery("select DISTINCT * from Record where date = ? and account = ? order by date desc",new String[]{dateStr,accountStr});
        }
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> read_cost_date(String start,String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where type = 1 and date between ? and ? order by date desc",new String[]{start,end});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> read_earn_date(String start,String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where type = 2 and date between ? and ? order by date desc",new String[]{start,end});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> read_by_member_cost(String categoryStr,String start,String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where member = ? and type = 1 and date between ? and ? order by date desc",new String[]{categoryStr,start,end});
        get_record(cursor,records);
        return records;
    }
    public LinkedList<RecordBean> read_by_member_earn(String categoryStr,String start, String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where member = ? and type = 2 and date between ? and ? order by date desc",new String[]{categoryStr,start,end});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_month(String monthStr,String accountStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if(accountStr == null){
            cursor = db.rawQuery("select DISTINCT * from Record where month = ? order by date desc",new String[]{monthStr});
        }
        else {
            cursor = db.rawQuery("select DISTINCT * from Record where month = ? and account = ? order by date desc",new String[]{monthStr,accountStr});
        }
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_year(String yearStr, String accountStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if(accountStr == null){
            cursor = db.rawQuery("select DISTINCT * from Record where year = ? order by date desc",new String[]{yearStr});
        }
        else {
            cursor = db.rawQuery("select DISTINCT * from Record where year = ? and account = ? order by date desc",new String[]{yearStr,accountStr});
        }
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> read_allRecords(){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date desc",null);
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_account(String accountStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? order by date desc",new String[]{accountStr});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_account_cost(String accountStr,String start, String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? and type = 1 and date between ? and ? order by date desc",new String[]{accountStr,start,end});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_account_earn(String accountStr,String start, String end){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? and type = 2 and date between ? and ? order by date desc",new String[]{accountStr,start,end});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_date_cost(String accountStr, String start_date, String end_date){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where category = ? and type = 1 and date between ? and ? order by date desc",new String[]{accountStr,start_date,end_date});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_date_earn(String accountStr, String start_date, String end_date){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where category = ? and type = 2 and date between ? and ? order by date desc",new String[]{accountStr,start_date,end_date});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_date_earn2(String accountStr, String start_date, String end_date){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where category2 = ? and type = 2 and date between ? and ? order by date desc",new String[]{accountStr,start_date,end_date});
        get_record(cursor,records);
        return records;
    }

    public LinkedList<RecordBean> select_by_date_cost2(String accountStr, String start_date, String end_date){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where category2 = ? and type = 1 and date between ? and ? order by date desc",new String[]{accountStr,start_date,end_date});
        get_record(cursor,records);
        return records;
    }

    public void get_record(Cursor cursor, LinkedList<RecordBean> records){
        if (cursor.moveToFirst()){
            do{
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String category2 = cursor.getString(cursor.getColumnIndex("category2"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String amount = cursor.getString(cursor.getColumnIndex("amount"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String member = cursor.getString(cursor.getColumnIndex("member"));

                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setCategory2(category2);
                record.setRemark(remark);
                record.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));
                record.setDate(date);
                record.setTimeStamp(timeStamp);
                record.setAccount(account);
                record.setMember(member);

                records.add(record);

            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public LinkedList<String> getAvailableDate(int choice){

        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        if(choice == 1){
            dates.add("all records");
        }
        else if(choice == 2){
            Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc",new String[]{});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("year"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        else if(choice == 3){
            Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc",new String[]{});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("month"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        else {
            Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc",new String[]{});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return dates;
    }

    public LinkedList<String> getAvailableDate2(int choice,String accountStr){

        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        if(choice == 1){
            dates.add("all records");
        }
        else if(choice == 2){
            Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? order by date asc",new String[]{accountStr});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("year"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        else if(choice == 3){
            Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? order by date asc",new String[]{accountStr});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("month"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        else {
            Cursor cursor = db.rawQuery("select DISTINCT * from Record where account = ? order by date asc",new String[]{accountStr});
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if (!dates.contains(date)){
                        dates.add(date);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return dates;
    }

}
