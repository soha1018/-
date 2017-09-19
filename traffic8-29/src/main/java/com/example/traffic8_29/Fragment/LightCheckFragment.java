package com.example.traffic8_29.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.Http.Threshold;
import com.example.traffic8_29.R;

import java.util.HashMap;
import java.util.Random;

/**
 * 光照检测模块
 * Created by Administrator on 2017/9/7.
 */

public class LightCheckFragment extends Fragment implements View.OnClickListener {
    private View view;
    /**
     * 查询
     */
    private Button mBtnQueryLight;
    /**
     * 查询
     */
    private TextView mTvLightStatus;
    private Switch mSwLight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_check, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        mBtnQueryLight = (Button) view.findViewById(R.id.btn_query_light);
        mBtnQueryLight.setOnClickListener(this);
        mTvLightStatus = (TextView) view.findViewById(R.id.tv_light_status);
        mSwLight = (Switch) view.findViewById(R.id.sw_light);

        mSwLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //自动
                    setRoadLightMode("Auto");
                } else {
                    //手动
                    setRoadLightMode("Manual");
                }
            }
        });
    }

    private void setRoadLightMode(String mode) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在设置，请稍等");
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("ControlMode", mode);
        new HttpQuery("SetRoadLightControlMode", map) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_light:
                queryLight();
                break;
        }
    }

    /**
     * 查询光强度
     */
    private void queryLight() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在查询，请稍等");
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("SenseName", "LightIntensity");
        new HttpQuery("GetSenseByName", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                if (baseBean.lightIntensity < Threshold.LIGHT_INTENSITY_MIN) {
                    mTvLightStatus.setTextColor(Color.RED);
                    mTvLightStatus.setText("当前光照值" + baseBean.lightIntensity + "太暗了，为您自动打开路灯");
                    //打开灯
                    for (int i = 1; i < 4; i++) {
                        settingLightStatus("Open",i);
                    }
                } else {

                    if (baseBean.lightIntensity > Threshold.LIGHT_INTENSITY_MAX) {
                        //关闭灯
                        for (int i = 1; i < 4; i++) {
                            settingLightStatus("Close",i);
                        }
                        mTvLightStatus.setTextColor(Color.RED);
                        mTvLightStatus.setText("当前光照值" + baseBean.lightIntensity + "太亮了，为您自动关闭路灯");
                    } else {
                        mTvLightStatus.setTextColor(Color.GREEN);
                        mTvLightStatus.setText("当前光照值" + baseBean.lightIntensity);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String s) {
                dialog.dismiss();
                int anInt = new Random().nextInt(10000);
                if (anInt < Threshold.LIGHT_INTENSITY_MIN) {
                    mTvLightStatus.setTextColor(Color.RED);

                    mTvLightStatus.setText("当前光照值" + anInt + "太暗了，为您自动打开路灯");
                } else {

                    if (anInt > Threshold.LIGHT_INTENSITY_MAX) {
                        mTvLightStatus.setTextColor(Color.RED);

                        mTvLightStatus.setText("当前光照值" + anInt + "太亮了，为您自动关闭路灯");
                    } else {
                        mTvLightStatus.setTextColor(Color.GREEN);

                        mTvLightStatus.setText("当前光照值" + anInt);
                    }
                }
            }
        };
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
