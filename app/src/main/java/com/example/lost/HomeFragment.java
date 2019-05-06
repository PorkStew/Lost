package com.example.lost;



import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
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
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PermissionsListener, MapboxMap.OnMapClickListener, OnMapReadyCallback{


    // variables for adding location layer
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private FloatingActionButton fab_location_search;
    //gets user location
    private LocationEngine locationEngine;
    private LocationComponent locationComponent;
    private Location originlocation;
    private Marker destinationMarker;


    PermissionsManager permissionsManager = new PermissionsManager(this);
    PlaceAutocompleteFragment autocompleteFragment;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = view.findViewById(R.id.mapViews);

        startButton = view.findViewById(R.id.startB);
        fab_location_search = view.findViewById(R.id.fab_location_searchB);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch navigation ui
            }
        });


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                map = mapboxMap;

            mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

                    map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                        @Override
                        public boolean onMapClick(@NonNull LatLng point) {
                            Toast.makeText(getActivity(), "ssssssss",
                                    Toast.LENGTH_SHORT).show();
                            Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            Toast.makeText(getActivity(), "Map Clicked", Toast.LENGTH_SHORT).show();
                            Point originPoint = Point.fromLngLat(point.getLongitude(),
                                    point.getLatitude());

                            GeoJsonSource source = map.getStyle().getSourceAs("destination-source-id");
                            if (source != null) {
                                source.setGeoJson(Feature.fromGeometry(destinationPoint));
                            }

                            //getRoute(originPoint, destinationPoint);
                            startButton.setEnabled(true);
                            return true;



                        }
                    });
                    enableLocationComponent(style);
                    initSearchFab();
                    addDestinationIconSymbolLayer(style);


                }
            });

            }
        });

        return view;
    }
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {

        Toast.makeText(getActivity(), "Hello i am in add destionation icon",
                Toast.LENGTH_SHORT).show();

        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }
    private void initSearchFab() {
        fab_location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlaceOptions.builder()
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default);

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (map != null) {
                Style style = map.getStyle();


// Move map camera to the selected location
                map.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                        ((Point) selectedCarmenFeature.geometry()).longitude()))
                                .zoom(16.0)
                                .build()), 4000);

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
     //   Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

//gets user promissions
    @Override
    public void onPermissionResult(boolean granted) {
    }
    // gets permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if(PermissionsManager.areLocationPermissionsGranted(getContext()))
        {
            LocationComponent locationComponent = map.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this.getContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.GPS);

        }else{

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }

    }
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }

    @Override
    public boolean onMapClick(@NonNull LatLng latLng) {
        return false;
    }
}
