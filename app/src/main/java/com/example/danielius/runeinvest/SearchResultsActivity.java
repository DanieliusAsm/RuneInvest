package com.example.danielius.runeinvest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danielius.runeinvest.api.model.FirebaseItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsActivity extends SearchActivity {

    DatabaseReference databaseReference;
    private ArrayList<FirebaseItem> items = new ArrayList<>();
    @BindView(R.id.my_items)
    RecyclerView recycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
        String queryText = getIntent().getStringExtra("query");
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

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(items);
        recycler.setAdapter(adapter);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        ArrayList<FirebaseItem> items;
        public RecyclerAdapter(ArrayList<FirebaseItem> items) {
            this.items=items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.row_items,parent,false);
            return new ViewHolder(v);
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
    }
}
