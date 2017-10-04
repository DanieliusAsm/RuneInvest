package com.example.danielius.runeinvest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.adapters.MyItemsRecyclerAdapter;
import com.example.danielius.runeinvest.api.model.FirebaseItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouritesFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.my_items)
    RecyclerView recyclerView;
    MyItemsRecyclerAdapter adapter;
    ArrayList<FirebaseItem> items = new ArrayList();
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyItemsRecyclerAdapter(getActivity(),items);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites").orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               final String itemName = (String) dataSnapshot.getValue();
                Log.d("debug","user fav:"+itemName);
                Log.d("debug",FirebaseDatabase.getInstance().getReference("/items/"+itemName).toString());
                FirebaseDatabase.getInstance().getReference("/items/"+itemName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseItem item = dataSnapshot.getValue(FirebaseItem.class);
                        if(item!=null){
                            item.setName(itemName);
                            items.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("debug",databaseError.getDetails());
                    }
                });



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
