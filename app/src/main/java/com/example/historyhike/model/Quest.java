package com.example.historyhike.model;

import java.util.ArrayList;

public class Quest {
    private int id;
    private String title;
    private String description;
    private ArrayList<Objective> questPath;
    private QuestState state; // Enum to hold quest states (NOT_STARTED, IN_PROGRESS & COMPLETED)

    // Enum for QuestState
    public enum QuestState {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Objective> getQuestPath() {
        return questPath;
    }

    public void setQuestPath(ArrayList<Objective> questPath) {
        this.questPath = questPath;
    }

    public QuestState getState() {
        return state;
    }

    public void setState(QuestState state) {
        this.state = state;
    }

    public Quest() {
        this.questPath = new ArrayList<>();
        this.state = QuestState.NOT_STARTED;
    }

    public Objective getCurrentObjective() {
        // Return the first incomplete objective
        for (Objective obj : questPath) {
            if (!obj.isComplete()) {
                return obj;
            }
        }
        return null; // All objectives completed
    }

    public void downloadAndStartQuest() {
        // TODO: Will take a quest from my REST API and begin it. Will store it to allow it to be accessed offline. Maybe even download ALL quests, just not here...?
    }

    public void completeQuest() {
        // TODO: Will mark the quest as complete. Possibly delete it?
    }


    public void cancelQuest() {
        // TODO: Will cancel a quest, allowing the user to start a new one. Possibly delete it?
    }
}
