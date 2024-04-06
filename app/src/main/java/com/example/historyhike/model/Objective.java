package com.example.historyhike.model;

public class Objective {
    private int id;
    private double latitude;
    private double longitude;
    private String name;    // TODO: decide whether or not to keep? Could be good to have a textual representation of an obj, BEFORE reaching it
    private String description;
    private boolean completionStatus;
    String completionText;

    private String imageURL;
    private Artefact reward;

    public Objective(int id, double latitude, double longitude, String name, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.completionStatus = false;  // Objective completion is obviously set to false upon creation
    }

    public double getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return completionStatus;
    }

    public void setComplete(boolean newStatus) {
        this.completionStatus = newStatus;
    }

    public Artefact getReward() {
        return reward;
    }

    public void setReward(Artefact reward) {
        this.reward = reward;
    }

    public String getCompletionText() {
        return completionText;
    }

    public void setCompletionText(String completionText) {
        this.completionText = completionText;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

