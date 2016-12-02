package com.example.varun.LocationTracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by varun on 10/31/2016.
 */


public class myAdapter extends ArrayAdapter<String[]> {
    public myAdapter(Context context, ArrayList<String[]> resource) {
        super(context, R.layout.adapterlayout, resource);
    }

    TextView time;
    TextView latitude;
    TextView longitude;
    @Override
    public View getView(final int index, View row, ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        row = minflater.inflate(R.layout.adapterlayout, parent, false);
        time=(TextView)row.findViewById(R.id.time);
        latitude=(TextView)row.findViewById(R.id.latitude);
        longitude=(TextView)row.findViewById(R.id.longitude);

        String[] val=getItem(index);
        time.setText(val[0]);
        latitude.setText(val[1]+",");
        longitude.setText(val[2]);
        return row;
    }
}

