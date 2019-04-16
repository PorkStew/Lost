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

        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        displayMessage("profile selected....");
                        menuItem.setChecked(true);
                        ProfileFragment fragment = new ProfileFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, fragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_navigation:
                        displayMessage("Navigation selected....");
                        menuItem.setChecked(true);
                        HomeFragment f = new HomeFragment();
                        fragmentTransaction.replace(R.id.ShowFragments, f)
                                .addToBackStack(null)
                                .commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
