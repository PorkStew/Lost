package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    //Variable declarations
    TextView firstName, surname, username, email;
    Button submitB;
    boolean Stop = false;
    int testing = 0;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Links TextViews with Variables
        firstName = view.findViewById(R.id.firstNameTXTB);
        surname = view.findViewById(R.id.surnameTXTB);
        username = view.findViewById(R.id.usernameTXTB);
        email = view.findViewById(R.id.emailTXTB);
        submitB = view.findViewById(R.id.SubmitB);
        users user = new users();
        //sets submit button to be inactive at the start of the fragment
        submitB.setVisibility(View.GONE);
        //button to send changed data through
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitB.setVisibility(View.VISIBLE);
                Stop = true;
                String Firstname = firstName.getText().toString();
                String Surname = surname.getText().toString();
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                if (TextUtils.isEmpty(Firstname) || TextUtils.isEmpty(Surname) || TextUtils.isEmpty(Username) || TextUtils.isEmpty(Email)) {
                    Toast.makeText(getActivity(), "Data cannot be left blank!!!", Toast.LENGTH_SHORT).show();

                    return;
                }
                //takes data from TextViews and then inputs it into the database
                users user = new users();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child(user.getID());
                HashMap usermap = new HashMap();
                usermap.put("firstName", Firstname);
                usermap.put("surname", Surname);
                usermap.put("username", Username);
                usermap.put("email", Email);
                databaseReference.updateChildren(usermap);
            }
        });
        //TextView listener
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //used to prevent Button from being activated and only once data is changed
                if (testing == 1) {
                    submitB.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //TextView listener
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (testing == 1) {
                    submitB.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //TextView listener
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (testing == 1) {
                    submitB.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //TextView listener
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (testing == 1) {
                    System.out.println("in if testing eamil");
                    submitB.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("in else email");
                    testing = testing + 1;
                }
                if (Stop == true) {
                    System.out.println("in if stop");
                    submitB.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //gets the data from the user id collect when login in and uses it to find the users information which is then shown on screen
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USERS").child(user.getID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstName.setText(dataSnapshot.child("firstName").getValue().toString());
                surname.setText(dataSnapshot.child("surname").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
