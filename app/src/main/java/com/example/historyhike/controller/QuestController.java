package com.example.historyhike.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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

    // TODO: review this, because this is not very MVC...
    public void completeQuest() {
        Log.d("QuestDebug", "Completing quest now");
        mapsActivity.setLastObjComplete(true);

        if (currentQuest != null) {
            // Add the artefact to the museum
            mapsActivity.getMuseumController().addArtefact(currentQuest.getReward());
            currentQuest.setState(Quest.QuestState.COMPLETED);

            // Retrieve JWT from SharedPreferences
            SharedPreferences sharedPreferences = mapsActivity.getSharedPreferences("HistoryHikePrefs", MODE_PRIVATE);
            String jwt = sharedPreferences.getString("JWT_TOKEN", null);

            if (jwt != null) {
                // Call API to complete the quest
                mapsActivity.getApiController().completeQuest(jwt, currentQuest.getId(), new ApiController.CompleteQuestCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("QuestDebug", "Quest completion API success.");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("QuestDebug", "Quest completion API failed: " + errorMessage);
                        // Handle failure (optional: retry, show a message, etc.)
                    }
                });
            } else {
                Log.e("QuestDebug", "JWT token not found. Unable to complete quest.");
            }

            // Reset current quest
            currentQuest = null;
            currentObjectiveIndex = -1;
            Log.d("QuestDebug", "Quest completed, state reset");
        } else {
            Log.d("QuestDebug", "Attempted to complete a null quest");
        }

        mapsActivity.cancelQuest();  // Reset the UI
    }


    public void cancelQuest() {
        if (currentQuest != null && currentQuest.getState() == Quest.QuestState.IN_PROGRESS) {
            currentQuest.setState(Quest.QuestState.NOT_STARTED);
            // TODO: extra logic
            currentQuest = null;
        }
    }

    public void checkAndUpdateObjectiveBasedOnProximity(Location currentLocation) {
        if (currentQuest == null) {
            Log.d("ProximityCheck", "No active quest. Skipping proximity check.");
            return;
        }

        if (currentObjectiveIndex < 0 || currentObjectiveIndex >= currentQuest.getQuestPath().size()) {
            Log.d("ProximityCheck", "Invalid objective index. Skipping proximity check.");
            return;
        }

        Objective currentObjective = getCurrentObjective();
        if (currentObjective == null) {
            Log.d("ProximityCheck", "Current objective is null. Skipping proximity check.");
            return;
        }

        float[] results = new float[1];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                currentObjective.getLatitude(), currentObjective.getLongitude(), results);
        float distanceInMeters = results[0];

        Log.d("ProximityCheck", "Distance to objective: " + distanceInMeters + " meters. Objective: " + currentObjective.getName());

        if (distanceInMeters <= PROXIMITY_THRESHOLD) {
            completeObjective();
        }
    }

    public Objective getCurrentObjective() {
        if (currentQuest != null && currentObjectiveIndex >= 0) { // set by startQuest
            return currentQuest.getQuestPath().get(currentObjectiveIndex);
        }
        return null;
    }

    public void completeObjective() {
        Log.d("QuestDebug", "Attempting to complete objective at index: " + currentObjectiveIndex);
        if (currentQuest == null || currentObjectiveIndex < 0 || currentObjectiveIndex >= currentQuest.getQuestPath().size()) {
            Log.d("QuestDebug", "Invalid quest or index");
            return;
        }

        Objective currentObjective = getCurrentObjective();
        if (currentObjective == null) {
            Log.d("QuestDebug", "Current objective is null");
            return;
        }

        currentObjective.setComplete(true);
        Log.d("QuestDebug", "Objective marked as complete");

        if (currentObjectiveIndex == currentQuest.getQuestPath().size() - 1) {
            Log.d("QuestDebug", "Last objective completed, completing quest");
            mapsActivity.completeObjective();
            completeQuest();
        } else {
            mapsActivity.completeObjective();
            currentObjectiveIndex++;
            Objective nextObjective = getCurrentObjective();
            if (nextObjective != null) {
                mapsActivity.runOnUiThread(() -> mapsActivity.updateMapObjective(nextObjective));
            }
        }
    }
}
