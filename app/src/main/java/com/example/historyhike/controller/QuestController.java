package com.example.historyhike.controller;

import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;
import com.example.historyhike.model.Museum;
import java.util.ArrayList;
import java.util.List;

public class QuestController {
    private List<Quest> availableQuests;
    private Quest currentQuest;
    private int currentObjectiveIndex;
    private Museum museum;      // Coupled to museum. Maybe not ideal...

    public QuestController(List<Quest> availableQuests, Museum museum) {
        this.availableQuests = availableQuests;
        this.museum = museum;
        this.currentQuest = null; // No current quest initially
    }

    // Gets the starting points of all quests
    public ArrayList<Objective> getStartingPoints() {
        ArrayList<Objective> startingPoints = new ArrayList<>();
        for (Quest quest : availableQuests) {
            if (quest.getQuestPath() != null && !quest.getQuestPath().isEmpty()) {
                startingPoints.add(quest.getQuestPath().get(0)); // Add the starting point of each quest, which is their first objective
            }
        }
        return startingPoints;
    }

    public void startQuest(Quest quest) {
        if (quest != null && (currentQuest == null || currentQuest.getState() == Quest.QuestState.COMPLETED)) {
            this.currentQuest = quest;
            this.currentObjectiveIndex = 0; // Start with the first objective
            quest.setState(Quest.QuestState.IN_PROGRESS);
        }
    }

    public void completeQuest() {
        if (currentQuest != null) {
            // Set the quest state to COMPLETED
            currentQuest.setState(Quest.QuestState.COMPLETED);
            // Add the reward to the museum
            museum.addArtefact(currentQuest.getReward());
            currentQuest = null; // Reset current quest
            currentObjectiveIndex = -1; // Reset current objective index
        }
    }

    public void cancelQuest() {
        if (currentQuest != null && currentQuest.getState() == Quest.QuestState.IN_PROGRESS) {
            currentQuest.setState(Quest.QuestState.NOT_STARTED);
            // Additional logic to reset or remove the current quest
            currentQuest = null;
        }
    }

    public Objective getCurrentObjective() {
        if (currentQuest != null && currentObjectiveIndex >= 0) { // set by startQuest
            return currentQuest.getQuestPath().get(currentObjectiveIndex);
        }
        return null;
    }

    public void completeObjective() {
        if (currentQuest != null && currentObjectiveIndex >= 0) {
            Objective currentObjective = currentQuest.getQuestPath().get(currentObjectiveIndex);
            currentObjective.setComplete(true);

            if (currentObjectiveIndex < currentQuest.getQuestPath().size() - 1) {
                // Move to the next objective
                currentObjectiveIndex++;
            } else {
                // All objectives completed
                completeQuest();
            }
        }
    }
}
