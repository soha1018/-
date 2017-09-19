package com.example.traffic8_29.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Http.HttpQuery;
import com.example.traffic8_29.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TruntimeFragement extends Fragment {
    private View view;
    private TextView mTVPM25;
    private TextView mTVTe;
    private TextView mTVHu;
    /**
     * 1号道路
     */
    private TextView mTvRoid1;
    /**
     * 1号道路
     */
    private TextView mTvRoid2;
    /**
     * 1号道路
     */
    private TextView mTvRoid3;
    private TextView mCollor1;
    private TextView mColler2;
    private TextView mColler3;
    private Random random = new Random();

    private static final String TAG = "GetRoadStatus";
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getData();
            handler.postDelayed(this, 3000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_truntime_fragement, null);
        initView(view);

        return view;

    }

    private void initView(View view) {
        mTVPM25 = (TextView) view.findViewById(R.id.TV_PM25);
        mTVTe = (TextView) view.findViewById(R.id.TV_te);
        mTVHu = (TextView) view.findViewById(R.id.TV_hu);
        mTvRoid1 = (TextView) view.findViewById(R.id.Tv_Roid1);
        mTvRoid2 = (TextView) view.findViewById(R.id.Tv_Roid2);
        mTvRoid3 = (TextView) view.findViewById(R.id.Tv_Roid3);
        mCollor1 = (TextView) view.findViewById(R.id.collor1);
        mColler2 = (TextView) view.findViewById(R.id.coller2);
        mColler3 = (TextView) view.findViewById(R.id.coller3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(runnable, 0);
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

    private void getData() {
        Map map = new HashMap();
        new HttpQuery("GetAllSense", map) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                int pm25 = baseBean.pm25;
                int temp = baseBean.temp;
                int humidity = baseBean.humidity;

                if (pm25 <= 0) {
                    pm25 = random.nextInt(320);
                }
                if (temp <= 0) {
                    temp = random.nextInt(100);
                }
                if (humidity <= 0) {
                    humidity = random.nextInt(100);
                }
                mTVPM25.setText(""+pm25);
                mTVTe.setText("" +temp);
                mTVHu.setText(""+humidity);
            }

            @Override
            public void onError(String s) {
                mTVPM25.setText(""+random.nextInt(320));
                mTVTe.setText("" +random.nextInt(100));
                mTVHu.setText(""+random.nextInt(100));

            }
        };

        //实时路况 1
        Map map1 = new HashMap();
        map1.put("RoadId", 1);
        new HttpQuery(TAG, map1) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                mTvRoid1.setText("1号道路" + getroid(baseBean.balance));
                mCollor1.setBackgroundResource(getColoer(baseBean.balance));
            }

            @Override
            public void onError(String s) {
                int nextInt = random.nextInt(5);
                mTvRoid1.setText("1号道路" + getroid(nextInt));
                mCollor1.setBackgroundResource(getColoer(nextInt));
            }
        };

        //实时路况 2
        Map map2 = new HashMap();
        map2.put("RoadId", 2);
        new HttpQuery(TAG, map2) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                mTvRoid2.setText("2号道路" + getroid(baseBean.balance));
                mColler2.setBackgroundResource(getColoer(baseBean.balance));
            }

            @Override
            public void onError(String s) {
                int nextInt = random.nextInt(5);
                mTvRoid2.setText("2号道路" + getroid(nextInt));
                mColler2.setBackgroundResource(getColoer(nextInt));
            }
        };

        //实时路况 3
        Map map3 = new HashMap();
        map3.put("RoadId", 3);
        new HttpQuery(TAG, map3) {
            @Override
            public void onSucceed(BaseBean baseBean) {
                mTvRoid3.setText("3号道路" + getroid(baseBean.balance));
                mColler3.setBackgroundResource(getColoer(baseBean.balance));
            }

            @Override
            public void onError(String s) {
                int nextInt = random.nextInt(5);
                mTvRoid3.setText("3号道路" + getroid(nextInt));
                mColler3.setBackgroundResource(getColoer(nextInt));
            }
        };
    }


    public String getroid(int roid) {
        String jiguo = "未知";
        switch (roid) {
            case 0:
                jiguo = "通畅";
                break;

            case 1:
                jiguo = "通畅";

                break;
            case 2:
                jiguo = "较通畅";

                break;
            case 3:
                jiguo = "拥挤";

                break;
            case 4:
                jiguo = "堵塞";

                break;
            case 5:
                jiguo = "爆表";

                break;
        }
        return jiguo;

    }



    public int getColoer(int Start) {
        int coler = R.color.colorMoRen;
        switch (Start) {

            case 0:
                coler = R.color.colorMoRen;
                break;

            case 1:
                coler = R.color.colorMoRen1;

                break;
            case 2:
                coler = R.color.colorMoRen2;

                break;
            case 3:
                coler = R.color.colorMoRen3;

                break;
            case 4:
                coler = R.color.colorMoRen4;

                break;
            case 5:
                coler = R.color.colorMoRen5;

                break;


        }

        return coler;
    }
}
