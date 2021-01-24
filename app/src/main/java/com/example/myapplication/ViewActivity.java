package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends HeaderActivity {
    AnyChartView anyChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setView();
        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        setUpPieChart();
    }
    public void setUpPieChart(){
        Pie pie = AnyChart.pie();
        String[] name = {" ","Water"};
        int[] percent = {30,70};
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i=0; i<name.length; i++){
            dataEntries.add(new ValueDataEntry(name[i],percent[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}