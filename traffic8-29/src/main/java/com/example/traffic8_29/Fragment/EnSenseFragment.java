package com.example.traffic8_29.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.MainActivity;
import com.example.traffic8_29.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * 方块的实时环境界面
 * Created by Administrator on 2017/9/1.
 */

public class EnSenseFragment extends Fragment {
    private GridView mGvSense;
    private String[] names = new String[]{"PM2.5", "CO2", "温度", "湿度", "光照", "道路1"};
    private Handler handler = new Handler();
    private int[] yMax = new int[]{300, 10000, 50, 100, 10000, 4};
    private ArrayList<Integer> list = new ArrayList<>();
    private Random random = new Random();
    private int i;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            querySense();
            queryRoad();
            handler.postDelayed(runnable, 2000);
        }
    };
    private EnSenseAdapter adapter;

    /**
     * 查询道路
     */
    private void queryRoad() {
        if (list.size() == 5) {

            HashMap<String, Object> map = new HashMap<>();
            map.put("RoadId", 1);
            new HttpQuery("GetRoadStatus", map) {
                @Override
                public void onSucceed(BaseBean baseBean) {
                    list.addAll(Arrays.asList(baseBean.balance));

                    if (list.size() >= 6) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(String s) {
                    list.addAll(Arrays.asList(random.nextInt(4)));

                    if (list.size() >= 6) {
                        adapter.notifyDataSetChanged();
                    }
                }
            };
        }

    }

    /**
     * 请求环境指标
     */
    private void querySense() {
        new HttpQuery("GetAllSense", null) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                getData(baseBean);
                Log.i(TAG, "onSucceed: list的长度是："+list.size());
                /*if (list.size() == 5) {
                    queryRoad();
                }*/
            }

            @Override
            public void onError(String s) {
                getData(null);
                Log.i(TAG, "onSucceed: list的长度是："+list.size());

               /* if (list.size() == 5) {
                    queryRoad();
                }*/
            }
        };
    }

    /**
     * 获取到环境的数据
     *
     * @param baseBean bean类
     */
    private void getData(BaseBean baseBean) {
        if (list.size() >= 6) {
            list = new ArrayList<>();
        }
        if (baseBean == null) {
            baseBean = new BaseBean();
        }

        int pm25 = baseBean.pm25;
        int co2 = baseBean.co2;
        int temp = baseBean.temp;
        int humidity = baseBean.humidity;
        int lightIntensity = baseBean.light;

        setValues(0, pm25);
        setValues(1, co2);
        setValues(2, temp);
        setValues(3, humidity);
        setValues(4, lightIntensity);

    }

    private void setValues(int pos, int values) {
        if (values <= 0) {
            values = random.nextInt(yMax[pos]);
        }
        list.add(values);
    }

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
        View view = inflater.inflate(R.layout.fragment_en_sense, container, false);

        initView(view);
        initAdapter();
        return view;
    }

    private void initAdapter() {
        adapter = new EnSenseAdapter();
        mGvSense.setAdapter(adapter);


        mGvSense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: 选中了：" + position);
                ((MainActivity) getActivity()).setSenseCurrent();
            }
        });
    }

    private void initView(View view) {
        mGvSense = (GridView) view.findViewById(R.id.gv_sense);
    }

    private class EnSenseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EnSenseHolder senseHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.gv_sense_item, null);
                senseHolder = new EnSenseHolder(convertView);
                convertView.setTag(senseHolder);
            } else {
                senseHolder = (EnSenseHolder) convertView.getTag();
            }
            if (list.size() != 6) {
                senseHolder.en_value.setText(String.valueOf(random.nextInt(yMax[position])));
            } else {
                Log.i(TAG, "getView: 还是不错滴");
                senseHolder.en_value.setText(String.valueOf(list.get(position)));
            }
            senseHolder.tv_en_name.setText(names[position]);
            setTextColor(senseHolder.ll_en, senseHolder.en_value, senseHolder.tv_en_status, position);
            return convertView;
        }
    }

    private void setTextColor(LinearLayout ll_en, TextView en_value, TextView tv_en_status, int position) {
        if (en_value.getText().toString().equals("")) {
            return;
        } else {
            i = Integer.parseInt(en_value.getText().toString());
        }


        switch (position) {
            case 0:
                setWarning(ll_en, tv_en_status, 50, 200, Color.RED, Color.GREEN, "警告", "正常");
                break;
            case 1:
                setWarning(ll_en, tv_en_status, 0, 6000, Color.RED, Color.GREEN, "警告", "正常");
                break;
            case 2:
                setWarning(ll_en, tv_en_status, -20, 40, Color.RED, Color.GREEN, "警告", "正常");
                break;
            case 3:
                setWarning(ll_en, tv_en_status, 0, 80, Color.RED, Color.GREEN, "警告", "正常");
                break;
            case 4:
                setWarning(ll_en, tv_en_status, 0, 7000, Color.RED, Color.GREEN, "警告", "正常");
                break;
            case 5:
                setWarning(ll_en, tv_en_status, -1, 3, Color.RED, Color.GREEN, "警告", "正常");
                break;

        }
    }

    /**
     * 设置警戒值
     *
     * @param ll_en     布局
     * @param status    当前状态
     * @param valuesMin 设置最小值
     * @param valuesMax 设置最大值
     * @param red       警告颜色
     * @param green     正常颜色
     * @param warning   警告话语
     * @param normal    默认话语
     */
    private void setWarning(LinearLayout ll_en, TextView status, int valuesMin, int valuesMax, int red, int green, String warning, String normal) {
        if (valuesMin > i || i > valuesMax) {
            ll_en.setBackgroundColor(Color.RED);
            status.setText("警告");
        } else {
            ll_en.setBackgroundColor(Color.GREEN);
            status.setText("正常");
        }
    }

    private static class EnSenseHolder {
        View view;
        TextView tv_en_name;
        TextView tv_en_status;
        TextView en_value;
        LinearLayout ll_en;

        EnSenseHolder(View view) {
            this.view = view;
            this.tv_en_name = (TextView) view.findViewById(R.id.tv_en_name);
            this.tv_en_status = (TextView) view.findViewById(R.id.tv_en_status);
            this.en_value = (TextView) view.findViewById(R.id.en_value);
            this.ll_en = (LinearLayout) view.findViewById(R.id.ll_en);
        }
    }
}
