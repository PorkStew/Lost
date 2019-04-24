package com.example.lost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //creates data for fueltype spinner and creates a listener for the spinner
        String[] data = {"none", "unleaded 93", "unleaded 95", "diesel"};
        final Spinner FuelTypeS = (Spinner) view.findViewById(R.id.FuelTypeS);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
        FuelTypeS.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        FuelTypeS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //creates data for transport type spinner and creates a listener for the spinner
        String[] TransPortTypeData = {"none","car", "bus", "train", "bike"};
        final Spinner TransportTypeS = (Spinner) view.findViewById(R.id.TransportTypeS);
        ArrayAdapter<String> TransportTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, TransPortTypeData);
        TransportTypeS.setAdapter(TransportTypeAdapter);
        TransportTypeAdapter.notifyDataSetChanged();
        TransportTypeS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //creates data for metric system spinner and creates a listener for the spinner
        String[] metricSystemData = {"none","Kilometers", "Miles"};
        final Spinner metricSystemDataS = (Spinner) view.findViewById(R.id.metricSystemS);
        ArrayAdapter<String> metricSystemAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, metricSystemData);

        metricSystemDataS.setAdapter(metricSystemAdapter);
        metricSystemAdapter.notifyDataSetChanged();
        metricSystemDataS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // variable declarations and links EditView with a TextView
        Button submitB = view.findViewById(R.id.SubmitB);
        final EditText firstName = view.findViewById(R.id.FirstNameTXTB);
        final EditText surname = view.findViewById(R.id.SurnameTXTB);
        final EditText username = view.findViewById(R.id.UsernameTXTB);
        final EditText emailAddress = view.findViewById(R.id.EmailTXTB);
        final EditText password = view.findViewById(R.id.PasswordTXTB);
        final EditText confirmPassword = view.findViewById(R.id.ConfirmPasswordTXTB);
        //submit button listener which gets text from each TextView and puts it into a String
        submitB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String FirstName = firstName.getText().toString();
                String Surname = surname.getText().toString();
                String UserName = username.getText().toString();
                String EmailAddress = emailAddress.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();
                String TransportType = TransportTypeS.getSelectedItem().toString();
                String FuelType = FuelTypeS.getSelectedItem().toString();
                String MetricSystem = metricSystemDataS.getSelectedItem().toString();
                //Checks data to see if requirements specified are meet
                if (TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(Surname) || TextUtils.isEmpty(UserName) || TextUtils.isEmpty(EmailAddress) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(ConfirmPassword)) {
                    Toast.makeText(getActivity(), "Information missing!!!",
                            Toast.LENGTH_SHORT).show();
                } else if(!EmailAddress.contains("@")){
                    Toast.makeText(getActivity(), "Email must contain E.G @gmail.com",
                            Toast.LENGTH_SHORT).show();
                } else if (TransportType.equals("car") && FuelType.equals("none")) {
                    Toast.makeText(getActivity(), "Please select a fuel type!!",
                            Toast.LENGTH_SHORT).show();
                } else if(TransportType.equals("none")) {
                    Toast.makeText(getActivity(), "Transport type must be selected!!!",
                            Toast.LENGTH_SHORT).show();
                }else if(MetricSystem.equals("none")){
                    Toast.makeText(getActivity(), "Please select a metric system!!!",
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    //inputs data into a class call users and then inputs that into Firebase and a database class
                    users user = new users(FirstName, Surname, UserName, EmailAddress, FuelType, TransportType, Password, MetricSystem);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("USERS");
                    databaseReference.push().setValue(user);
                    //calls login fragment
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new LoginFragment());
                    Toast.makeText(getActivity(), "Registration Successful!!", Toast.LENGTH_SHORT).show();
                    fr.commit();
                }
            }
        });
        return view;

    }

}
