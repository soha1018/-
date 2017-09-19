package com.example.traffic8_29.Engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.traffic8_29.Bean.EtcBean;
import com.example.traffic8_29.Bean.SenseBean;
import com.example.traffic8_29.Bean.UserBean;
import com.example.traffic8_29.SqlDB.MySql;

import java.util.ArrayList;

/**
 * 数据库的工具类
 * Created by Administrator on 2017/8/29.
 */

public class SqlEngine {

    private final MySql sql;
    private String tableEtc = "etc";
    private String tableInfo = "info";
    private String tableSense = "sense";

    private SqlEngine(Context context) {
        sql = new MySql(context);
    }

    private static SqlEngine sqlEngine = null;

    public static SqlEngine getInstance(Context context) {
        if (sqlEngine == null) {
            sqlEngine = new SqlEngine(context);
            return sqlEngine;
        } else {
            return sqlEngine;
        }
    }

    public void insertETC(int number, int money, String user, long time) {
        SQLiteDatabase database = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("money", money);
        values.put("user", user);
        values.put("time", time);
        database.insert(tableEtc, null, values);
        database.close();
    }

    public ArrayList<EtcBean> queryETC() {
        SQLiteDatabase database = sql.getReadableDatabase();
        Cursor query = database.query(tableEtc, null, null, null, null, null, null);
        ArrayList<EtcBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            EtcBean bean = new EtcBean();
            bean.number = query.getInt(query.getColumnIndex("number"));
            bean.money = query.getInt(query.getColumnIndex("money"));
            bean.user = query.getString(query.getColumnIndex("user"));
            bean.time = query.getLong(query.getColumnIndex("time"));
            list.add(bean);
        }

        database.close();
        return list;
    }

    public void insertInfo(String user, String pwd, String phone) {
        SQLiteDatabase database = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("pwd", pwd);
        values.put("phone", phone);
        database.insert(tableInfo, null, values);
        database.close();
    }

    public ArrayList<UserBean> queryInfo() {
        SQLiteDatabase database = sql.getReadableDatabase();
        Cursor query = database.query(tableInfo, null, null, null, null, null, null);
        UserBean bean;
        ArrayList<UserBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            bean = new UserBean();
            bean.setUser(query.getString(query.getColumnIndex("user")));
            bean.setPhone(query.getString(query.getColumnIndex("phone")));
            bean.setPwd(query.getString(query.getColumnIndex("pwd")));
            list.add(bean);
        }
        database.close();
        return list;
    }

    public void insertSense(String name, int number, String status, long time) {
        SQLiteDatabase db = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("number", number);
        values.put("status", status);
        values.put("time", time);
        db.insert(tableSense, null, values);
        db.close();
    }

    public ArrayList<SenseBean> querySense(String name,long time) {
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor query = db.query(tableSense, null, "name=? and time>?", new String[]{name, String.valueOf(time)}, null, null, null);
        ArrayList<SenseBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            SenseBean bean = new SenseBean();
            bean.name = query.getString(query.getColumnIndex("name"));
            bean.status = query.getString(query.getColumnIndex("status"));
            bean.number = query.getInt(query.getColumnIndex("number"));
            bean.time = query.getLong(query.getColumnIndex("time"));
            list.add(bean);
        }
        db.close();
        return list;
    }
}
