package com.example.chengen.crowdsafes;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static double latitude;
    private static double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
    }
    public static String getTarget() {
        return latitude + "" + longitude;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation == null) {
                Toast.makeText(this, "Unknow Location", Toast.LENGTH_LONG).show();
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                latitude = myLocation.getLatitude();
                longitude = myLocation.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                MarkerOptions maker = new MarkerOptions().draggable(true).position(new LatLng(latitude,longitude)).title("Your are here!");
                Marker marker = mMap.addMarker(maker);
                marker.showInfoWindow();
                marker.setDraggable(true);
               mMap.setOnMarkerDragListener(this);
            }
        }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
       
    }
    }
