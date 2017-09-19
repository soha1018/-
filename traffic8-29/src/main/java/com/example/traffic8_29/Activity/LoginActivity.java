package com.example.traffic8_29.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.UserBean;
import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.Global.MyApplication;
import com.example.traffic8_29.Http.Https;
import com.example.traffic8_29.MainActivity;
import com.example.traffic8_29.R;
import com.example.traffic8_29.Utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cb_remember;
    private CheckBox cb_auto;
    private static final String TAG = "LoginActivity";
    private int pos = 1;
    private int length = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //存入默认的账号，密码
        SqlEngine.getInstance(this).insertInfo("admin", "admin", "13888888888");
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //跳转到注册界面
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Button btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netSetting();
            }
        });

        initView();
        initData();
    }

    /**
     * 网络设置
     */
    private void netSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.net_setting);
        builder.setIcon(R.drawable.car);
        View view = View.inflate(this, R.layout.dialog_netsetting, null);
        builder.setView(view);
        AlertDialog dialog = builder.show();
        initDialog(dialog, view);
    }

    private void initDialog(final AlertDialog builder, View view) {
        final EditText ed_net1 = (EditText) view.findViewById(R.id.et_net1);
        final EditText ed_net2 = (EditText) view.findViewById(R.id.et_net2);
        final EditText ed_net3 = (EditText) view.findViewById(R.id.et_net3);
        final EditText ed_net4 = (EditText) view.findViewById(R.id.et_net4);
        final EditText et_port = (EditText) view.findViewById(R.id.et_port);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) view.findViewById(R.id.btn_yes);

        //取出来SP中中的值，设置到IP
        String ips = (String) SpUtils.getValues(this, Https.IP_ADDRESS, "");
        String ports = (String) SpUtils.getValues(this, Https.PORT, "");
        if (!TextUtils.isEmpty(ips)) {
            ed_net1.setText(ips.substring(0, 3));
            ed_net2.setText(ips.substring(4, 7));
            ed_net3.setText(ips.substring(8, 9));
            ed_net4.setText(ips.substring(10, 13));
        }
        if (!TextUtils.isEmpty(ports)) {
            et_port.setText(ports);
        }

        final ArrayList<EditText> edList = new ArrayList<>();
        edList.add(ed_net1);
        edList.add(ed_net2);
        edList.add(ed_net3);
        edList.add(ed_net4);
        edList.add(et_port);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == length) {
                    Log.i(TAG, "onTextChanged: 长度等于三");
                    Log.i(TAG, "onTextChanged: 索引是：" + pos);
                    if (pos < edList.size()) {
                        edList.get(pos).requestFocus();
                        pos++;
                        if (pos == 3) {
                            length = 3;
                        } else {
                            length = 3;
                        }
                    } else {
                        pos = 1;
                    }
                }

                for (int i = 0; i < edList.size(); i++) {
                    edList.get(i).isFocusable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        ed_net1.addTextChangedListener(textWatcher);
        ed_net2.addTextChangedListener(textWatcher);
        ed_net3.addTextChangedListener(textWatcher);
        ed_net4.addTextChangedListener(textWatcher);
        et_port.addTextChangedListener(textWatcher);


        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        btn_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip1 = ed_net1.getText().toString();
                String ip2 = ed_net2.getText().toString();
                String ip3 = ed_net3.getText().toString();
                String ip4 = ed_net4.getText().toString();


                String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
                String port = et_port.getText().toString();

                Https.ip = ip;
                Https.port = port;
                SpUtils.putValues(getApplicationContext(), Https.IP_ADDRESS, ip);
                SpUtils.putValues(getApplicationContext(), Https.PORT, port);
                Log.i(TAG, "initDialog: ip:" + Https.ip + ":" + Https.port);
                builder.dismiss();
            }
        });
    }

    private void initData() {
        String user = (String) SpUtils.getValues(this, Https.USER, "");
        String pwd = (String) SpUtils.getValues(this, Https.PWD, "");
        boolean is_remember = (boolean) SpUtils.getValues(this, Https.IS_REMEMBER, false);
        boolean is_auto = (boolean) SpUtils.getValues(this, Https.IS_AUTO, false);
        if (is_auto) {
            cb_auto.setChecked(is_auto);
            startActivity(new Intent(this, MainActivity.class));
        }
        if (is_remember) {
            cb_remember.setChecked(is_remember);
        }
        if (!TextUtils.isEmpty(user)) {
            mEmailView.setText(user);
        }
        if (!TextUtils.isEmpty(pwd)) {
            mPasswordView.setText(pwd);
        }
    }

    private void initView() {
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检测密码
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("请输入密码");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        SqlEngine sqlEngine = SqlEngine.getInstance(this);
        ArrayList<UserBean> list = sqlEngine.queryInfo();
        if (list.size() <= 0) {
            Toast.makeText(this, "没有这个账户", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            UserBean bean = list.get(i);
            Log.i(TAG, "attemptLogin: 执行到此");
            if (!bean.getUser().equals(email)) {
                Toast.makeText(this, "没有这个账户", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!bean.getPwd().equals(password)) {
                Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String ip = (String) SpUtils.getValues(MyApplication.getContext(), Https.IP_ADDRESS, "");
        String port = (String) SpUtils.getValues(MyApplication.getContext(), Https.PORT, "");
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            Toast.makeText(this, "请先设置网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // 显示一个进度微调，并启动一个后台任务
            // 执行用户登录尝试。
            showProgress(true);
            //判断是否勾选
            isCheckBox(email, password);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            startActivity(new Intent(this, MainActivity.class));

        }
    }

    /**
     * 判断是否勾选
     *
     * @param email    账号
     * @param password 密码
     */
    private void isCheckBox(String email, String password) {
        if (cb_remember.isChecked()) {
            SpUtils.putValues(this, Https.USER, email);
            SpUtils.putValues(this, Https.PWD, password);
            SpUtils.putValues(this, Https.IS_REMEMBER, true);
        } else {
            SpUtils.putValues(this, Https.IS_REMEMBER, false);
            //清除已经保存的
            SpUtils.putValues(this, Https.USER, "");
            SpUtils.putValues(this, Https.PWD, "");
        }
        if (cb_auto.isChecked()) {
            SpUtils.putValues(this, Https.IS_AUTO, true);
        } else {
            SpUtils.putValues(this, Https.IS_AUTO, false);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.matches("^[a-zA-Z]{5,12}$");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}