package com.example.lost;


import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PermissionsListener{
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap MainMapboxMap;

    //variable declarations for buttons
    private Button startButton;
    private static final int requestCode = 1;
    private FloatingActionButton searchBarB;

    //This will get the best route
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationRoute;
    private static final String TAG = "DirectionsActivity";
    PermissionsManager permissionsManager = new PermissionsManager(this);
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //this gets the MapBox token to allow map displaying
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //links views together between map and buttons
        mapView = view.findViewById(R.id.mapViews);
        startButton = view.findViewById(R.id.startB);
        searchBarB = view.findViewById(R.id.searchBarB);
        //start navigation button listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder().directionsRoute(currentRoute).shouldSimulateRoute(false).build();
                NavigationLauncher.startNavigation(getActivity(), options);
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                //this iniziates the map
                MainMapboxMap = mapboxMap;

            mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

                    MainMapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                        @Override
                        public boolean onMapClick(@NonNull LatLng point) {
                            //this adds marker to map to get route
                            Point usersDestinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            Point usersOriginPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            GeoJsonSource geoJsonSource = MainMapboxMap.getStyle().getSourceAs("destination-source-id");
                            if (geoJsonSource != null) {
                                geoJsonSource.setGeoJson(Feature.fromGeometry(usersDestinationPoint));
                            }
                            //this shows the users location
                            showUserRoute(usersOriginPoint, usersDestinationPoint);
                            startButton.setEnabled(true);
                            return true;
                        }
                    });
                    //method calls
                    locationEnable(style);
                    searchBar();
                    addMarker(style);
                }
            });
            }
        });
        return view;
    }
    //once a pin has been dropped and the start button has been clicked a route will be shown
    private void showUserRoute(Point origin, Point destination) {
        NavigationRoute.builder(getContext()).accessToken(Mapbox.getAccessToken()).origin(origin).alternatives(true).destination(destination).profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC).build() .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        Log.d(TAG, "code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "A route was not found please try agian or the access token is wrong!!");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);
                        if (navigationRoute != null) {
                            navigationRoute.removeRoute();
                        } else {
                            navigationRoute = new NavigationMapRoute(null, mapView, MainMapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationRoute.addRoute(currentRoute);
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "System Failure Error: " + throwable.getMessage());
                    }
                });
    }
    private void addMarker(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id", BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties( iconImage("destination-icon-id"), iconAllowOverlap(true), iconIgnorePlacement(true));
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }
    private void searchBar() {
        searchBarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this moves camera to address entered
                Intent intent = new PlaceAutocomplete.IntentBuilder().accessToken(Mapbox.getAccessToken()).placeOptions(PlaceOptions.builder().build(PlaceOptions.MODE_CARDS)).build(getActivity());startActivityForResult(intent, requestCode);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default);
            if (MainMapboxMap != null) {
                Style style = MainMapboxMap.getStyle();
                MainMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(), ((Point) selectedCarmenFeature.geometry()).longitude())).zoom(20.0).build()), 5000);
                Toast.makeText(getActivity(), "Tap location to set Marker and then click start navigation!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
     //   this displays the map
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    ///this will get location permissions from the user
    @Override
    public void onPermissionResult(boolean granted) {
    }
    // gets permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @SuppressWarnings( {"MissingPermission"})
    private void locationEnable(@NonNull Style loadedMapStyle) {
        if(PermissionsManager.areLocationPermissionsGranted(getContext()))
        {
            LocationComponent locationComponent = MainMapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this.getContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.GPS);
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }

    }
}
