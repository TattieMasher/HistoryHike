package com.example.historyhike.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.historyhike.R;
import com.example.historyhike.controller.GeolocationController;
import com.example.historyhike.model.Geolocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Geolocation.LocationUpdateListener {

    private GoogleMap mMap;
    private GeolocationController geolocationController;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000; // Must match LocationController

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Geolocation geolocation = new Geolocation();
        geolocation.setLocationUpdateListener(this); // Set MapsActivity as the location listener

        geolocationController = new GeolocationController(this, geolocation);

        // Find the map asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request if denied
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Begin updating locations again
            geolocationController.startLocationUpdates(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause location updates when app is closed
        geolocationController.stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geolocationController.startLocationUpdates(this);
            } else {
                Toast.makeText(this, "Location permission denied. Location is needed, please add it!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Check for permissions again
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        mMap.clear(); // Clear previous markers (if any) to re-draw
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.610611, -4.494881)).title("Kilmarnock Bus Station"));
    }

    @Override
    public void onLocationUpdated(double latitude, double longitude) {
        runOnUiThread(() -> {
            LatLng currentLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        });
    }
}
