package com.example.danielius.runeinvest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.danielius.runeinvest.api.Client;
import com.example.danielius.runeinvest.api.model.Alpha;
import com.example.danielius.runeinvest.api.response.CategoryResponse;
import com.example.danielius.runeinvest.api.response.GraphResponse;
import com.example.danielius.runeinvest.api.response.ItemResponse;
import com.example.danielius.runeinvest.sqlite.MySQLiteHelper;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        /*Client.get().getItemGraph("4698", new Callback<GraphResponse>() {
            @Override
            public void success(GraphResponse graphResponse, Response response) {
                Log.d("debug", "success");

                for (Map.Entry<String, String> entry : graphResponse.getData().entrySet()) {
                    long time = Long.parseLong(entry.getKey());
                    long lastSaved = Prefs.getLong("updateTime", 0);
                    if (time > lastSaved) {
                        //update
                        Prefs.putLong("updateTime", time);

                        displayNotification();
                    } else {
                        //dont update
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("debug", "fail");
            }
        });*/
;

        //Prefs.putBoolean("initialized",false);
        //if(!Prefs.getBoolean("initialized",false)){
            //for(int i=0;i<38;i++){
                //loadItems(i);
            //}
        //}else{
            //Prefs.putBoolean("initialized",true);
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            Intent intent2 = new Intent(LoadingActivity.this, ChartActivity.class);
            startActivity(intent);
            finish();
       // }
    }

    private void loadItems(final int category) {
        Client.get().getCategory(""+category, new Callback<CategoryResponse>() {
            @Override
            public void success(CategoryResponse categoryResponse, Response response) {
                Log.d("debug","category success");
                for(int i=1;i<categoryResponse.getAlpha().size();i++){
                    Alpha alpha = categoryResponse.getAlpha().get(i);
                    if(Integer.parseInt(alpha.getItems())>0){
                        int cycles = Integer.parseInt(alpha.getItems())/12;
                        if(Integer.parseInt(alpha.getItems())%12!=0){
                            cycles+=1;
                        }

                        for(int c = 1;c<=cycles;c++){
                            Client.get().getItemsInCategory("" + category, alpha.getLetter(), "" + c, new Callback<ItemResponse>() {
                                @Override
                                public void success(ItemResponse itemResponse, Response response) {
                                    Log.d("debug", "success");
                                    MySQLiteHelper.getInstance(getBaseContext()).addItemsInCategory(itemResponse.getItems(), category);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d("debug","fail");
                                }
                            });
                            if(c==cycles && category == 37){
                                Prefs.putBoolean("initialized",true);
                                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("debug","fail");
            }
        });
    }
    public void displayNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Updated")
                .setContentText("Database updated");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        int notificationId = 001;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationId, mBuilder.build());
    }
}
