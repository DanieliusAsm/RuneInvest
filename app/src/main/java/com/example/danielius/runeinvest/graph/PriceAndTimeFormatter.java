package com.example.danielius.runeinvest.graph;

import com.jjoe64.graphview.DefaultLabelFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Danielius on 2017-05-27.
 * Converts huge numbers like 1`000`000 into 1m on y axis.
 * Time provided is in Milliseconds. Outputs a month + day on x axis
 */

public class PriceAndTimeFormatter extends DefaultLabelFormatter{

    Calendar calendar;

    public PriceAndTimeFormatter(){
        calendar = calendar==null ? Calendar.getInstance() : calendar;
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if(value<=0){
            return "0";
        }

        if(isValueX) {
            int length = (int) (Math.log10(value) + 1);
            double formatted = value / Math.pow(10, length);

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            String formattedString;
            formattedString = decimalFormat.format(formatted);

            if (length >= 7 && length <= 9) {
                formattedString = formattedString + "M";
            } else if (length >= 10 && length <= 15) {
                formattedString = formattedString + "B";
            }
            return formattedString;
        }else {
            long timeInMillis = (long) value;
            calendar.setTimeInMillis(timeInMillis);
            String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            int mMonth = calendar.get(Calendar.MONTH) - 1;
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            return month[mMonth] + " " + mDay;
        }
    }
}
