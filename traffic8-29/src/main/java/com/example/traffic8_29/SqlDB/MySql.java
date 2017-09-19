package com.example.traffic8_29.SqlDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MySql extends SQLiteOpenHelper {
    public MySql(Context context) {
        super(context, "MySql.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table etc(_id Integer primary key autoincrement,number varchar(10),money varchar(20)," +
                "user varchar(40),time varchar(50))");
        db.execSQL("create table info(_id Integer primary key autoincrement,user varchar(30),pwd varchar(30)," +
                "phone varchar(30))");
        db.execSQL("create table sense(_id Integer primary key autoincrement,name varchar(30),number varchar(30)," +
                "status varchar(30),time varchar(40))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
