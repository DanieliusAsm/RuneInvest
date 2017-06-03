package com.example.danielius.runeinvest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.response.GraphResponse;
import com.example.danielius.runeinvest.graph.PriceAndTimeFormatter;
import com.example.danielius.runeinvest.graph.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

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

        Client.get().getItemGraph(itemId, new Callback<GraphResponse>() {
            @Override
            public void success(GraphResponse graphResponse, Response response) {
                Log.d("debug","success");
                List<String> xVals = new ArrayList<String>();
                List<Float> yVals = new ArrayList<Float>();
                TimeFormatter timeFormatter = new TimeFormatter();

                ArrayList<PointValue> values = new ArrayList<PointValue>();

                for (Map.Entry<String, String> entry : graphResponse.getData().entrySet()) {
                    Float x = Float.parseFloat(entry.getKey());
                    Float y = Float.parseFloat(entry.getValue());

                    values.add(new PointValue(x,y));
                }
                ArrayList<Float> axisValues = timeFormatter.formatAxisValues(values,180);
                ArrayList<String> axisLabels = timeFormatter.formatAxisLabels(values,180);

                Line line = new Line(values);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);

                LineChartData chartData = new LineChartData();
                chartData.setLines(lines);

                Axis xAxis = Axis.generateAxisFromCollection(axisValues,axisLabels);
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

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
