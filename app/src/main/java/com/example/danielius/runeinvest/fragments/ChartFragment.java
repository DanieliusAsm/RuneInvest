package com.example.danielius.runeinvest.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.response.GraphResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    //@Bind(R.id.chart)
    LineChart chart;

    private String itemId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        chart = new LineChart(getActivity());
        getActivity().setContentView(chart);
        Client.get().getItemGraph(itemId, new Callback<GraphResponse>() {
            @Override
            public void success(GraphResponse graphResponse, Response response) {
                Log.d("debug","success");

                List<String> xVals = new ArrayList<String>();
                List<Float> yVals = new ArrayList<Float>();
                int i =0;
                for (Map.Entry<String, String> entry : graphResponse.getData().entrySet()) {
                    i++;
                    if(i % 2 == 0){
                        xVals.add(entry.getKey());
                        yVals.add(Float.parseFloat(entry.getValue()));
                    }
                }

                LineData data = new LineData(xVals,getDataSet(yVals));
                chart.setData(data);
                chart.setDescription("My Chart");
                chart.animateX(2000);
                chart.invalidate();
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

    private ArrayList<LineDataSet> getDataSet(List<Float> yVals) {
        ArrayList<LineDataSet> dataSets = null;

        ArrayList<Entry> valueSet = new ArrayList<>();
        for(int i=0;i<yVals.size();i++){
            float yVal = yVals.get(i);
            Entry entry = new Entry(yVal,i);
            valueSet.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(valueSet, "Brand 1");
        lineDataSet.setColor(Color.rgb(0, 155, 0));


        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        return dataSets;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setContentView(R.layout.activity_main);
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
