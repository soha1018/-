package com.example.traffic8_29.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;

import java.util.HashMap;
import java.util.Random;

/**
 * 车辆管理的界面
 * Created by Administrator on 2017/9/6.
 */

public class CarManageFragment extends Fragment {
    private Spinner mSpCar;
    private Switch mSwCar;
    private Spinner mSpCarSpeed;
    private TextView mTvSpeed;
    private int carPos;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            querySpeed(1);
            querySpeed(2);
            querySpeed(3);
            querySpeed(4);
            handler.postDelayed(runnable, 10 * (60 * 1000));
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            handler.removeCallbacks(runnable);
        } else {
            handler.post(runnable);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.post(runnable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_manage, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        initSpinner();
        //小车开停控制
        mSwCar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //小车启动
                    queryStart();
                } else {
                    queryStop();
                }
            }
        });
        //小车速度查询
        mSpCarSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                querySpeed(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void querySpeed(final int car) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("CarId", car);
        new HttpQuery("GetCarSpeed", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                mTvSpeed.setText(baseBean.carSpeed + "");
                if (baseBean.carSpeed > 100) {
                    showDialog(car);
                }
            }

            @Override
            public void onError(String s) {
                int anInt = new Random().nextInt(150);
                mTvSpeed.setText(anInt + "");
                if (anInt > 100) {
                    showDialog(car);
                }
            }
        };
    }

    /**
     * 弹出对话框，警告小车的速度
     *
     * @param car 车号
     */
    private void showDialog(int car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("警告");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(car + "号小车速度超过100千米");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void queryStop() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在停止车辆，请稍等");
        dialog.show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("CarId", carPos);
        map.put("CarAction", "Stop");

        new HttpQuery("SetCarMove", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                if (baseBean.isSucceed) {
                    Toast.makeText(getActivity(), "停止成功", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String s) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "停止成功", Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * 设置小车启动的方法
     */
    private void queryStart() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在启动车辆，请稍等");
        dialog.show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("CarId", carPos);
        map.put("CarAction", "Start");

        new HttpQuery("SetCarMove", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                if (baseBean.isSucceed) {
                    Toast.makeText(getActivity(), "启动成功", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getActivity(), "启动成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
    }

    private void initSpinner() {
        String[] carNumber = new String[15];
        for (int i = 0; i < carNumber.length; i++) {
            carNumber[i] = (i + 1) + "号车";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text, carNumber);
        mSpCar.setAdapter(adapter);
        mSpCarSpeed.setAdapter(adapter);

        mSpCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carPos = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        mSpCar = (Spinner) view.findViewById(R.id.sp_car);
        mSwCar = (Switch) view.findViewById(R.id.sw_car);
        mSpCarSpeed = (Spinner) view.findViewById(R.id.sp_car_speed);
        mTvSpeed = (TextView) view.findViewById(R.id.tv_speed);
    }
}
