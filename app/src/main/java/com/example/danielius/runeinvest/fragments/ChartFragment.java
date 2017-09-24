package com.example.danielius.runeinvest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.response.GraphResponse;
import com.example.danielius.runeinvest.graph.PriceFormatter;
import com.example.danielius.runeinvest.graph.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    @BindView(R.id.lineChart)
    LineChartView lineChart;
    private String itemId;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        itemId = getArguments().getString("item_id");

        Client.get().getItemGraph(itemId, new Callback<GraphResponse>() {
            @Override
            public void success(GraphResponse graphResponse, Response response) {
                Log.d("debug","success");
                TimeFormatter timeFormatter = new TimeFormatter();
                PriceFormatter priceFormatter = new PriceFormatter();
                ArrayList<PointValue> values = new ArrayList<PointValue>();
                float maxY=0,minY=Float.MAX_VALUE;

                for (Map.Entry<String, String> entry : graphResponse.getData().entrySet()) {
                    Float x = Float.parseFloat(entry.getKey());
                    Float y = Float.parseFloat(entry.getValue());
                    values.add(new PointValue(x,y));

                    if(y>maxY){
                        maxY = y;
                    }
                    if(y<minY){
                        minY = y;
                    }
                }
                ArrayList<Float> xAxisValues = timeFormatter.formatAxisValues(values,180);
                ArrayList<String> xAxisLabels = timeFormatter.formatAxisLabels(values,180);
                ArrayList<Float> yAxisValues = priceFormatter.formatAxisValues(minY,maxY);
                ArrayList<String> yAxisLabels = priceFormatter.formatAxisLabels(yAxisValues);

                Line line = new Line(values).setColor(Color.BLUE).setHasLabelsOnlyForSelected(true);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);

                LineChartData chartData = new LineChartData();
                chartData.setLines(lines);

                Axis xAxis = Axis.generateAxisFromCollection(xAxisValues,xAxisLabels);
                chartData.setAxisXBottom(xAxis);
                Axis yAxis = Axis.generateAxisFromCollection(yAxisValues,yAxisLabels);
                chartData.setAxisYLeft(yAxis);

                lineChart.setLineChartData(chartData);
                lineChart.setValueSelectionEnabled(true);

                /*lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
                    @Override
                    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {

                    }

                    @Override
                    public void onValueDeselected() {

                    }
                });*/
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("debug","fail");
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
