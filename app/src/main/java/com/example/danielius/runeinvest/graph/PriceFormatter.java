package com.example.danielius.runeinvest.graph;

import android.util.Log;

//import com.jjoe64.graphview.DefaultLabelFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by Danielius on 2017-05-27.
 * Converts huge numbers like 1`000`000 into 1m on y axis.
 * Time provided is in Milliseconds. Outputs a month + day on x axis
 */

public class PriceFormatter /*extends DefaultLabelFormatter*/ {

    Calendar calendar;

    public PriceFormatter() {
        calendar = Calendar.getInstance();
    }

    public ArrayList<Float> formatAxisValues(float min, float max) {
        ArrayList<Float> selectedValues = new ArrayList<>();
        selectedValues.add(min);
        float difference = (max-min)/9;

        for(int i=0;i<8;i++){
            float n = 2+i;
            float axisValue = min + difference*(n-1);
            selectedValues.add(axisValue);
        }
        selectedValues.add(max);
        return selectedValues;
    }

    public ArrayList<String> formatAxisLabels(ArrayList<Float> axisValues) {
        ArrayList<String> axisLabels = new ArrayList<>();

        for(float value : axisValues){
            int length = (int) (Math.log10(value) + 1);
            double formatted;

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
            String formattedString;

            if (length >= 5 && length <= 6) {
                formatted = value / Math.pow(10, 3);
                formattedString = decimalFormat.format(formatted) + "k";
            } else if (length >= 7 && length <= 9) {
                formatted = value / Math.pow(10, 6);
                formattedString = decimalFormat.format(formatted) + "M";
            } else if (length >= 10 && length <= 15) {
                formatted = value / Math.pow(10, 9);
                formattedString = decimalFormat.format(formatted) + "B";
            } else {
                formattedString = "" + value;
            }
            axisLabels.add(formattedString);
            //Log.d("data","v:"+value+" length:"+length+" formatted:"+formatted);
        }

        return axisLabels;
    }
}
