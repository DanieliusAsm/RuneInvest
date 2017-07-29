package com.example.danielius.runeinvest.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.model.Alpha;
import com.example.danielius.runeinvest.api.model.Item;
import com.example.danielius.runeinvest.api.response.CategoryResponse;
import com.example.danielius.runeinvest.api.response.ItemResponse;
import com.example.danielius.runeinvest.sqlite.MySQLiteHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ItemsFragment extends Fragment {

    @BindView(R.id.my_items)
    RecyclerView recycler;

    private int categoryId;
    private MyRecyclerAdapter adapter;
    List<Item> items = new ArrayList<Item>();
    List<Alpha> alphaList = new ArrayList<Alpha>();
    ArrayList<Integer> categories = new ArrayList<>();
    private Runnable runnable;
    Handler handler;
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        handler = new Handler();
        adapter = new MyRecyclerAdapter(items);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);

        if(items.size()==0){
            /*List<Item> categoryItems = MySQLiteHelper.getInstance(getActivity().getBaseContext()).getItemsInCategory(categoryId);
            if(categoryItems!= null){
                Log.d("debug", "category items not null");
                items.addAll(categoryItems);
                adapter.notifyDataSetChanged();
            }else{*/
                Log.d("debug", "category items null");
                /*Client.get().getCategory(""+categoryId, new Callback<CategoryResponse>() {
                    @Override
                    public void success(CategoryResponse categoryResponse, Response response) {
                        Log.d("debug", "category success");
                        //alphaList.addAll(categoryResponse.getAlpha());
                        startDownloading();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("debug","fail");
                    }
                });*/
                startDownloading();
            //}
        }
    }

    private void startDownloading() {
        for(int i=0;i<38;i++){
            categories.add(i);
        }

        runnable = new Runnable() {
            @Override
            public void run() {

                if(alphaList.size()==0){
                    if(categories.size()==0){
                        return;
                    }

                    Log.d("debug", "category request"+categories.get(0));
                    Client.get().getCategory("" + categories.get(0), new Callback<CategoryResponse>() {
                        @Override
                        public void success(CategoryResponse categoryResponse, Response response) {
                            Log.d("debug", "category success");
                            alphaList.clear();
                            if(categoryResponse!=null){
                                alphaList.addAll(categoryResponse.getAlpha());
                                categories.remove(0);
                                categoryId=categories.get(0);
                            }
                            handler.post(runnable);
                            //startDownloading();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("debug", "fail");
                        }
                    });
                }

                    if (alphaList.size() != 0) {
                        Alpha alpha = alphaList.get(0);
                        Log.d("debug", "alpha items:"+alpha.getItems());
                        if (!alpha.getItems().equals("0")) {
                            int cycles = Integer.parseInt(alpha.getItems()) / 12;
                            if (Integer.parseInt(alpha.getItems()) % 12 != 0) {
                                cycles += 1;
                            }
                            Log.d("debug", "cycles:"+cycles);

                            for (int c = 1; c <= cycles; c++) {
                                Client.get().getItemsInCategory("" + categoryId, alpha.getLetter(), "" + c, new Callback<ItemResponse>() {
                                    @Override
                                    public void success(ItemResponse itemResponse, Response response) {
                                        //Log.d("debug", "success");
                                        //MySQLiteHelper.getInstance(getActivity()).addItemsInCategory(itemResponse.getItems(), categoryId);
                                        //items.addAll(itemResponse.getItems());
                                        //adapter.notifyDataSetChanged();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                        if(response.getBody() != null){
                                            for(Item item : itemResponse.getItems()){
                                                reference.child("items").child(item.getItemName()).setValue(item.getItemId());
                                            }
                                        }

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.d("debug", "fail");
                                    }
                                });
                                try {
                                    Log.d("debug", "sleepy time");
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (c == cycles) {
                                    Log.d("debug", "done downloading items for letter " + alphaList.get(0).getLetter()+ " in category: "+categoryId);
                                    alphaList.remove(0);
                                    handler.postDelayed(runnable,300);
                                }
                            }
                        } else { // this letter has no available items so lets skip it
                            alphaList.remove(0);
                            handler.postDelayed(runnable,300);
                        }
                    }
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{
        List<Item> items;

        public MyRecyclerAdapter(List<Item> items) {
            this.items=items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.row_items,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(items.get(position).getItemName());
            holder.price.setText(items.get(position).getCurrentPrice().getItemPrice());
            holder.trend.setText(items.get(position).getPriceChange().getItemPrice());

            Picasso.with(holder.itemView.getContext())
                    .load(items.get(position).getIcon())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.itemImage);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_image)
        ImageView itemImage;

        @BindView(R.id.item_title)
        TextView title;

        @BindView(R.id.item_price)
        TextView price;

        @BindView(R.id.item_trend)
        TextView trend;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.row_item)
        void onClick(){
            ChartFragment fragment = new ChartFragment();
            fragment.setItemId(items.get(getAdapterPosition()).getItemId());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
