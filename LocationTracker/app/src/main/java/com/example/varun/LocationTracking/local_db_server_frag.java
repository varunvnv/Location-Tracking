package com.example.varun.LocationTracking;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.Handler;


public class local_db_server_frag extends android.app.Fragment {

    private Activity myActivity;
    ArrayList<String[]> list=new ArrayList<>();
    ListView listView;
View view;
    Handler timeHandler;
    public local_db_server_frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_local_db_server, container, false);
        listView=(ListView)view.findViewById(R.id.local_db_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("Bundle", "BUNDLE NOT NULL");
            list = (ArrayList<String[]>) bundle.getSerializable("list");
            if(list!=null) {
              populate();
            }
        } else {
            Log.d("Bundle", "BUNDLE IS NULL");
        }
    }

    public void populate(){
        ListAdapter adapter = new myAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }


   /* public void update_list(ArrayList<String[]> new_list)
    {
        list=new_list;
        populate();
    }*/
}