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

import com.example.traffic8_29.Bean.EtcBean;
import com.example.traffic8_29.Engine.SqlEngine;
import com.example.traffic8_29.R;
import com.example.traffic8_29.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Administrator on 2017/8/29.
 */

public class EtcQueryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Spinner sp_etc_query;
    /**
     * 查询
     */
    private Button btn_query_etc;
    /**
     * 查询
     */
    private TextView tv_default;
    private ListView lv_etc;
    private ArrayList<EtcBean> list;
    private EtcAdapter etcAdapter;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etc_query, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initData() {

        String[] names = {"时间升序", "时间降序"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, names);
        sp_etc_query.setAdapter(adapter);

        getData();

        sp_etc_query.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        sp_etc_query = (Spinner) view.findViewById(R.id.sp_etc_query);
        btn_query_etc = (Button) view.findViewById(R.id.btn_query_etc);
        btn_query_etc.setOnClickListener(this);
        tv_default = (TextView) view.findViewById(R.id.tv_default);
        lv_etc = (ListView) view.findViewById(R.id.lv_etc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_etc:
                getData();
                if (etcAdapter == null) {
                    return;
                }
                switch (pos) {
                    case 0:
                        Collections.sort(list, new Comparator<EtcBean>() {
                            @Override
                            public int compare(EtcBean o1, EtcBean o2) {
                                return (int) (o1.time-o2.time);
                            }
                        });
                        etcAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Collections.reverse(list);
                        etcAdapter.notifyDataSetChanged();
                        break;
                }
                break;
        }
    }

    private void getData() {
        SqlEngine sqlEngine = SqlEngine.getInstance(getActivity());
        list = sqlEngine.queryETC();
        if (list.size() > 0) {
            tv_default.setVisibility(View.GONE);
            lv_etc.setVisibility(View.VISIBLE);

            etcAdapter = new EtcAdapter();
            lv_etc.setAdapter(etcAdapter);
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(500);
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            lv_etc.setLayoutAnimation(controller);

        } else {
            tv_default.setVisibility(View.VISIBLE);
            lv_etc.setVisibility(View.GONE);
        }
    }

    private class EtcAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public EtcBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_etc_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            EtcBean bean = getItem(position);
            holder.tv_number.setText(position+1+"");
            holder.tv_money.setText(bean.money+"");
            holder.tv_car.setText(bean.number+"");
            holder.tv_user.setText(bean.user);
            holder.tv_time.setText(Utils.LongToString(bean.time));

            return convertView;
        }
    }

    static class ViewHolder {
        View view;
        TextView tv_number;
        TextView tv_car;
        TextView tv_money;
        TextView tv_user;
        TextView tv_time;

        ViewHolder(View view) {
            this.view = view;
            this.tv_number = (TextView) view.findViewById(R.id.tv_number);
            this.tv_car = (TextView) view.findViewById(R.id.tv_car);
            this.tv_money = (TextView) view.findViewById(R.id.tv_money);
            this.tv_user = (TextView) view.findViewById(R.id.tv_user);
            this.tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
