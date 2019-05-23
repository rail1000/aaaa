package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TB_NAME = "tb_rates";
    private static final int VERSION = 1;
    private static final String DB_NAME = "myrate.db";
    //private static final String TB_NAME = ;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DB_NAME, null, VERSION);
    }

    public DBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE "+TB_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CURNAME TEXT, CURRATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
