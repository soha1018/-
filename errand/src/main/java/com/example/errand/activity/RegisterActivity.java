package com.example.errand.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.errand.R;
import com.example.errand.http.UserRequest;
import com.example.errand.intafece.UserInterface;
import com.example.errand.utils.MyUtils;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRepassword;
    private EditText mEtRegisterEmail;
    private EditText mEtPhone;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    /**
     * 检查数据的合法性
     */
    private void checkData() {
        final String username = mEtUsername.getText().toString().trim();
        final String password = mEtPassword.getText().toString().trim();
        String rePassword = mEtRepassword.getText().toString().trim();
        final String email = mEtRegisterEmail.getText().toString().trim();
        final String phone = mEtPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePassword)) {
            Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rePassword.equals(password)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //正则表达式校验邮箱和手机号
        if (!email.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            Toast.makeText(this, "邮箱格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$")) {
            Toast.makeText(this, "手机号码格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        //长度校验
        if (username.length() < 4 || username.length() > 9) {
            Toast.makeText(this, "用户名长度必须4-9", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            Toast.makeText(this, "密码长度必须6-16", Toast.LENGTH_SHORT).show();
            return;
        }
        //提供接口，把数据存入数据库
        final ProgressDialog dialog = MyUtils.setProgressDialog(this, "注册", "正在注册，请稍等。。。");
        UserRequest request = new UserRequest();
        request.registerUser(username, password, email, phone, new UserInterface() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "注册失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private void initView() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtRepassword = (EditText) findViewById(R.id.et_repassword);
        mEtRegisterEmail = (EditText) findViewById(R.id.et_register_email);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });
    }
}
