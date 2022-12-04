package com.hanyuzhou.accountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

public class DBhelper extends SQLiteOpenHelper {
    private String TAG ="DBHelper";

    public static String DB_NAME = "MyDB";
    private static final String CREATE_RECORD_DB = "create table MyDB ("
            + "id integer primary key autoincrement, "
            + "type, "
            + "category) ";


    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean adddao(DAo dAo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        LinkedList<DAo> dAo1 = readRecords_s(dAo.getType());
        for(DAo dAo2 :dAo1){
            if(dAo2.getCategory().equals(dAo.getCategory())){
                return false;
            }
        }
        values.put("type",dAo.getType());
        values.put("category",dAo.getCategory());
        db.insert(DB_NAME,null,values);
        values.clear();
        return true;
    }
    public void editRecord(String uuid,DAo record){
        removeRecordFromCategory(uuid);

        adddao(record);
    }


    public void  removeRecordFromTpye(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"type = ?",new String[]{type});
    }

    public void  removeRecordFromCategory(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"category = ?",new String[]{category});
    }

    public LinkedList<DAo> readRecords_z() {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB where type = ?", new String[]{"z"});
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<DAo> readRecords_m() {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB where type = ?", new String[]{"m"});
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }


    public LinkedList<DAo> readRecords() {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB", null);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<DAo> readRecords_c() {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB where type = ?", new String[]{"c"});
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }
    public LinkedList<DAo> readRecords_s(String types) {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB where type = ?", new String[]{types});
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<DAo> readRecords_e() {
        LinkedList<DAo> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select DISTINCT * from MyDB where type = ?", new String[]{"e"});
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                DAo record = new DAo();
                record.setType(type);
                record.setCategory(category);
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }
}
