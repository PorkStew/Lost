package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

DatabaseReference databaseReference;


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
               databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child("-LcRKU6CF6dqtFovsf4_");
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       String name = dataSnapshot.child("userName").getValue().toString();
                       System.out.printf(name+" sssssssssssssssssssssssssssssssssssssss");
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment)
                        .addToBackStack(null)
                        .commit();


            }


        });

        Button loginB = (Button)view.findViewById(R.id.LoginB);

        loginB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// Get a reference to our posts




                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Navigation_DrawerFragment navigation_drawerFragment = new Navigation_DrawerFragment();
                fragmentTransaction.replace(R.id.fragment_container, navigation_drawerFragment)
                        .addToBackStack(null)
                        .commit();


                FragmentTransaction frs = getFragmentManager().beginTransaction();
                frs.replace(R.id.ShowFragments, new ProfileFragment());
                frs.commit();








                }


        });
    return view;

    }


}
