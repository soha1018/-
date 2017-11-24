package com.example.errand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.errand.R;
import com.example.errand.bean.UserBean;
import com.example.errand.http.UserRequest;
import com.example.errand.intafece.UserInterface;
import com.example.errand.utils.MyUtils;
import com.example.errand.utils.SqlUtils;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private ImageView mIvIcon;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mBtnRegister;
    private CheckBox cb_auto;
    private CheckBox cb_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
    }

    private void initData() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        boolean auto = (boolean) MyUtils.getValues(getApplicationContext(), "CB_AUTO", true);
        SqlUtils sqlUtils = SqlUtils.getInstance(getApplicationContext());
        if (auto) {
            String username = (String) MyUtils.getValues(getApplicationContext(), "USERNAME", "");
            UserBean bean = sqlUtils.queryUser(username);

            if (bean != null) {

                mEtUsername.setText(bean.getUsername());
                mEtPassword.setText(bean.getPassword());

                login(bean.getUsername(), bean.getPassword());
            }
            cb_auto.setChecked(true);
        } else {
            cb_auto.setChecked(false);
        }
        boolean save = (boolean) MyUtils.getValues(getApplicationContext(), "CB_SAVE", true);
        if (save) {
            String username = (String) MyUtils.getValues(getApplicationContext(), "USERNAME", "");
            if (username != null) {
                mEtUsername.setText(username);
            }
            cb_save.setChecked(true);
        } else {
            cb_save.setChecked(false);
        }
    }

    /**
     * 用户登录
     */
    private void loginUser() {
        final String username = mEtUsername.getText().toString().trim();
        final String password = mEtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        login(username, password);
    }

    /**
     * 网络登陆
     */
    private void login(final String username, final String password) {
        UserRequest request = new UserRequest();
        request.login(username, password, new UserInterface() {
            @Override
            public void onSuccess() {
                SqlUtils sqlUtils = SqlUtils.getInstance(getApplicationContext());
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                if (cb_auto.isChecked()) {
                    MyUtils.putValues(getApplicationContext(), "CB_AUTO", true);
                    MyUtils.putValues(getApplicationContext(), "USERNAME", username);
                    sqlUtils.insert(username, password);
                } else {
                    sqlUtils.deleteUser(username);
                    MyUtils.putValues(getApplicationContext(), "CB_AUTO", false);
                }
                if (cb_save.isChecked()) {
                    MyUtils.putValues(getApplicationContext(), "CB_SAVE", true);
                    MyUtils.putValues(getApplicationContext(), "USERNAME", username);
                } else {
                    MyUtils.putValues(getApplicationContext(), "CB_SAVE", false);
                    MyUtils.putValues(getApplicationContext(), "USERNAME", "");
                }
            }

            @Override
            public void onError() {
                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (TextView) findViewById(R.id.btn_register);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        cb_save = (CheckBox) findViewById(R.id.cb_save);
    }
}
