package com.example.historyhike.model;

import java.util.ArrayList;

public class Quest {
    private int id;
    private String title;
    private String description;
    private ArrayList<Objective> questPath;
    private QuestState state; // Enum to hold quest states (NOT_STARTED, IN_PROGRESS & COMPLETED)
    private Artefact reward;

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

    public Artefact getReward() {
        return reward;
    }

    public void setReward(Artefact reward) {
        this.reward = reward;
    }

    public Quest() {
        this.questPath = new ArrayList<>();
        this.state = QuestState.NOT_STARTED;
    }

    // Overloaded method to pre-fill a quest. Most likely that this will be used, rather than multiple calls to add quest objectives.
    public Quest(ArrayList<Objective> questPath) {
        this.questPath = questPath;
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
}
