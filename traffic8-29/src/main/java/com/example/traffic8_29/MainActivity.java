package com.example.traffic8_29;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffic8_29.Fragment.CarManageFragment;
import com.example.traffic8_29.Fragment.EnSenseFragment;
import com.example.traffic8_29.Fragment.EtcQueryFragment;
import com.example.traffic8_29.Fragment.IdeaFragment;
import com.example.traffic8_29.Fragment.LightCheckFragment;
import com.example.traffic8_29.Fragment.LightManageFragment;
import com.example.traffic8_29.Fragment.MyCarFragment;
import com.example.traffic8_29.Fragment.SenseFragment;
import com.example.traffic8_29.Fragment.SenseHistoryFragment;
import com.example.traffic8_29.Fragment.TrafficLightFragment;
import com.example.traffic8_29.Fragment.TruntimeFragement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction beginTransaction;
    private Fragment[] fragments;
    private long oldTime;
    private int[] title;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Utils.showNotification(this,"标题","累死了",pi);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_title = (TextView) findViewById(R.id.tv_main_title);
        title = new int[]{R.string.sense, R.string.my_car, R.string.etc_quert,
                R.string.traffic_light,R.string.road_status,R.string.en_sense,
                R.string.sense_history,R.string.car_manage,R.string.light_manage,
                R.string.light_intensity_check,R.string.creative};
        select(0);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - oldTime > 1500) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                oldTime = timeMillis;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            select(0);
        } else if (id == R.id.nav_gallery) {
            select(1);
        } else if (id == R.id.nav_slideshow) {
            select(2);
        } else if (id == R.id.nav_traffic_light) {
            select(3);
        }else if (id == R.id.nav_manage) {
            select(4);
        } else if (id == R.id.nav_en_sense) {
            select(5);
        }else if (id == R.id.sense_history) {
            select(6);
        } else if (id == R.id.nav_car_manage) {
            select(7);
        }else if (id == R.id.nav_light_manage) {
            select(8);
        }else if (id == R.id.nav_light_check) {
            select(9);
        }else if (id == R.id.creative) {
            select(10);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void select(int pos) {
        tv_title.setText(title[pos]);
        beginTransaction = getSupportFragmentManager().beginTransaction();
        initFragment();
        hideFragment();
        showFragment(pos);
        beginTransaction.commit();
    }

    private void showFragment(int pos) {
        if (fragments.length > pos) {
            beginTransaction.show(fragments[pos]);
        }
    }


    private void hideFragment() {
        for (Fragment f :
                fragments) {
            beginTransaction.hide(f);
        }
    }

    private void initFragment() {
        if (fragments == null) {
            fragments = new Fragment[]{new SenseFragment(),new MyCarFragment(),
                    new EtcQueryFragment(),new TrafficLightFragment(),new TruntimeFragement(),
                    new EnSenseFragment(),new SenseHistoryFragment(),new CarManageFragment(),
                    new LightManageFragment(),new LightCheckFragment(),new IdeaFragment()};
            for (Fragment f :
                    fragments) {
                beginTransaction.add(R.id.fl_main, f);
            }
        }
    }

    /**
     * 设置环境折线图当前的位置
     */
    public void setSenseCurrent() {
        select(0);
    }
}
