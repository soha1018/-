package com.example.traffic8_29.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Engine.LinearEngine;
import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SenseFragment extends Fragment {
    private View view;
    private TextView tv_title;
    private ViewPager vp_sense;
    private LinearLayout ll_point;
    private String[] names;
    private ArrayList<LinearEngine> chartList = new ArrayList<>();
    private ArrayList<LineChart> lineCharts = new ArrayList<>();
    private int pos;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new HttpQuery("GetAllSense", null) {
                @Override
                public void onSucceed(BaseBean baseBean) {
                    getData(baseBean);
                }

                @Override
                public void onError(String s) {
                    getData(null);
                }
            };
            handler.postDelayed(runnable, 3000);
        }
    };
    private int[] mMax;
    private int[] yMax;

    private void getData(BaseBean baseBean) {
        if (baseBean == null) {
            baseBean = new BaseBean();
        }
        int pm25 = baseBean.pm25;
        int co2 = baseBean.co2;
        int temp = baseBean.temp;
        int humidity = baseBean.humidity;
        int light = baseBean.light;

        switch (pos) {
            case 0:
                setValues(pm25);
                break;
            case 1:
                setValues(co2);
                break;
            case 2:
                setValues(temp);
                break;
            case 3:
                setValues(humidity);
                break;
            case 4:
                setValues(light);
                break;
        }
    }

    private void setValues(int values) {
        LinearEngine engine = chartList.get(pos);
        engine.upData(values);
        SqlEngine sqlEngine = SqlEngine.getInstance(getActivity());
        if (values <= 0) {
            values = new Random().nextInt(yMax[pos]);
        }
        if (values < mMax[pos]) {
            sqlEngine.insertSense(names[pos], values, "正常", System.currentTimeMillis());
        } else {
            sqlEngine.insertSense(names[pos],values,"警告",System.currentTimeMillis());
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.removeCallbacks(runnable);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sense, container, false);

        initView(view);
        initData();
        initAdapter(0);
        return view;

    }

    public void initAdapter(int current) {
        ll_point.getChildAt(0).setEnabled(false);
        tv_title.setText(names[0]);
        vp_sense.setAdapter(new SenseAdapter());

        vp_sense.setCurrentItem(current);
        vp_sense.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                tv_title.setText(names[position]);
                for (int i = 0; i < ll_point.getChildCount(); i++) {
                    if (i == position) {
                        ll_point.getChildAt(i).setEnabled(false);
                    } else {
                        ll_point.getChildAt(i).setEnabled(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < ll_point.getChildCount(); i++) {
            final int finalI = i;
            ll_point.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vp_sense.setCurrentItem(finalI);
                }
            });
        }
    }

    private void initData() {
        names = new String[]{"PM2.5", "CO2", "空气温度", "空气湿度", "光强度"};
        yMax = new int[]{400, 10000, 100, 100, 10000};
        mMax = new int[]{200, 7000, 40, 40, 7000};
        LinearEngine linearEngine;
        View point;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < names.length; i++) {
            linearEngine = new LinearEngine(names[i]);
            chartList.add(linearEngine);

            LineChart chart = linearEngine.getView(getActivity(), yMax[i]);
            lineCharts.add(chart);

            point = new View(getActivity());
            point.setBackgroundResource(R.drawable.select_point);
            point.setEnabled(true);
            layoutParams = new LinearLayout.LayoutParams(30, 30);
            if (i > 0) {
                layoutParams.leftMargin = 15;
            }
            ll_point.addView(point, layoutParams);

        }
    }

    private void initView(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        vp_sense = (ViewPager) view.findViewById(R.id.vp_sense);
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
    }


    private class SenseAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return lineCharts.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LineChart chart = lineCharts.get(position);
            container.addView(chart);
            return chart;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
