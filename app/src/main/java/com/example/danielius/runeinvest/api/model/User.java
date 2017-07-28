package com.example.danielius.runeinvest.api.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Danielius on 2017-06-10.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;

    public User(){}

    public User(String name, String email){
        this.name=name;
        this.email=email;
    }
}
