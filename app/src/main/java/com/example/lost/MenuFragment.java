package com.example.lost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawer;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


       View view =inflater.inflate(R.layout.fragment_menu, container, false);
       Toolbar toolbar = view.findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        drawer = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        return view;


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_profile:
                FragmentTransaction profile = getFragmentManager().beginTransaction();
                profile.replace(R.id.fragment_container, new ProfileFragment());
                profile.add(R.id.fragment_container, new MenuFragment());
                profile.commit();
                break;
            case R.id.nav_navigation:
                FragmentTransaction navigation = getFragmentManager().beginTransaction();
                navigation.replace(R.id.fragment_container, new HomeFragment());
                navigation.add(R.id.fragment_container, new MenuFragment());
                navigation.commit();
                break;
            case R.id.nav_preferences:
                FragmentTransaction preferences = getFragmentManager().beginTransaction();
                preferences.replace(R.id.fragment_container, new PreferencesFragment());
                preferences.add(R.id.fragment_container, new MenuFragment());
                preferences.commit();
                break;
            case R.id.nav_information:
                FragmentTransaction information = getFragmentManager().beginTransaction();
                information.replace(R.id.fragment_container, new InformationFragment());
                information.add(R.id.fragment_container, new MenuFragment());
                information.commit();
                break;
            case R.id.nav_logout:
                FragmentTransaction logout = getFragmentManager().beginTransaction();
                logout.replace(R.id.fragment_container, new LoginFragment());
                logout.commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
