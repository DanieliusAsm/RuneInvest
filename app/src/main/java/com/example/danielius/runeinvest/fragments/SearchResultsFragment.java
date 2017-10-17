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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultsFragment extends Fragment {

    private ArrayList<FirebaseItem> items = new ArrayList<>();
    @BindView(R.id.my_items)
    RecyclerView recycler;
    MyItemsRecyclerAdapter adapter;
    Unbinder unbinder;
    ArrayList<Integer> selectedItems = new ArrayList<>();
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String queryText = getArguments().getString("query");
        firebaseFirestore.collection("items").orderBy("name").startAt(queryText).endAt(queryText+"\uf8ff").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for(DocumentSnapshot document : documentSnapshots.getDocuments()){
                    Log.d("debug","data:"+document.getData());
                    FirebaseItem item = document.toObject(FirebaseItem.class);
                    item.setItemReference(document.getReference());
                    Log.d("debug","snapshot id:"+item.getName());
                    Log.d("debug","--------------");
                    items.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("debug","Error:"+e.getMessage());
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyItemsRecyclerAdapter(getActivity(),items,selectedItems);
        recycler.setAdapter(adapter);

        adapter.setActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.d("debug", "onCreateActionMode");
                mode.getMenuInflater().inflate(R.menu.menu_cab_search,menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_favourite:
                        for(int i=0;i<selectedItems.size();i++){
                            int selectedItem = selectedItems.get(i);
                            FirebaseItem firebaseItem = items.get(selectedItem);
                            Map<String, Object> favourite = firebaseItem.toHashMap();

                            // TODO check if exists already. Duplicates..
                            firebaseFirestore.document("/users/"+FirebaseAuth.getInstance().getUid()+"/favourites/"+firebaseItem.getName()).set(favourite);
                        }
                        break;
                    case R.id.action_delete:
                        Log.d("debug", "items size:"+items.size());
                        for(int i=0;i<selectedItems.size();i++){
                            int selectedItem = selectedItems.get(i);
                            items.remove(selectedItem-i);
                        }
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
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
