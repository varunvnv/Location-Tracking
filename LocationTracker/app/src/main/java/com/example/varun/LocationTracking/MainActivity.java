package com.example.varun.LocationTracking;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
//import android.icu.util.Calendar;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    PendingIntent pendingIntent;
    AlarmManager am;
    Calendar calendar;
    Timestamp timestamp;
    String Lat;
    String Lng;
    String time;
    ArrayList<String[]> list = new ArrayList<>();
    String mode = "local";
SQLiteDatabase dbase;

    /*private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };*/
    private static final int LOCATION_REQUEST = 1337 + 3;
    Toolbar toolbar;
    private FirebaseDatabase db;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Calling service");

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            local_db_server_frag frag = new local_db_server_frag();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bun = new Bundle();
            bun.putSerializable("list", list);
            transaction.replace(R.id.local_frame, frag);
            transaction.addToBackStack(null);
            frag.setArguments(bun);
            transaction.commit();

            server_db frag2 = new server_db();
            android.app.FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
            Bundle bun2 = new Bundle();
            bun2.putSerializable("list", list);
            transaction2.replace(R.id.remote_frame, frag2);
            transaction2.addToBackStack(null);
            frag2.setArguments(bun);
            transaction2.commit();

        }
        else{
            local_db_server_frag frag = new local_db_server_frag();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bun = new Bundle();
            bun.putSerializable("list", list);
            transaction.replace(R.id.frame, frag);
            transaction.addToBackStack(null);
            frag.setArguments(bun);
            transaction.commit();
        }



        if(savedInstanceState!=null)
        {
            list=(ArrayList<String[]>)savedInstanceState.getSerializable("list");
        }


            Intent intent = new Intent(this, GPS_coordinates_service.class);
            startService(intent);

    IntentFilter filter = new IntentFilter();
    filter.addAction("action");
    registerReceiver(new ReceiverClassName(), filter);

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private class ReceiverClassName extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //unregisterReceiver(context);
            Lat = intent.getStringExtra("Lat");
            Lng = intent.getStringExtra("Lng");
            time = intent.getStringExtra("time");

            String[] val = {time, Lat, Lng};
            list.add(0, val);

            if (mode.equals("local")) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    local_db_server_frag frag = new local_db_server_frag();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle bun = new Bundle();
                    bun.putSerializable("list", list);
                    transaction.replace(R.id.local_frame, frag);
                    transaction.addToBackStack(null);
                    frag.setArguments(bun);
                    transaction.commit();
                } else {
                    System.out.println("after adding");
                    local_db_server_frag frag = new local_db_server_frag();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle bun = new Bundle();
                    bun.putSerializable("list", list);
                    transaction.replace(R.id.frame, frag);
                    transaction.addToBackStack(null);
                    frag.setArguments(bun);
                    transaction.commit();
                }
            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInstanceState", "onSaveInstanceState called");
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", list);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // reads XML
        inflater.inflate(R.menu.menu_main, menu); // to create
        return super.onCreateOptionsMenu(menu); // the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_online) {
            mode = "server";
            server_db frag = new server_db();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bun = new Bundle();
            transaction.replace(R.id.frame, frag);
            transaction.addToBackStack(null);
            frag.setArguments(bun);
            transaction.commit();
        } else if (item.getItemId() == R.id.action_offline) {
            mode = "local";
            local_db_server_frag frag = new local_db_server_frag();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bun = new Bundle();
            bun.putSerializable("list", list);
            transaction.replace(R.id.frame, frag);
            transaction.addToBackStack(null);
            frag.setArguments(bun);
            transaction.commit();
        } else if (item.getItemId() == R.id.action_query) {
            mode = "Query";
            QueryFragment frag = new QueryFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bun = new Bundle();
            bun.putSerializable("list", list);
            transaction.replace(R.id.frame, frag);
            transaction.addToBackStack(null);
            frag.setArguments(bun);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
