package com.example.historyhike.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.historyhike.R;
import com.example.historyhike.controller.GeolocationController;
import com.example.historyhike.controller.QuestController;
import com.example.historyhike.model.Geolocation;
import com.example.historyhike.model.Museum;
import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Geolocation.LocationUpdateListener {

    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private GeolocationController geolocationController;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000; // Must match GeolocationController
    private QuestController questController;
    private Museum museum; // TODO: Re-visit. Slightly breaks MVC as this is a model, but its functionality is so simple that an extra controller may overcomplicate
    private boolean isFirstLocated = false;
    ArrayList<Quest> allAvailableQuests = new ArrayList<>(); // TODO: This will come from my API, eventually
    private HashMap<Marker, Quest> markerQuestMap = new HashMap<>(); // Used to store quest-objective pairs to display from objective references within the map
    private final int DEFAULT_ZOOM = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpContentView();         // Set up activity and map within
        initialiseComponents();     // Initialise some dynamic components
        testQuests();               // Create test quests and populate them, to test as we go
        initialiseControllers();    // Initialise the geolocation and quest controllers

        // Request permissions, if necessary
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request if denied
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request if denied
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Begin updating locations again
            geolocationController.startLocationUpdates(this);
            questController.setMapsActivity(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause location updates when app is closed
        geolocationController.stopLocationUpdates();
    }

    private void testQuests(){
        // test quest3, with objectives
        Objective obj5 = new Objective(1, 55.6057023352494, -4.496883453828011, "Home", "desc");
        Objective obj6 = new Objective(1, 55.604129221001536, -4.496313879389737, "Mc'D's", "desc");
        ArrayList<Objective> path3 = new ArrayList<>();
        path3.add(obj5);
        path3.add(obj6);
        Quest quest3 = new Quest();
        quest3.setTitle("Test me");
        quest3.setDescription("From home to McDonald's. A Quest well completed.");
        quest3.setQuestPath(path3);

        museum = new Museum();
        allAvailableQuests.add(quest3); // This will be obtained from QuestController once structure is complete
        questController = new QuestController(allAvailableQuests, museum);
    }

    private void setUpContentView() {
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initialiseComponents() {
        Button btnCancelQuest = findViewById(R.id.btnCancelQuest);
        TextView scrollTitle = findViewById(R.id.scroll_title);

        btnCancelQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelQuest();
            }
        });

        scrollTitle.setText(getString(R.string.scroll_quest_title)); // Already set in XML anyway, but just to be sure
        initialiseBottomSheetBehavior();    // Allow expected BottomSheet behaviour
    }

    private void initialiseControllers() {
        Geolocation geolocation = new Geolocation();

        geolocationController = new GeolocationController(this, geolocation);
        geolocationController.setLocationUpdateListener(this); // Set MapsActivity as the location listener

        if(questController.getCurrentQuest() == null) {
            addQuestsToSheet(); // currently uses allAvailableQuests field
        } else {
            updateObjectivesView();
        }

        // Set QuestController's map reference to this activity
        questController.setMapsActivity(this);

        geolocationController.setProximityListener(questController);
    }

    private void initialiseBottomSheetBehavior() {
        // Find the BottomSheet LinearLayout by its ID
        final LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);

        // Get the BottomSheetBehavior from the XML
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Sets the initial state of the BottomSheet to collapsed
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Sets the peak height
        bottomSheetBehavior.setPeekHeight(200); // TODO: review this
        bottomSheetBehavior.setHideable(false); // Keep this. Stops it disappearing

        // Set the BottomSheet to expand fully on click
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    // To be called also when quests are cancelled or completed
    private void initialiseMapWithQuests() {
        if (mMap == null) return; // in case mMap is not initialized yet, as this is going to be called in multiple places

        mMap.clear(); // Clear any previous markers to re-draw

        // Loop through available quests to add their first objective as a marker
        for (Quest quest : allAvailableQuests) {
            Objective startingPoint = quest.getQuestPath().get(0); // Get the fist objective
            LatLng position = new LatLng(startingPoint.getLatitude(), startingPoint.getLongitude()); // Get its location
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(quest.getTitle())); // Create a marker for it
            markerQuestMap.put(marker, quest); // Associate marker with quest for later reference
        }

        // Setting marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            Quest quest = markerQuestMap.get(marker);
            if (quest != null) {
                showQuestDialog(quest); // Show a dialog when a quest marker is clicked
            }
            return true; // Return true to indicate we've handled the event
        });
    }

    private void addQuestsToSheet() {
        // Get reference to quest container from Activity_maps xml
        LinearLayout scrollContainer = findViewById(R.id.scroll_container);
        scrollContainer.removeAllViews(); // Clear the container of any remaining views

        for (Quest quest : allAvailableQuests) {
            // Inflate the quest item layout into quest container
            View questItem = LayoutInflater.from(this).inflate(R.layout.scroll_item, scrollContainer, false);

            // Set the title and description
            TextView title = questItem.findViewById(R.id.scroll_title);
            TextView description = questItem.findViewById(R.id.scroll_description);
            title.setText(quest.getTitle());
            description.setText(quest.getDescription());

            questItem.setOnClickListener(view -> {
                // Retrieve the first objective of the clicked quest
                Objective firstObjective = quest.getQuestPath().get(0);
                double lat = firstObjective.getLatitude();
                double lng = firstObjective.getLongitude();

                // Collapse the bottom sheet when quest is clicked
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                // Set map to location of first obj on clicked quest
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20)); // Adjust zoom level as needed
            });

            // Add the inflated view to the quest container
            scrollContainer.addView(questItem);
        }
    }

    public void completeObjective() {
        // Assuming you have a title and description for the completed objective
        String title = "Complete!";
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sagittis odio gravida, scelerisque nulla a, tristique lacus. Suspendisse fringilla arcu ac lobortis molestie. Sed quis nibh venenatis, dictum ante eu, egestas massa. Vestibulum venenatis lacinia mollis. Praesent tempus eleifend auctor. \\n\\n\n" +
                "Pellentesque eget magna arcu. Integer eget libero lectus. Morbi vitae metus in odio tempor efficitur vitae eget odio. Donec sit amet nibh dui. Phasellus dui turpis, hendrerit eget aliquam non, ultricies vitae dolor. Integer semper faucibus nisi, sit amet pellentesque orci iaculis at.";
        // TODO: New lines don't work
        ObjectiveCompleteDialogFragment dialogFragment = ObjectiveCompleteDialogFragment.newInstance(title, description);
        dialogFragment.show(getSupportFragmentManager(), "objectiveCompleteDialog");
        updateObjectivesView();
    }

    private void updateObjectivesView() {
        LinearLayout scrollContainer = findViewById(R.id.scroll_container);
        scrollContainer.removeAllViews(); // Clear existing views TODO: necessary? Only need this when moving from quests -> objectives

        Quest currentQuest = questController.getCurrentQuest();
        boolean foundNextObjective = false;

        if (currentQuest != null) {
            for (Objective objective : currentQuest.getQuestPath()) {
                View objectiveView = LayoutInflater.from(this).inflate(R.layout.scroll_item, scrollContainer, false);
                TextView title = objectiveView.findViewById(R.id.scroll_title);
                TextView description = objectiveView.findViewById(R.id.scroll_description);

                title.setText(objective.getName());
                description.setText(objective.getDescription());

                // If this objective is complete or we have already found the next objective,
                if (objective.isComplete() || foundNextObjective) {
                    title.setTextColor(getResources().getColor(R.color.greyText)); // grey out the text
                    description.setTextColor(getResources().getColor(R.color.greyText));
                } else {
                    // This is the next incomplete objective.
                    foundNextObjective = true; // Mark that the next objective is found
                }

                scrollContainer.addView(objectiveView, 0); // Add next objective at the top of the list

                // Once the next incomplete objective has been added, break to stop adding more.
                if (foundNextObjective) {
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geolocationController.startLocationUpdates(this);
                // Ensure the map is ready and permissions are granted before enabling the location layer
                if (mMap != null) {
                    try {
                        mMap.setMyLocationEnabled(true);
                        recenterMapOnUser();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Check for permissions again
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        initialiseMapWithQuests();
    }

    private void showQuestDialog(Quest quest) {
        new AlertDialog.Builder(this)
                .setTitle(quest.getTitle())
                .setMessage(quest.getLongDescription())
                .setPositiveButton("Accept", (dialogInterface, i) -> {
                    acceptQuest(quest);
                })
                .setNegativeButton("Decline", (dialogInterface, i) -> {
                    recenterMapOnUser();
                })
                .show();
    }

    private void acceptQuest(Quest quest) {
        TextView scrollTitle = findViewById(R.id.scroll_title);

        questController.startQuest(quest);
        recenterMapOnUser();
        updateObjectivesView();
        cancelButtonVisible(true); // Show cancel quest button

        scrollTitle.setText(R.string.scroll_obj_title);
    }

    public void cancelQuest() {
        questController.cancelQuest();
        // Hide the cancel button
        cancelButtonVisible(false);  // Hide cancel quest button
        initialiseMapWithQuests();  // Return map to showing quests, not current quest's objectives
        addQuestsToSheet();         // Add quests back to the BottomSheet
        recenterMapOnUser();        // Move the map to the user's location
    }

    public void completeQuest() {
        // Complete the quest...
        questController.completeQuest();
        // Hide the cancel button
        cancelButtonVisible(false);  // Hide cancel quest button
    }

    private void cancelButtonVisible(boolean visible) {
        Button btnCancelQuest = findViewById(R.id.btnCancelQuest);
        if(visible) {
            btnCancelQuest.setVisibility(View.VISIBLE);
        } else {
            btnCancelQuest.setVisibility(View.GONE);
        }
    }

    public void updateMapObjective(Objective objective) {
        if (objective != null) {
            LatLng objectiveLocation = new LatLng(objective.getLatitude(), objective.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(objectiveLocation).title(objective.getDescription()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(objectiveLocation, DEFAULT_ZOOM));
        }
    }

    private void recenterMapOnUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            geolocationController.getLastLocation(location -> {
                if (location != null) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM));
                }
            });
        }
    }

    @Override
    public void onLocationUpdated(double latitude, double longitude) {
        runOnUiThread(() -> {
            if(!isFirstLocated) {
                LatLng currentLocation = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
                isFirstLocated = true; // Flag variable to stop location updates re-centering EVERY time
            }
        });
    }
}