package com.example.historyhike.model;

import android.location.Location;

public class Geolocation {
    private double latitude;
    private double longitude;

    private LocationUpdateListener locationUpdateListener;

    // Defining an interface with which to accept location (lat, lon) updates
    public interface LocationUpdateListener {
        void onLocationUpdated(double latitude, double longitude);
    }

    public void setLocationUpdateListener(LocationUpdateListener listener) {
        this.locationUpdateListener = listener;
    }

    // Takes a location (defined as lat, lon) and sets the Geolocation's fields to match
    public void updateLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        notifyLocationUpdate();
    }

    // Notifies the listener of a new location
    private void notifyLocationUpdate() {
        if (locationUpdateListener != null) {
            locationUpdateListener.onLocationUpdated(latitude, longitude);
        }
    }
}
