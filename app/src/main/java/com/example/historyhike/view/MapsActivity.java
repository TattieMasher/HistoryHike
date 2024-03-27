package com.example.historyhike.view;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.widget.Toast;

import com.example.historyhike.R;
import com.example.historyhike.model.Geolocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.historyhike.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Geolocation.LocationUpdateListener {

    private GoogleMap mMap;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Geolocation geolocation; // Use THIS instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialise Geolocation object
        geolocation = new Geolocation(this);

        // Set the location listener
        geolocation.setLocationUpdateListener(this);

        // Asynchronously find the MapFragment from layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geolocation.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        geolocation.stopLocationUpdates();
    }


    @Override
    public void onLocationUpdated(double lat, double lon) {
        // Update the map with the new location
        if (mMap != null) { // Check if mMap is initialised
            runOnUiThread(() -> {
                LatLng currentLatLng = new LatLng(lat, lon);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                mMap.clear(); // Clear existing markers
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if location permission granted. If not, request it.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Enable location viewing on the map
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, call onMapReady again to set up the location updates
                // First, check if mMap may is fully initialised
                if (mMap != null) {
                    onMapReady(mMap);
                }
            } else {
                Toast.makeText(this, "Location permission required. Please allow location access.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

