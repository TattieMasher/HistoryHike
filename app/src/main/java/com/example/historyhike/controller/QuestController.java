package com.example.historyhike.controller;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.historyhike.model.Objective;
import com.example.historyhike.model.ProximityListener;
import com.example.historyhike.model.Quest;
import com.example.historyhike.model.Museum;
import com.example.historyhike.view.MapsActivity;

import java.util.ArrayList;
import java.util.List;

public class QuestController implements ProximityListener {
    private ArrayList<Quest> availableQuests;
    private Quest currentQuest;
    private int currentObjectiveIndex;
    private Museum museum;      // Coupled to museum. Maybe not ideal...
    private MapsActivity mapsActivity;
    final float PROXIMITY_THRESHOLD = 30; // TODO: Revise the proximity

    public QuestController(ArrayList<Quest> availableQuests, Museum museum) {
        this.availableQuests = availableQuests;
        this.museum = museum;
        this.currentQuest = null; // No current quest initially
    }

    public void setMapsActivity(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onProximityCheck(Location location) {
        checkAndUpdateObjectiveBasedOnProximity(location);
    }

    public ArrayList<Quest> getAvailableQuests() {
        return availableQuests;
    }

    public void setAvailableQuests(ArrayList<Quest> availableQuests) {
        this.availableQuests = availableQuests;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
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

            // Preload images for each objective
            for (Objective objective : quest.getQuestPath()) {
                String imageUrl = objective.getImageURL(); // TODO: Make sure this is always set. I know you're gonna forget later, so todo todo todo
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(mapsActivity)
                            .load(imageUrl)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .preload();
                }
            }

            if (mapsActivity != null) {
                mapsActivity.updateMapObjective(getCurrentObjective());
            }
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
            // TODO: extra logic
            currentQuest = null;
        }
    }

    public void checkAndUpdateObjectiveBasedOnProximity(Location currentLocation) {
        if (currentQuest == null || currentObjectiveIndex < 0 || currentObjectiveIndex >= currentQuest.getQuestPath().size()) {
            return; // No active quest or invalid objective index
        }

        Objective currentObjective = getCurrentObjective();
        if (currentObjective == null) {
            return; // TODO: Handle this error case
        }

        float[] results = new float[1];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                currentObjective.getLatitude(), currentObjective.getLongitude(), results);
        float distanceInMeters = results[0];

        if (distanceInMeters <= PROXIMITY_THRESHOLD) {
            completeObjective();
        }

        Log.d("ProximityCheck", "Distance to objective: " + distanceInMeters + " meters.");
        Log.d("ProximityCheck", currentQuest.getTitle() + ": " + currentObjective.getName());
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
                // Update objectives list from view
                mapsActivity.completeObjective();
                // Increment current objective AFTERWARDS, so that UI can update elements with prev objective completion first
                currentObjectiveIndex++;
                // Fetch the next objective after incrementing the index
                Objective nextObjective = getCurrentObjective();
                // Update the map with the *next* objective
                mapsActivity.runOnUiThread(() -> mapsActivity.updateMapObjective(nextObjective));
            } else {
                // All objectives completed
                completeQuest();
            }
        }
    }
}
