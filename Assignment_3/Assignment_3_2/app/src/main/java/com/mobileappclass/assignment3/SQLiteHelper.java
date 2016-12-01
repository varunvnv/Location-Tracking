package com.mobileappclass.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "coordinates.db";
    private static final String TABLE_NAME = "coordinates_table";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";
    private static final String COLUMN_NETID="netid";
    public static final String NET_ID="nvv6";
    public static final String students="Students";
    Context context;
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_TIMESTAMP + " TEXT," +
                COLUMN_LAT + " TEXT," +
                COLUMN_LNG + " TEXT);";
        db.execSQL(query);
    }

        @Override
        public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){

        }

    public void insertEntry(String timestamp, String lat,String lng){
        System.out.println("inside insert");
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COLUMN_TIMESTAMP,timestamp);
        content.put(COLUMN_LAT,lat);
        content.put(COLUMN_LNG,lng);
        //content.put(COLUMN_NETID,"nvv6");
        db.insert(TABLE_NAME,null,content);
        System.out.println("added to local db");
    }

    public ArrayList<String[]> getEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        cursor.moveToFirst();

        ArrayList<String[]> values = new ArrayList<>();

        do{
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            String lat = cursor.getString(cursor.getColumnIndex(COLUMN_LAT));
            String lng = cursor.getString(cursor.getColumnIndex(COLUMN_LNG));
            String []value={time,lat,lng};

            values.add(value);

        }while (cursor.moveToNext());

        return values;
    }

    public Cursor customQuery(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public void clear()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<String[]> getEntriesDesc()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        cursor.moveToFirst();

        ArrayList<String[]> values = new ArrayList<>();

        do{
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            String lat = cursor.getString(cursor.getColumnIndex(COLUMN_LAT));
            String lng = cursor.getString(cursor.getColumnIndex(COLUMN_LNG));
            String []value={time,lat,lng};

            values.add(0,value);

        }while (cursor.moveToNext());

        return values;
    }

    public void update(String timestamp,String Latitude,String Longitude){
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected_WIFI = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        boolean isConnected_MOBILE = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

        if(isConnected_WIFI==true)
        {
            String[] t=timestamp.split("/");
            String time=t[0]+"-"+t[1];

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
            DatabaseReference students = ref.child(NET_ID);
            DatabaseReference bart = students.child(time);
            bart.child("date").setValue(timestamp);
            bart.child("netid").setValue(NET_ID);
            bart.child("x").setValue(Latitude);
            bart.child("y").setValue(Longitude, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    System.out.println("Added to database");
                }
            });
        }
        else{
            insertEntry(timestamp,Latitude,Longitude);
        }
    }

    public void add(String timestamp,String Latitude,String Longitude){
        String[] t=timestamp.split("/");
        String time=t[0]+"-"+t[1];
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
        DatabaseReference students = ref.child(NET_ID);
        DatabaseReference bart = students.child(time);
        bart.child("date").setValue(timestamp);
        bart.child("netid").setValue(NET_ID);
        bart.child("x").setValue(Latitude);
        bart.child("y").setValue(Longitude, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                System.out.println("Added to database");
            }
        });
    }
}
