package com.mobileappclass.assignment3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by varun on 11/6/2016.
 */

public class serveradapter extends ArrayAdapter<String[]> {
public serveradapter(Context context, ArrayList<String[]> resource) {
    super(context, R.layout.newadapterlayout, resource);
}

    TextView time;
    TextView lat;
    TextView lng;
    TextView netid;
    @Override
    public View getView(final int index, View row, ViewGroup parent) {

        LayoutInflater minflater=LayoutInflater.from(getContext());
        row=minflater.inflate(R.layout.newadapterlayout,parent,false);
        time=(TextView)row.findViewById(R.id.time);
        lat=(TextView)row.findViewById(R.id.lat);
        lng=(TextView)row.findViewById(R.id.lng);
        netid=(TextView)row.findViewById(R.id.netid);

        String[] val=getItem(index);

        time.setText(val[0]);
        lat.setText(val[2]+", ");
        lng.setText(val[3]+", ");
        netid.setText(val[1]);

        return row;
        }
        }