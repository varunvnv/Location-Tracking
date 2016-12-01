package com.mobileappclass.assignment3;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class server_db extends android.app.Fragment {

    private Activity myActivity;
    TextView network_type;
    ListView listView;
    ArrayList<String[]> list=new ArrayList<>();
    ArrayList<String[]> to_display=new ArrayList<>();
    ArrayList<String[]> updated_list=new ArrayList<>();
Handler timeHandler;
    Button sync_button;
    TextView server_status;
    SQLiteHelper helper;
    ListAdapter myadapter;
    public server_db() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper=new SQLiteHelper(getActivity(),null,null,1);
        View view = inflater.inflate(R.layout.fragment_server_db, container, false);
listView=(ListView)view.findViewById(R.id.server_db_list);
        network_type=(TextView)view.findViewById(R.id.network_type);
server_status=(TextView)view.findViewById(R.id.server_status);
        sync_button=(Button)view.findViewById(R.id.sync_button);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();
        HandlerThread hThread = new HandlerThread("name");
        hThread.start();
        Looper looper = hThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            public void run() {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Students/nvv6");

// Attach a listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        to_display.clear();
                        String x = dataSnapshot.getKey().toString();
                        //System.out.println("x is: " + x);
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String[] val = {" ", " ", " ", " "};
                            int i = 0;
                            for (DataSnapshot post : postSnapshot.getChildren()) {
                                String y = post.getValue().toString();
                                val[i] = y;
                                i++;
                                // System.out.println("y is: " + y);
                            }
                            to_display.add(0, val);
                        }
                        myadapter = new serveradapter(getActivity(), to_display);
                        listView.setAdapter(myadapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Failed to read value.", error.toException());
                    }
                });
            }
        });

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected_WIFI = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        boolean isConnected_MOBILE = activeNetwork != null &&activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

        if(isConnected_WIFI==true)
        {
            String ssid=null;
            WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo;

            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                ssid = wifiInfo.getSSID();
            }
            server_status.setText("CONNECTED");
                network_type.setText("WIFI: " + ssid);
        }
        else if(isConnected_MOBILE==true)
        {
            server_status.setText("NOT CONNECTED");
            network_type.setText("MOBILE");
        }
        else{
            server_status.setText("NOT CONNECTED");
            network_type.setText("NOT CONNECTED");
        }

        sync_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                try{
                    list=helper.getEntries();
                    for(int i=0;i<list.size();i++)
                    {
                        String[] val=list.get(i);
                        helper.add(val[0],val[1],val[2]);
                    }
                    helper.clear();
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(),"No local database found to upload",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}