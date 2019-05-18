package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment {
    Button SubmitB;
    String transportType;
    String metricSystem;
    String[] metricSystemData;
    String[] transportTypeData;
    boolean Stop = false;
    public PreferencesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        users u = new users();
        final View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        SubmitB = view.findViewById(R.id.SubmitB);
        SubmitB.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USERS").child(u.getID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transportType = dataSnapshot.child("transportType").getValue().toString();
                metricSystem = dataSnapshot.child("metricSystem").getValue().toString();
                if (metricSystem.equals("Kilometers")) {
                    metricSystemData = new String[]{metricSystem, "Miles"};

                } else if (metricSystem.equals("Miles")) {
                    metricSystemData = new String[]{metricSystem, "Kilometers"};

                }
                final Spinner metricSystemDataS = (Spinner) view.findViewById(R.id.metricSystemS);
                ArrayAdapter<String> metricSystemAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, metricSystemData);
                metricSystemDataS.setAdapter(metricSystemAdapter);
                metricSystemAdapter.notifyDataSetChanged();
                metricSystemDataS.setSelection(0,false);
                metricSystemDataS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SubmitB.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if (transportType.equals("car")) {
                    transportTypeData = new String[]{transportType, "bus", "train", "bike"};
                } else if (transportType.equals("bus")) {
                    transportTypeData = new String[]{transportType, "car", "train", "bike"};
                } else if (transportType.equals("train")) {
                    transportTypeData = new String[]{transportType, "car", "bus", "bike"};
                } else if (transportType.equals("bike")) {
                    transportTypeData = new String[]{transportType, "car", "bus", "train"};
                }
                final Spinner TransportTypeS = (Spinner) view.findViewById(R.id.transportTypeS);
                ArrayAdapter<String> TransportTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, transportTypeData);

                TransportTypeS.setAdapter(TransportTypeAdapter);
                TransportTypeAdapter.notifyDataSetChanged();
                TransportTypeS.setSelection(0,false);
                TransportTypeS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SubmitB.setVisibility(View.VISIBLE);
                        if(Stop == true)
                        {
                            SubmitB.setVisibility(View.GONE);
                            Stop = false;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                SubmitB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String TransportType = TransportTypeS.getSelectedItem().toString();
                        String MetricSystem = metricSystemDataS.getSelectedItem().toString();
                        users user = new users();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child(user.getID());
                        HashMap usermap = new HashMap(); // if you have data in diff data types go for HashMap<String,Object> or you can continue with HashMap<String,String>
                        usermap.put("metricSystem",MetricSystem);
                        usermap.put("transportType", TransportType);
                        databaseReference.updateChildren(usermap);
                        Stop = true;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
