package com.example.traffic8_29.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;

import java.util.HashMap;

/**
 * 路灯管理
 * Created by Administrator on 2017/9/7.
 */

public class LightManageFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Spinner mSpLight;
    private TextView mTvLightStatus;
    /**
     * 查询
     */
    private Button mBtnQueryLight;
    private Spinner mSpLightNumber;
    /**
     * 查询
     */
    private Button mBtnLightOpen;
    /**
     * 查询
     */
    private Button mBtnLightOn;
    private int statusPos;
    private int settingPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_manage, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initData() {
        initSpinner();
    }

    private void initSpinner() {
        String[] number = new String[3];
        for (int i = 0; i < number.length; i++) {
            number[i] = (i + 1) + "号路灯";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text, number);
        mSpLight.setAdapter(adapter);
        mSpLightNumber.setAdapter(adapter);

        mSpLight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    queryLightStatus(1);
                }
                statusPos = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpLightNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingPos = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 查询红绿灯的状态
     *
     * @param i 几号路灯
     */
    private void queryLightStatus(int i) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在查询路灯状态。。。");
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("RoadLightId", i);
        new HttpQuery("GetRoadLightStatus", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                if (baseBean.lightStatus.equals("Open")) {
                    mTvLightStatus.setText("开启");
                }
                if (baseBean.lightStatus.equals("Close")) {
                    mTvLightStatus.setText("关闭");
                }
                dialog.dismiss();
            }


            @Override
            public void onError(String s) {
                mTvLightStatus.setText("关闭");
                dialog.dismiss();
            }
        };
    }

    private void initView(View view) {
        mSpLight = (Spinner) view.findViewById(R.id.sp_light);
        mTvLightStatus = (TextView) view.findViewById(R.id.tv_light_status);
        mBtnQueryLight = (Button) view.findViewById(R.id.btn_query_light);
        mBtnQueryLight.setOnClickListener(this);
        mSpLightNumber = (Spinner) view.findViewById(R.id.sp_light_number);
        mBtnLightOpen = (Button) view.findViewById(R.id.btn_light_open);
        mBtnLightOpen.setOnClickListener(this);
        mBtnLightOn = (Button) view.findViewById(R.id.btn_light_on);
        mBtnLightOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_light:
                queryLightStatus(statusPos);
                break;
            case R.id.btn_light_open:
                settingLightStatus("Open", settingPos);
                break;
            case R.id.btn_light_on:
                settingLightStatus("Close", settingPos);
                break;
        }
    }

    /**
     * 设置路灯状态
     * @param action 状态
     * @param settingPos 几号路灯
     */
    private void settingLightStatus(String action, int settingPos) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在设置路灯状态。。。");
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("RoadLightId", settingPos);
        map.put("Action", action);
        new HttpQuery("SetRoadLightStatusAction", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
    }
}
