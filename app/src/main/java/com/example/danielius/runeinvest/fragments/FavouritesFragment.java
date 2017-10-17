package com.example.danielius.runeinvest.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.danielius.runeinvest.R;
import com.example.danielius.runeinvest.adapters.MyItemsRecyclerAdapter;
import com.example.danielius.runeinvest.api.model.FirebaseItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouritesFragment extends Fragment {


    public static final String TAG = "debug";
    Unbinder unbinder;
    @BindView(R.id.my_items)
    RecyclerView recyclerView;
    MyItemsRecyclerAdapter adapter;
    ArrayList<FirebaseItem> items = new ArrayList();
    DatabaseReference databaseReference;
    DocumentReference ref;
    private ArrayList<Integer> selectedItems = new ArrayList<>();

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
        adapter = new MyItemsRecyclerAdapter(getActivity(),items, selectedItems);
        recyclerView.setAdapter(adapter);
        adapter.setActionModeCallback(new MyActionModeCallback());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        CollectionReference ref = FirebaseFirestore.getInstance().collection("users/"+auth.getUid()+"/favourites");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();

                    for(DocumentSnapshot documentSnapshot : snapshot.getDocuments()){
                        Log.d(TAG,"data:"+documentSnapshot.getData());
                        FirebaseItem item = documentSnapshot.toObject(FirebaseItem.class);
                        items.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Log.w(TAG,"error getting favourites "+task.getException());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Log.d("debug", "onCreateActionMode");
            mode.getMenuInflater().inflate(R.menu.menu_cab_favourites,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_delete:
                    Log.d("debug", "items size:"+items.size());
                    WriteBatch batch = FirebaseFirestore.getInstance().batch();
                    for(int i=0;i<selectedItems.size();i++){
                        int selectedItem = selectedItems.get(i);
                        FirebaseItem firebaseItem = items.get(selectedItem);

                        batch.delete(FirebaseFirestore.getInstance().document("users/"+FirebaseAuth.getInstance().
                                getUid()+"/favourites/"+firebaseItem.getName()));
                        items.remove(selectedItem-i);
                        firebaseItem.getItemReference().set(firebaseItem);
                    }
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //babalalbalbal
                        }
                    });
                    break;
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Log.d("debug", "onDestroyActionMode");
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            adapter.setActionMode(null);

            selectedItems.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
