package com.example.historyhike.model;

public class Objective {
    private double latitude;
    private double longitude;
    private String description;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // This method will flag this objective as complete upon the user reaching their goal
    public boolean completeObjective() {
        return true;
    }
}

