package com.example.varun.LocationTracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GPS_coordinates_service extends Service {
    ArrayList<String>Lat=new ArrayList<>();
    ArrayList<String>Lng=new ArrayList<>();
    ArrayList<String>time=new ArrayList<>();
    String Latitude;
    String Longitude;
    Handler timeHandler;
    private static final int WAIT_TIME = 10000;
    SQLiteHelper helper;
    ArrayList<String[]> list=new ArrayList<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
System.out.println("inside service");

        helper=new SQLiteHelper(this,null,null,1);
                timeHandler = new Handler();
                Runnable timeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc == null) {
                                System.out.println("location is null");
                                loc = locationManager.getLastKnownLocation(
                                        LocationManager.NETWORK_PROVIDER);
                            }

                        if (loc != null) {
                        final double myLat = loc.getLatitude();
                        final double myLng = loc.getLongitude();
                        Latitude = Double.toString(myLat);
                        Longitude = Double.toString(myLng);
                        System.out.println("in thread");
                        System.out.println("coordinates are: " + Latitude + " " + Longitude);
                        Long tsLong = System.currentTimeMillis();
                        Date df = new java.util.Date(tsLong);
                        String timestamp = new SimpleDateFormat("MM/dd hh:mm:ss a:").format(df);
                        System.out.println(timestamp);

//helper.clear();

                            ConnectivityManager cm =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            boolean isConnected_WIFI = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                            boolean isConnected_MOBILE = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

                            if(isConnected_WIFI==true) {
                                helper.update(timestamp, Latitude, Longitude);
                            }
                            else{
                                System.out.println("adding to local db");
                                helper.insertEntry(timestamp, Latitude, Longitude);
                            }
                        Intent done = new Intent();
                        done.setAction("action");
                        done.putExtra("Lat", Latitude);
                        done.putExtra("Lng", Longitude);
                        done.putExtra("time", timestamp);
                        sendBroadcast(done);
                    }}catch (SecurityException e){
                            e.printStackTrace();
                        }
                        timeHandler.postDelayed(this, WAIT_TIME);
                    }
                };
                timeHandler.postDelayed(timeRunnable, 10);
                // thread.start();




        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
