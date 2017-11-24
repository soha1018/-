package com.example.errand.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.errand.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private ViewPager mVpSplash;
    private ImageView[] imageViews;
    private LinearLayout mll_point;
    private Button btn_splash;
    private int current;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        initAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAdapter() {
        mVpSplash.setAdapter(new MySplashAdapter());
        mll_point.getChildAt(0).setEnabled(false);

        //页面事件的监听
        mVpSplash.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                for (int i = 0; i < mll_point.getChildCount(); i++) {
                    if (i == position) {
                        mll_point.getChildAt(i).setEnabled(false);
                    } else {
                        mll_point.getChildAt(i).setEnabled(true);
                    }

                }
                if (position < (imageViews.length - 1)) {
                    btn_start.setVisibility(View.INVISIBLE);
                } else {
                    btn_start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mVpSplash.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float entX;
            float entY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        entX = motionEvent.getX();
                        entY = motionEvent.getY();
                        if (entX < startX && current==(imageViews.length-1)) {
                            startLoginActivity();
                        }

                        break;

                }
                return false;
            }
        });


        //点击小圆点
        for (int i = 0; i < mll_point.getChildCount(); i++) {
            final int finalI = i;
            mll_point.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mVpSplash.setCurrentItem(finalI);
                }
            });
        }
    }

    /**
     * 初始化图片数据
     */
    private void initData() {
        int[] images = {R.mipmap.splash1, R.mipmap.splash2, R.mipmap.splash3};
        imageViews = new ImageView[images.length];
        View view;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < imageViews.length; i++) {
            //图片
            imageViews[i] = new ImageView(this);
            imageViews[i].setImageResource(images[i]);

            //圆点
            view = new View(this);
            view.setBackgroundResource(R.drawable.select_point);
            params = new LinearLayout.LayoutParams(20, 20);
            if (i > 0) {
                params.leftMargin = 12;
            }
            mll_point.addView(view, params);
        }

    }

    private void initView() {
        mVpSplash = (ViewPager) findViewById(R.id.vp_splash);
        mll_point = (LinearLayout) findViewById(R.id.ll_point);
        btn_splash = (Button) findViewById(R.id.btn_splash);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });

    }

    /**
     * 跳转到登陆的界面
     */
    private void startLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private class MySplashAdapter extends PagerAdapter {
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
