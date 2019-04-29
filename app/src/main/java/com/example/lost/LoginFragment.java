package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    //Variable constructors
    TextView username, password;
    Button registerB, loginB;
    String usernameDatabase = "";
    String passwordDatabase = "";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //links buttons and TextViews with variables;
        registerB =view.findViewById(R.id.RegisterB);
        loginB = view.findViewById(R.id.LoginB);
        username = view.findViewById(R.id.UsernameTXTB);
        password = view.findViewById(R.id.PasswordTXTB);
        //Register button listener which loads the Register Fragment
        registerB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment)
                        .addToBackStack(null)
                        .commit();
            }


        });
        //Login button listener
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets data from TextView into String
               final String UserName = username.getText().toString();
               final String Password = password.getText().toString();
                //checks data to see if the user entered anything
                if(TextUtils.isEmpty(UserName) || TextUtils.isEmpty(Password)){
                    Toast.makeText(getActivity(), "Information Missing!!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // takes the users username and checks Firebase to find any matches if not then an error message is printed out
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("USERS");
                myRef.orderByChild("username").equalTo(UserName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            users user = new users();
                            user.setID(childDataSnapshot.getKey());
                            usernameDatabase = childDataSnapshot.child("username").getValue().toString();
                            passwordDatabase = childDataSnapshot.child("password").getValue().toString();
                        }
                        if (UserName.equals(usernameDatabase) && passwordDatabase.equals(Password)) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            HomeFragment navigation_drawerFragment = new HomeFragment();
                            fragmentTransaction.replace(R.id.fragment_container, navigation_drawerFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else
                        {
                            Toast.makeText(getActivity(), "Incorrect user information, OR plz register!!", Toast.LENGTH_SHORT).show();
                            passwordDatabase = "";
                            usernameDatabase = "";
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }
}
