package com.edu.edumediaplayer.playback;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.widget.ImageView;

import com.edu.edumediaplayer.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class Visualizer {

    private BarChart leftChannelChart;
    private BarChart rightChannelChart;
    private ImageView progressBar;
    private Activity activity;
    private float screenWidth;


    public Visualizer(BarChart topBarChart, BarChart bottomBarChart, ImageView progressBar, Activity activity) {
        this.activity = activity;
        this.progressBar = progressBar;
        leftChannelChart = topBarChart;
        rightChannelChart = bottomBarChart;
        stripVisuals(leftChannelChart);
        stripVisuals(rightChannelChart);

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    private void stripVisuals(BarChart barChart) {
        barChart.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
        Description empty = new Description();
        empty.setText("");
        barChart.setDescription(empty);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);
        barChart.setAutoScaleMinMaxEnabled(true);

        // remove axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);

        // hide legend
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    public void setData(List<Integer> leftChannel, List<Integer> rightChannel) {
        setData(leftChannelChart, leftChannel);
        List<Integer> oppVals = new ArrayList<>();
        for (Integer a:rightChannel)
            oppVals.add(a*-1);
        setData(rightChannelChart, oppVals);
    }
    private void setData(BarChart chart, List<Integer> vals) {
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Integer val:vals) {
            entries.add(new BarEntry(i++, val));
        }
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        BarData data = new BarData(dataset);
        data.setBarWidth(1f);
        chart.setData(data);
    }

    public void updateProgress(float perc) {
        int width = (int)(perc*(screenWidth-90));
        if (width<1)
            width = 1;
        progressBar.getLayoutParams().width = width;
        progressBar.requestLayout();
    }

    public void feed(short[] newRawData) {
    }
}
