package com.example.danielius.runeinvest.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.adapters.MyItemsRecyclerAdapter;
import com.example.danielius.runeinvest.api.model.FirebaseItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

public class SearchResultsFragment extends Fragment {

    DatabaseReference databaseReference;
    private ArrayList<FirebaseItem> items = new ArrayList<>();
    @BindView(R.id.my_items)
    RecyclerView recycler;
    MyItemsRecyclerAdapter adapter;
    Unbinder unbinder;
    ArrayList<Integer> selectedItems = new ArrayList<>();
    ActionMode mActionMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        String queryText = getArguments().getString("query");
        databaseReference.orderByKey().startAt(queryText).endAt(queryText+"\uf8ff")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("debug","onChildAdded");
                        FirebaseItem item = dataSnapshot.getValue(FirebaseItem.class);
                        Log.d("debug","snapshot id:"+item.getId());
                        Log.d("debug","snapshot key:"+dataSnapshot.getKey());
                        Log.d("debug","--------------");
                        item.setName(dataSnapshot.getKey());
                        items.add(item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("debug","onChildChanged");
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("debug","onChildRemoved");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("debug","onChildMoved");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("debug","onCancelled");
                    }
                });

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyItemsRecyclerAdapter(getActivity(),items);
        recycler.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
