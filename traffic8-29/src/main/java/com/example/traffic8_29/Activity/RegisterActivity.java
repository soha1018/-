package com.example.traffic8_29.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.R;

/**
 * 注册界面
 * Created by Administrator on 2017/9/1.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_user;
    private EditText et_pwd;
    private EditText et_pwds;
    private EditText et_phone;
    private Button btn_cancel;
    private Button btn_yes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void isEdit() {
        String user = getEditText(et_user);
        String pwd = getEditText(et_pwd);
        String pwds = getEditText(et_pwds);
        String phone = getEditText(et_phone);
        if (isEmpty(user, "请输入用户名")) {
            return;
        }
        if (isEmpty(pwd, "请输入密码")) {
            return;
        }
        if (isEmpty(pwds, "请输入确认密码")) {
            return;
        }
        if (isEmpty(phone, "请输入手机号码")) {
            return;
        }
        if (isMatches(user, "^[a-zA-Z]{5,12}$", "账户格式不正确")) {
            return;
        }
        if (isMatches(pwd, "^[a-zA-Z0-9]{5,8}$", "密码格式不正确")) {
            return;
        }
        if (isMatches(pwds, "^[a-zA-Z0-9]{5,8}$", "密码格式不正确")) {
            return;
        }
        if (isMatches(phone, "^(13[0-9]|14[57]|15[^4]|18[0,5-9])\\d{8}$", "手机格式不正确")) {
            return;
        }
        if (!pwd.equals(pwds)) {
            Toast.makeText(this, "两次输入密码的不一样", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, LoginActivity.class));
        SqlEngine sqlEngine = SqlEngine.getInstance(this);
        sqlEngine.insertInfo(user,pwd,phone);

        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        finish();

    }

    private boolean isMatches(String user, String matches, String message) {
        if (!user.matches(matches)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(String user, String message) {
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwds = (EditText) findViewById(R.id.et_pwds);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_yes:
                isEdit();
                break;
        }
    }
}
