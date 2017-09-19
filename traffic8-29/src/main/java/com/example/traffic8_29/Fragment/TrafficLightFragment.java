package com.example.traffic8_29.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

/**
 * 红绿灯管理
 * Created by Administrator on 2017/8/30.
 */

public class TrafficLightFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Spinner mSpLight;
    /**
     * 查询
     */
    private Button mBtnLightQuery;
    /**
     * 查询
     */
    private LinearLayout mTvDefault;
    private ListView mLvLight;
    private int pos = 1;
    private ArrayList<BaseBean> list;
    private LightAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic_light, container, false);

        initView(view);
        initData();
        return view;
    }


    private void initData() {
        initSpinner();
        queryLight();
    }

    /**
     * 查询当前红绿灯的状态
     */
    private void queryLight() {
        HashMap<String, Object> map;
        for (int i = 1; i <= 5; i++) {
            if (i == 5) {
                list = new ArrayList<BaseBean>();
            }
            map = new HashMap<>();
            map.put("TrafficLightId", i);
            final int finalI = i;
            new HttpQuery("GetTrafficLightConfigAction", map) {
                @Override
                public void onSucceed(BaseBean baseBean) {
                    baseBean.roadNumber = finalI;
                    list.add(baseBean);
                    if (list.size() == 5) {
                        mLvLight.setVisibility(View.VISIBLE);
                        mTvDefault.setVisibility(View.GONE);

                        adapter = new LightAdapter();
                        mLvLight.setAdapter(adapter);
                    } else {
                        mLvLight.setVisibility(View.GONE);
                        mTvDefault.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(String s) {
                    BaseBean baseBean = new BaseBean();
                    baseBean.redTime = 5;
                    baseBean.greedTime = 5;
                    baseBean.yellowTime = 3;
                    list.add(baseBean);


                    mLvLight.setVisibility(View.VISIBLE);
                    mTvDefault.setVisibility(View.GONE);

                    adapter = new LightAdapter();

                    mLvLight.setAdapter(adapter);
                    Log.i(TAG, "onError: " + s);
                }
            };
        }

    }


    private void initSpinner() {
        String[] names = {"路口升序", "路口降序", "红灯升序", "红灯降序", "绿灯升序", "绿灯降序", "黄灯升序", "黄灯降序"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, names);
        mSpLight.setAdapter(adapter);

        mSpLight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        mSpLight = (Spinner) view.findViewById(R.id.sp_light);
        mBtnLightQuery = (Button) view.findViewById(R.id.btn_light_query);
        mBtnLightQuery.setOnClickListener(this);
        mTvDefault = (LinearLayout) view.findViewById(R.id.tv_default);
        mLvLight = (ListView) view.findViewById(R.id.lv_light);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_light_query:
                if (adapter == null) {
                    return;
                }
                switch (pos) {
                    case 0:
                        Collections.sort(list, new Comparator<BaseBean>() {
                            @Override
                            public int compare(BaseBean o1, BaseBean o2) {
                                return o1.roadNumber-o2.roadNumber;
                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Collections.sort(list, new Comparator<BaseBean>() {
                            @Override
                            public int compare(BaseBean o1, BaseBean o2) {
                                return o2.roadNumber-o1.roadNumber;
                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                }
                break;
        }
    }

    private class LightAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public BaseBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LightHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_light_item, null);
                holder = new LightHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (LightHolder) convertView.getTag();
            }
            BaseBean bean = getItem(position);
            holder.mRoadNumber.setText(bean.roadNumber+"");
            holder.mGreenTime.setText(bean.greedTime + "");
            holder.mYellowTime.setText(bean.yellowTime + "");
            holder.mRedTime.setText(bean.redTime + "");
            return convertView;
        }
    }

    static class LightHolder {
        View view;
        TextView mRoadNumber;
        TextView mRedTime;
        TextView mYellowTime;
        TextView mGreenTime;

        LightHolder(View view) {
            this.view = view;
            this.mRoadNumber = (TextView) view.findViewById(R.id.road_number);
            this.mRedTime = (TextView) view.findViewById(R.id.red_time);
            this.mYellowTime = (TextView) view.findViewById(R.id.yellow_time);
            this.mGreenTime = (TextView) view.findViewById(R.id.green_time);
        }
    }
}
