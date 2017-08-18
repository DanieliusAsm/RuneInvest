package com.example.danielius.runeinvest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Danielius on 2017-08-18.
 */

public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
