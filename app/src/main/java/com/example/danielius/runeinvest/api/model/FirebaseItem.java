package com.example.danielius.runeinvest.api.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Danielius on 2017-09-23.
 */
@IgnoreExtraProperties
public class FirebaseItem {

    long id;
    String name;

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
}
