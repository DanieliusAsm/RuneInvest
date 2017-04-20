package com.example.danielius.runeinvest;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.danielius.runeinvest.fragments.CategoryFragment;
import com.example.danielius.runeinvest.fragments.ItemsFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment())
                .commit();

        //Intent service = new Intent(MainActivity.this, GrandExchangeCheck.class);
        //startService(service);

        // main activity - tavo dabartine klase, secondactivity tavo kita klase i kuria keisi
       // Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        //startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("Ar tikrai norite iseiti ?")
                    .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Taip", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
