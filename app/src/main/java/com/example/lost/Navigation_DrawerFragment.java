package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Navigation_DrawerFragment extends Fragment {
    //constructors
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public Navigation_DrawerFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation__drawer, container, false);
        //create toolbar listener
        toolbar = view.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //creates an action bar to which a button is added to open drawer
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout = view.findViewById(R.id.drawer_layout);

        navigationView = view.findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        menuItem.setChecked(true);
                        ProfileFragment profileFragment = new ProfileFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, profileFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_navigation:
                        menuItem.setChecked(true);
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, homeFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_preferences:
                        menuItem.setChecked(true);
                        PreferencesFragment preferencesFragment = new PreferencesFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, preferencesFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_about:
                        menuItem.setChecked(true);
                        InformationFragment informationFragment = new InformationFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, informationFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        menuItem.setChecked(true);
                        LoginFragment loginFragment = new LoginFragment();
                        fragmentTransaction.replace(R.id.fragment_container, loginFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        drawerLayout.closeDrawers();

                        return true;
                }
                return false;
            }
        });

        return view;
    }

    private void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
