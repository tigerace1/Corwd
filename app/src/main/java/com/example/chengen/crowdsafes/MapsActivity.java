package com.example.chengen.crowdsafes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                Intent ii2 = getIntent();
                Bundle v2 = ii2.getExtras();
                if(v2!=null){
                    if(v2.getString("Location")!=null)
                    latitude = myLocation.getLatitude();
                    longitude = myLocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                    MarkerOptions maker = new MarkerOptions().draggable(true).position(new LatLng(latitude,longitude))
                            .title(v2.getString("LocDescription"));
                    Marker marker = mMap.addMarker(maker);
                    marker.showInfoWindow();
                    marker.setDraggable(false);
                }else {
                    latitude = myLocation.getLatitude();
                    longitude = myLocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                    MarkerOptions maker = new MarkerOptions().draggable(true).position(new LatLng(latitude, longitude)).title("Your are here!");
                    Marker marker = mMap.addMarker(maker);
                    marker.showInfoWindow();
                    marker.setDraggable(true);
                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int count = 0;
        if(b!=null)
           count = b.getInt("type");
        SharedPreferences sharedPref=getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        switch (count){
            case 0:
                editor = sharedPref.edit();
                editor.putString("missingLoc",longitude+","+latitude);
                editor.apply();
                break;
            case 1:
                editor = sharedPref.edit();
                editor.putString("aggressionLoc",longitude+","+latitude);
                editor.apply();
                break;
            case 2:
                editor = sharedPref.edit();
                editor.putString("medicalLoc",longitude+","+latitude);
                editor.apply();
                break;
            case 3:
                editor = sharedPref.edit();
                editor.putString("sanitaryLoc",longitude+","+latitude);
                editor.apply();
                break;
            case 4:
                editor = sharedPref.edit();
                editor.putString("suspicionLoc",longitude+","+latitude);
                editor.apply();
                break;
            case 10:
                startActivity(new Intent(this,NavigationMenu.class));
                break;
            case 20:
                startActivity(new Intent(this,ReciverPhoto.class));
                break;
        }
    }
}
