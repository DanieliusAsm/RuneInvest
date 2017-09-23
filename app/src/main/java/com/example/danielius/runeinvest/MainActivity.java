package com.example.danielius.runeinvest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.danielius.runeinvest.fragments.CategoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends SearchActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment())
                .commit();

        //Intent service = new Intent(MainActivity.this, GrandExchangeCheck.class);
        //startService(service);

        // main activity - tavo dabartine klase, secondactivity tavo kita klase i kuria keisi
       // Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        //startActivity(intent);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //change fragment here
        }else{}
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
