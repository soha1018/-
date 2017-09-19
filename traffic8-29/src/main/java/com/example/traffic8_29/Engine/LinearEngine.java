package com.example.traffic8_29.Engine;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LinearEngine {
    private String title;
    private int xMax;
    private LineChart mChart;

    public LinearEngine(String title) {
        this.title = title;
    }

    public LineChart getView(Context context,int xMax) {
        this.xMax = xMax;

        mChart = new LineChart(context);
        mChart.setDescription("");
        mChart.setScaleEnabled(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        Legend legend = mChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.BLACK);
        legend.setFormSize(25f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(25f);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMaxValue(xMax);
        yAxis.setAxisMinValue(0);
        yAxis.setDrawGridLines(true);
        yAxis.setStartAtZero(false);
        yAxis.setTextSize(25f);

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setDrawGridLines(false);

        return mChart;
    }

    public void upData(int values) {
        if (values <= 0) {
            values = new Random().nextInt(xMax);
        }

        LineData data = mChart.getData();
        LineDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            set = createDataSet();
            data.addDataSet(set);
        }

        data.addXValue(getTime());

        Entry entry = new Entry(values, set.getEntryCount());
        data.addEntry(entry,0);

        mChart.setVisibleXRangeMaximum(6);
        mChart.notifyDataSetChanged();
        mChart.moveViewToX(mChart.getXValCount()-6);
    }

    private LineDataSet createDataSet() {
        LineDataSet dataSet = new LineDataSet(null,title);
        dataSet.setCircleSize(5f);
        dataSet.setLineWidth(4f);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setDrawCubic(true);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.GREEN);
        return dataSet;
    }

    private String getTime() {
        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

        dateFormat.applyPattern("HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
