package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button registerB = (Button)view.findViewById(R.id.RegisterB);

        registerB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new RegisterFragment());
                fr.commit();


            }


        });

        Button loginB = (Button)view.findViewById(R.id.LoginB);

        loginB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction =fragmentManager.beginTransaction();
                Fragment fragment = new HomeFragment(); // your fragment to add
                transaction.replace(R.id.fragment_container, fragment); // a string in case you need to check with fragment is currently visible.
                transaction.addToBackStack(null);
                transaction.commit();

                FragmentManager fragmentManagers = getFragmentManager();
                FragmentTransaction transactions =fragmentManagers.beginTransaction();
                Fragment fragments = new MenuFragment(); // your fragment to add
                transactions.replace(R.id.fragment_container, fragments); // a string in case you need to check with fragment is currently visible.
                transactions.addToBackStack(null);
                transactions.commit();








                }


        });
    return view;

    }

}
