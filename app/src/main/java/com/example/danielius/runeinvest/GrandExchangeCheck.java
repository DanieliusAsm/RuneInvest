package com.example.danielius.runeinvest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Danielius on 06-Sep-15.
 */
public class GrandExchangeCheck extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "Service was started");
       //Notification notification = new Notification(R.mipmap.ic_launcher,"hahahahdshdsa",1);
        //getApplicationContext().getSystemService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "Service was destroyed");
    }
}
