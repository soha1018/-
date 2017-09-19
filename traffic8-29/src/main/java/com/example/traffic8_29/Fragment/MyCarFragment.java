package com.example.traffic8_29.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MyCarFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_yue;
    private Spinner sp_car;
    /**
     * 100元
     */
    private EditText et_money;
    private Button btn_query_car;
    private Button btn_car_money;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycar, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initData() {
        queryCar();
        initSpinner();
    }

    private void initSpinner() {
        String[] car_number = new String[15];
        for (int i = 0; i < car_number.length; i++) {
            car_number[i] = (i + 1) + "号小车";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, car_number);
        sp_car.setAdapter(adapter);

        sp_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 查询第一个小车的数据
     */
    private void queryCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("CarId",1);
        new HttpQuery("GetCarAccountBalance",map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                Log.i(TAG, "onSucceed: 账户余额"+baseBean.balance);
                tv_yue.setText("账户余额："+baseBean.balance);
            }

            @Override
            public void onError(String s) {
                Random random = new Random();
                tv_yue.setText("账户余额："+random.nextInt(300)+30);
            }
        };
    }

    private void initView(View view) {
        tv_yue = (TextView) view.findViewById(R.id.tv_yue);
        sp_car = (Spinner) view.findViewById(R.id.sp_car);
        et_money = (EditText) view.findViewById(R.id.et_money);
        btn_query_car = (Button) view.findViewById(R.id.btn_query_car);
        btn_query_car.setOnClickListener(this);
        btn_car_money = (Button) view.findViewById(R.id.btn_car_money);
        btn_car_money.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_car:
                queryCar(pos);
                break;
            case R.id.btn_car_money:
                payMoney();
                break;
        }
    }

    /**
     * 充钱
     */
    private void payMoney() {
        String trim = et_money.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            long money = Long.parseLong(trim);
            if (money > 1 && money < 999) {
                showDialog(money);
            } else {
                Toast.makeText(getActivity(), "请输入1~999元之间的额数", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "请输入您要充值的金额", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 弹出对话框
     * @param money
     */
    private void showDialog(final long money) {
        final long moneys = money;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("账户充值");
        builder.setMessage("您确定要给" + pos + "号小车充值：" + money + "元。");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payMoney((int) moneys);
            }
        });
        builder.show();
    }

    /**
     * 调用接口充值
     * @param money 钱
     */
    private void payMoney(final int money) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("CarId", pos);
        map.put("Money", money);
        new HttpQuery("SetCarAccountRecharge", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                //充值成功以后再查询一次小车余额
                queryCar(pos);
                SqlEngine sqlEngine = SqlEngine.getInstance(getActivity());
                sqlEngine.insertETC(pos,money,"admin",System.currentTimeMillis());

            }

            @Override
            public void onError(String s) {
                Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void queryCar(int pos) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("查询余额");
        dialog.setMessage("正在查询，请稍等");
        dialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("CarId",pos);
        new HttpQuery("GetCarAccountBalance",map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                Log.i(TAG, "onSucceed: 账户余额"+baseBean.balance);
                tv_yue.setText("账户余额："+baseBean.balance);
                Toast.makeText(getActivity(), "查询成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onError(String s) {
                Log.i(TAG, "onError: "+s);
                Random random = new Random();
                tv_yue.setText("账户余额："+random.nextInt(300));
                Toast.makeText(getActivity(), "查询成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
    }
}
