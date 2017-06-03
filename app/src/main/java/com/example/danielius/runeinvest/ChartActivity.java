package com.example.danielius.runeinvest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.lineChart)
    LineChartView lineChart;
    List<PointValue> values = new ArrayList<PointValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        values.add(new PointValue(1f,1f));
        values.add(new PointValue(2f,2f));
        values.add(new PointValue(3f,3f));
        values.add(new PointValue(4f,4f));
        values.add(new PointValue(5f,5f));

        Line line = new Line(values);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis xAxis = new Axis();
        Axis yAxis = new Axis();
        xAxis.setName("Date");
        yAxis.setName("Price");
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);

        lineChart.setLineChartData(data);
    }
}
