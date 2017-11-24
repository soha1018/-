package com.example.errand.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.errand.bean.UserBean;
import com.example.errand.db.MyDatabase;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/11/23.
 */

public class SqlUtils {
    private static SqlUtils sqlUtils;
    private final MyDatabase db;
    private String table = "user";

    private SqlUtils(Context context) {
        db = new MyDatabase(context);
    }

    public static SqlUtils getInstance(Context context) {
        if (sqlUtils == null) {
            sqlUtils = new SqlUtils(context);
            return sqlUtils;
        } else {
            return sqlUtils;
        }
    }

    public void insert(String username, String password) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",password);
        sqLiteDatabase.insert(table, null, values);
    }

    public UserBean queryUser(String username) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor query = sqLiteDatabase.query(table, null, "username=?", new String[]{username}, null, null, null);
        UserBean bean = null;
        while (query.moveToNext()) {
            bean = new UserBean();
            bean.setUsername(query.getString(query.getColumnIndex("username")));
            bean.setPassword(query.getString(query.getColumnIndex("password")));
            Log.i(TAG, "queryUser: "+bean.toString());
        }
        query.close();

        return bean;
    }

    public void deleteUser(String username) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.delete(table, "username=?", new String[]{username});
    }
}
