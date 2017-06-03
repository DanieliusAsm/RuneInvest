package com.example.danielius.runeinvest.graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by Danielius on 2017-06-03.
 * Time provided is in Milliseconds. Outputs a month + day on x axis
 */

public class TimeFormatter {

    Calendar calendar;
    public TimeFormatter(){
        calendar = Calendar.getInstance();
    }

    public ArrayList<String> formatAxisLabels(ArrayList<PointValue> values, int days){
        ArrayList<String> labels = new ArrayList<String>();
        int size = values.size();
        Integer[] indexes = getChartIndexes(days);

        for(int i=0;i<6;i++) {
            long timeInMillis = (long) values.get(indexes[i]).getX();
            calendar.setTimeInMillis(timeInMillis);
            String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            Log.d("data", mMonth + " <-Month Day-> " + mDay + " mili " + timeInMillis);
            labels.add(month[mMonth]);
        }
        return labels;
    }

    public ArrayList<Float> formatAxisValues(ArrayList<PointValue> values, int days){
        ArrayList<Float> axisValues = new ArrayList<Float>();
        int size = values.size();
        Integer[] indexes = getChartIndexes(days);

        for(int i=0;i<6;i++) {
            axisValues.add(values.get(indexes[i]).getX());
        }
        return axisValues;
    }

    public Integer[] getChartIndexes(int days){
        return new Integer[]{7,38,69,100,131,162};
    }
}
