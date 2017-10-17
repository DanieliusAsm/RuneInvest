package com.example.danielius.runeinvest.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.api.model.FirebaseItem;
import com.example.danielius.runeinvest.fragments.ChartFragment;
import com.example.danielius.runeinvest.fragments.SearchResultsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

//Reusable adapter used in SearchResultsFragment, ItemsFragment and FavouritesFragment.
public class MyItemsRecyclerAdapter extends RecyclerView.Adapter<MyItemsRecyclerAdapter.ViewHolder> {

    ArrayList<FirebaseItem> items;
    AppCompatActivity activity;
    private ActionMode mActionMode;
    ArrayList<Integer> selectedItems;
    ActionMode.Callback mCallback;

    public MyItemsRecyclerAdapter(Activity activity, ArrayList<FirebaseItem> items,ArrayList<Integer> selectedItems) {
        this.activity = (AppCompatActivity) activity;
        this.items = items;
        this.selectedItems = selectedItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = activity.getLayoutInflater().inflate(R.layout.row_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getName());
        holder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setActionModeCallback(ActionMode.Callback callback){
        this.mCallback = callback;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public void setActionMode(ActionMode mActionMode) {
        this.mActionMode = mActionMode;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.row_item)
        void onClick() {
            ChartFragment fragment = new ChartFragment();
            Bundle bundle = new Bundle();
            bundle.putString("item_id", Long.toString(items.get(getAdapterPosition()).getId()));
            fragment.setArguments(bundle);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        @OnLongClick(R.id.row_item)
        boolean onLongClick(View v) {
            Log.d("debug", "onLongClick");
            if(mCallback == null){
                return true;
            }

            if (mActionMode == null) {
                activity.getSupportActionBar().hide();
                mActionMode = activity.startSupportActionMode(mCallback);

                Log.d("debug", "Adding selected item");
                selectedItems.add(getAdapterPosition());
                v.setBackgroundColor(Color.LTGRAY);
                mActionMode.setTitle("" + selectedItems.size());
            } else {
                Integer selectedItemIndex = getAdapterPosition();
                if (selectedItems.contains(selectedItemIndex)) {
                    Log.d("debug", "Removing selected item");
                    selectedItems.remove(selectedItemIndex);
                    v.setBackgroundColor(Color.WHITE);
                } else {
                    Log.d("debug", "Adding selected item");
                    selectedItems.add(getAdapterPosition());
                    v.setBackgroundColor(Color.LTGRAY);
                }
                mActionMode.setTitle("" + selectedItems.size());
            }

            return true;
        }
    }
}