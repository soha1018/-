package com.example.traffic8_29.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traffic8_29.Bean.SenseBean;
import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.R;
import com.example.traffic8_29.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/6.
 */

public class SenseHistoryFragment extends Fragment implements View.OnClickListener {
    private Spinner mSpType;
    private Spinner mSpTime;
    /**
     * 查询
     */
    private Button mBtnQuery;
    /**
     * 暂时还没有数据
     */
    private TextView mTvDefault;
    private ListView mLvSense;
    private String[] names;
    private int posType;
    private long time;
    private ArrayList<SenseBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sense_history, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initData() {
        initSpinner();
    }

    private void initSpinner() {
        names = new String[]{"PM2.5", "CO2", "空气温度", "空气湿度", "光照"};
        String[] times = new String[]{"三分钟", "五分钟"};
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, times);
        mSpType.setAdapter(namesAdapter);
        mSpTime.setAdapter(timesAdapter);

        mSpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        time = System.currentTimeMillis() - (3 * (60 * 1000));
                        break;
                    case 1:
                        time = System.currentTimeMillis() - (5 * (60 * 1000));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        mSpType = (Spinner) view.findViewById(R.id.sp_sense_type);
        mSpTime = (Spinner) view.findViewById(R.id.sp_sense_time);
        mBtnQuery = (Button) view.findViewById(R.id.btn_query);
        mBtnQuery.setOnClickListener(this);
        mTvDefault = (TextView) view.findViewById(R.id.tv_default);
        mLvSense = (ListView) view.findViewById(R.id.lv_sense_history);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                SqlEngine sqlEngine = SqlEngine.getInstance(getActivity());
                list = sqlEngine.querySense(names[posType], time);

                if (list.size() > 0) {
                    mLvSense.setVisibility(View.VISIBLE);
                    mTvDefault.setVisibility(View.GONE);

                    mLvSense.setAdapter(new SenseAdapter());
                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    LayoutAnimationController controller = new LayoutAnimationController(animation);
                    mLvSense.setLayoutAnimation(controller);
                } else {
                    mLvSense.setVisibility(View.GONE);
                    mTvDefault.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private class SenseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SenseBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SenseHistoryHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_sense_history_item, null);
                holder = new SenseHistoryHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (SenseHistoryHolder) convertView.getTag();
            }
            SenseBean bean = getItem(position);
            holder.mTvName.setText(bean.name);
            holder.mTvNumber.setText(bean.number+"");
            holder.mTvStatus.setText(bean.status);
            String time = Utils.LongToString(bean.time);
            holder.mTvTime.setText(time);
            return convertView;
        }
    }

    private static class SenseHistoryHolder {
        View view;
        TextView mTvName;
        TextView mTvNumber;
        TextView mTvStatus;
        TextView mTvTime;

        SenseHistoryHolder(View view) {
            this.view = view;
            this.mTvName = (TextView) view.findViewById(R.id.tv_name);
            this.mTvNumber = (TextView) view.findViewById(R.id.tv_number);
            this.mTvStatus = (TextView) view.findViewById(R.id.tv_status);
            this.mTvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
