package com.example.historyhike.model;

import android.location.Location;

public interface ProximityListener {
    void onProximityCheck(Location location);
}
