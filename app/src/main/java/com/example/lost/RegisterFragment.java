package com.example.lost;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */

public class RegisterFragment extends Fragment {

    public RegisterFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("its working yeah!!!!");

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button submitB = (Button)view.findViewById(R.id.SubmitB);

        submitB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                users user = new users();


            }


        });
        return view;

    }

    }

}
