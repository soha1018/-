package com.example.traffic8_29.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaFragment extends Fragment implements View.OnClickListener {
    /**
     * 专项治理道路环境
     */
    private Button btn_creative;
    private Spinner sp;
    /**
     * 专项治理道路环境
     */
    private TextView tv1;
    private Switch sw;
    private Handler handler= new Handler();
    private Switch mSwGreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idea, null);

        initView(view);
        initData();
        return view;
    }

    private void initData() {
        String[] road = new String[]{"小区-学校","小区-医院","停车场-小区"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, road);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    Toast.makeText(getActivity(), "开通成功，该道路值应许非机动车通行", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        alphaAnimation.setRepeatCount(20);

        tv1.startAnimation(alphaAnimation);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
            }
        });
        mSwGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        btn_creative = (Button) view.findViewById(R.id.btn_creative);
        btn_creative.setOnClickListener(this);
        sp = (Spinner) view.findViewById(R.id.sp);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        sw = (Switch) view.findViewById(R.id.sw);
        mSwGreen = (Switch) view.findViewById(R.id.sw_green);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_creative:
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setTitle("治理环境？");
                dialog.setMessage("正在限制车辆出行。。。正在出动洒水车。。。");
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "治理成功", Toast.LENGTH_SHORT).show();
                    }
                }, 2500);
                break;
        }
    }

}
