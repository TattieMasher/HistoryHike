package com.example.historyhike.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.historyhike.model.Geolocation;
import com.example.historyhike.model.ProximityListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeolocationController {
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Geolocation geolocation;
    private final Context context;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000; // Request code
    private LocationCallback locationCallback;
    private ProximityListener proximityListener;


    public GeolocationController(Context context, Geolocation geolocation) {
        this.context = context;
        this.geolocation = geolocation;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        initialiseLocationCallback();
    }

    public void setProximityListener(ProximityListener listener) {
        this.proximityListener = listener;
    }

    private void initialiseLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("LocationUpdate", "Location update received: " + locationResult.getLastLocation());
                if (locationResult == null) {
                    return; // TODO: Handle locationResult being null?
                }
                proximityListener.onProximityCheck(locationResult.getLastLocation());
                geolocation.updateLocation(locationResult.getLastLocation());
            }
        };
    }

    public void getLastLocation(OnSuccessListener<Location> listener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions check
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(listener);
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates(Activity activity) {
        // Check for location permissions and request if none. this is already done, but I suppose best to be safe.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(3000); // 3 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public int getLocationPermissionRequestCode() {
        return LOCATION_PERMISSION_REQUEST_CODE;
    }
}