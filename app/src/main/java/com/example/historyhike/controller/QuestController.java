package com.example.historyhike.controller;

import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;
import com.example.historyhike.model.Museum;
import java.util.ArrayList;
import java.util.List;

public class QuestController {
    private List<Quest> availableQuests;
    private Quest currentQuest;
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

    public void startQuest(int questId) {
        // Find the quest by id and start it
        for (Quest quest : availableQuests) {
            if (quest.getId() == questId && (currentQuest == null || currentQuest.getState() == Quest.QuestState.COMPLETED)) {
                this.currentQuest = quest;
                currentQuest.setState(Quest.QuestState.IN_PROGRESS);
                // TODO ...
                break;
            }
        }
    }

    public void completeQuest() {
        if (currentQuest != null && currentQuest.getState() == Quest.QuestState.IN_PROGRESS) {
            // Set the quest state to COMPLETED
            currentQuest.setState(Quest.QuestState.COMPLETED);
            // Add the reward to the museum
            museum.addArtefact(currentQuest.getReward());
            // TODO ...
        }
    }

    public void completeObjective(int objectiveId) {
        if (currentQuest != null && currentQuest.getState() == Quest.QuestState.IN_PROGRESS) {
            for (Objective obj : currentQuest.getQuestPath()) {
                if (obj.getId() == objectiveId) {
                    obj.setComplete(true);
                    break;
                }
            }
            // Check if all objectives are complete
            if (currentQuest.getQuestPath().stream().allMatch(Objective::isComplete)) {
                completeQuest();
            }
        }
    }

    public void cancelQuest() {
        if (currentQuest != null && currentQuest.getState() == Quest.QuestState.IN_PROGRESS) {
            currentQuest.setState(Quest.QuestState.NOT_STARTED);
            // Additional logic to reset or remove the current quest
            currentQuest = null;
        }
    }
}
