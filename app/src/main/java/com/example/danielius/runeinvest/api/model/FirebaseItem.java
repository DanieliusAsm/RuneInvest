package com.example.danielius.runeinvest.api.model;

/**
 * Created by Danielius on 2017-09-23.
 */

public class FirebaseItem {

    private long id;
    private String name;

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
