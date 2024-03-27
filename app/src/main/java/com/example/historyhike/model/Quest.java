package com.example.historyhike.model;

import java.util.ArrayList;

public class Quest {
    private int questID;
    private String title;
    private String description;
    private Objective startingPoint;
    private ArrayList<Objective> questPath;
    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Objective getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(Objective startingPoint) {
        this.startingPoint = startingPoint;
    }

    // Will take a quest from my REST API and begin it. Will store it to allow it to be accessed offline
    public void downloadAndStartQuest() {
    }

    // Will mark the quest as complete
    public void completeQuest() {
    }

    // Will cancel a quest, allowing the user to start a new one
    public void cancelQuest() {
    }
}
