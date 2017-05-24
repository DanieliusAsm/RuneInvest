package com.example.danielius.runeinvest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.response.GraphResponse;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    @BindView(R.id.graph) GraphView graphView;
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
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                int i =0;
                for (Map.Entry<String, String> entry : graphResponse.getData().entrySet()) {
                    i++;
                    if(i % 2 == 0){
                        Double x = Double.parseDouble(entry.getKey());
                        Double y = Double.parseDouble(entry.getValue());
                        DataPoint point = new DataPoint(x,y);
                        series.appendData(point, true, 10);
                    }
                }

                graphView.addSeries(series);
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
