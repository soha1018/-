package com.example.traffic8_29.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.traffic8_29.Http.Https;
import com.example.traffic8_29.MainActivity;
import com.example.traffic8_29.R;
import com.example.traffic8_29.Utils.SpUtils;

/**
 * 引导界面
 * Created by Administrator on 2017/8/31.
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mVpSplash;
    private LinearLayout mLlSplash;
    private ImageView[] imageViews;
    private Button btn_over;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();

    }

    private void initData() {
        initViewPager();
        initPoint();
    }

    /**
     * 初始化小圆点
     */
    private void initPoint() {
        View point;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageViews.length; i++) {
            point = new View(this);
            point.setBackgroundResource(R.drawable.select_point);
            point.setEnabled(true);
            layoutParams = new LinearLayout.LayoutParams(30, 30);
            if (i > 0) {
                layoutParams.leftMargin = 20;
            }
            mLlSplash.addView(point, layoutParams);
        }

        mLlSplash.getChildAt(0).setEnabled(false);

        mVpSplash.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mLlSplash.getChildCount(); i++) {
                    if (i == position) {
                        mLlSplash.getChildAt(i).setEnabled(false);
                    } else {
                        mLlSplash.getChildAt(i).setEnabled(true);
                    }
                }
                if (position == imageViews.length - 1) {
                    btn_over.setVisibility(View.VISIBLE);
                } else {
                    btn_over.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < mLlSplash.getChildCount(); i++) {
            final int finalI = i;
            mLlSplash.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVpSplash.setCurrentItem(finalI);
                }
            });
        }
    }

    private void initViewPager() {
        int[] image = new int[]{R.drawable.daohang1, R.drawable.daohang2, R.drawable.daohang3};
        imageViews = new ImageView[image.length];
        for (int i = 0; i < image.length; i++) {
            imageViews[i] = new ImageView(this);
            imageViews[i].setImageResource(image[i]);
        }
        mVpSplash.setAdapter(new SplashAdapter());
    }

    private void initView() {
        mVpSplash = (ViewPager) findViewById(R.id.vp_splash);
        mLlSplash = (LinearLayout) findViewById(R.id.ll_splash);
        btn_over = (Button) findViewById(R.id.btn_over);
        btn_over.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_over:
                startActivity(new Intent(this, LoginActivity.class));
                SpUtils.putValues(this, Https.IS_SPLASH,true);
                finish();
                break;
        }
    }

    private class SplashAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews[position];
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
