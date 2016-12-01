package com.mobileappclass.assignment3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class QueryFragment extends android.app.Fragment {

    private Activity myActivity;
    ListView listView;
    ArrayList<String[]> list=new ArrayList<>();
    Button enter_button;
    Spinner spinner;
    String action=" ";
    ArrayList<String[]>to_display=new ArrayList<>();
    ListAdapter myadapter;
    EditText netid;
    public QueryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_query, container, false);
        listView=(ListView)view.findViewById(R.id.server_db_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();

        enter_button=(Button)myActivity.findViewById(R.id.enter_button);
        spinner=(Spinner)myActivity.findViewById(R.id.spinner);
        netid=(EditText)myActivity.findViewById(R.id.netid);

enter_button.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v)
    {
try{
            String to_find=netid.getText().toString();
            if (to_find.equals(""))
            {
                Toast.makeText(getActivity(),"Enter a netid",Toast.LENGTH_SHORT).show();
            }
            else {
                populate(to_find);
                if (action.equals("Ascending")) {
                    myadapter = new serveradapter(getActivity(), list);
                    listView.setAdapter(myadapter);
                } else {
                    myadapter = new serveradapter(getActivity(), to_display);
                    listView.setAdapter(myadapter);
                }
            }
    }
catch (Exception e)
{
    Toast.makeText(getActivity(),"Enter a valid netid",Toast.LENGTH_SHORT).show();
}
    }
});
        }

    public void populate(final String to_find){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Students/"+to_find);

// Attach a listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // String x = dataSnapshot.getKey().toString();
                        //System.out.println("x is: " + x);
                        to_display.clear();
                        list.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String[] val = {" ", " ", " ", " "};
                            int i = 0;
                            for (DataSnapshot post : postSnapshot.getChildren()) {
                                String y = post.getValue().toString();
                                val[i] = y;
                                i++;
                                //System.out.println("y is: " + y);
                            }
                            to_display.add(0, val);
                            list.add(val);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Failed to read value.", error.toException());
                    }
                });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> spin, View v, int i, long id) {
                action = (String) spinner.getSelectedItem();
            }
            public void onNothingSelected(AdapterView<?> parent) {} // empty
        });
            }
        });
        thread.start();
    }
}
