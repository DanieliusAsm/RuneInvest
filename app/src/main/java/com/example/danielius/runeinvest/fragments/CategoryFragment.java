package com.example.danielius.runeinvest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.model.Category;
import com.example.danielius.runeinvest.api.model.Item;
import com.example.danielius.runeinvest.sqlite.MySQLiteHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CategoryFragment extends Fragment {

    @BindView(R.id.my_items)
    RecyclerView recycler;

    private MyRecyclerAdapter adapter;
    List<Category> items = new ArrayList<Category>();
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        adapter = new MyRecyclerAdapter(items);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);

        if(items.size()==0){
            items.addAll(MySQLiteHelper.getInstance(getActivity().getBaseContext()).getCategories());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

        List<Category> items;

        public MyRecyclerAdapter(List<Category> items) {
            this.items=items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.row_category,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.item_category)
        void onClick(){
            ItemsFragment fragment = new ItemsFragment();
            fragment.setCategoryId(getAdapterPosition());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
