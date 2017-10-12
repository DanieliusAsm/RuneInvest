package com.example.danielius.runeinvest.api.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Danielius on 2017-09-23.
 */
@IgnoreExtraProperties
public class FirebaseItem {

    private long id;
    private String name;
    private int price;
    private DocumentReference itemReference;

    public FirebaseItem(long id){
        this.id=id;
    }

    public FirebaseItem(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public DocumentReference getItemReference() {
        return itemReference;
    }

    public void setItemReference(DocumentReference itemReference) {
        this.itemReference = itemReference;
    }
}
