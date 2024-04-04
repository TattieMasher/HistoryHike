package com.example.historyhike.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000; // Must match LocationController
    private QuestController questController;
    private Museum museum; // TODO: Re-visit. Slightly breaks MVC as this is a model, but its functionality is so simple that an extra controller may overcomplicate
    private boolean isFirstLocated = false;
    ArrayList<Quest> allAvailableQuests = new ArrayList<>(); // TODO: This will come from my API, eventually
    private HashMap<Marker, Quest> markerQuestMap = new HashMap<>(); // Used to store quest-objective pairs to display from objective references within the map


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Geolocation geolocation = new Geolocation();
        geolocation.setLocationUpdateListener(this); // Set MapsActivity as the location listener

        geolocationController = new GeolocationController(this, geolocation);

        // test quest1, with objectives
        Objective obj1 = new Objective(1, 55.606133555727325, -4.497475033611991, "TexMex", "desc");
        Objective obj2 = new Objective(1, 55.60692571331974, -4.497477031104262, "obj2", "desc");
        ArrayList<Objective> path1 = new ArrayList<>();
        path1.add(obj1);
        path1.add(obj2);
        Quest quest1 = new Quest();
        quest1.setTitle("Quest 1!");
        quest1.setDescription("C'mon, complete me!");
        quest1.setLongDescription("Are you brave enough to visit the Kilmarnock TexMex? \n\nLegend has it that the food can explode even the most iron-clad of stomachs! \n\nIf you can handle the heat, you can surely handle anything!");
        quest1.setQuestPath(path1);

        // test quest2, with objectives
        Objective obj3 = new Objective(1, 55.60631364862508, -4.497009665283578, "O'Shan's", "desc");
        Objective obj4 = new Objective(1, 55.60767046649543, -4.496556187349334, "obj4", "desc");
        ArrayList<Objective> path2 = new ArrayList<>();
        path2.add(obj3);
        path2.add(obj4);
        Quest quest2 = new Quest();
        quest2.setTitle("Quest 2...");
        quest2.setDescription("Complete me too, please!");
        quest2.setQuestPath(path2);

        museum = new Museum(); // Assuming Museum constructor doesn't take parameters
        allAvailableQuests.add(quest1); // Adding both test quests
        allAvailableQuests.add(quest2); // Adding both test quests
        questController = new QuestController(allAvailableQuests, museum);

        // Get reference to quest container from Activity_maps xml
        LinearLayout questContainer = findViewById(R.id.quest_container);

        // Iterate over test quests (TODO: from live db, obviously)
        for (Quest quest : allAvailableQuests) {
            // Inflate the quest item layout into quest container
            View questItem = LayoutInflater.from(this).inflate(R.layout.quest_item, questContainer, false);

            // Set the title and description
            TextView title = questItem.findViewById(R.id.quest_title);
            TextView description = questItem.findViewById(R.id.quest_description);
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
            questContainer.addView(questItem);
        }


        // Find the map asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialise BottomSheetBehavior to allow its functionality
        initialiseBottomSheetBehavior();
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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause location updates when app is closed
        geolocationController.stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geolocationController.startLocationUpdates(this);
            } else {
                Toast.makeText(this, "Location permission denied. Location is needed, please add it!", Toast.LENGTH_LONG).show();
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
        mMap.clear(); // Clear previous markers (if any) to re-draw

        // Get the starting points from the QuestController, of my test quests
        for (Quest quest : allAvailableQuests) {
            // Assuming each quest has a method to get its starting point
            Objective startingPoint = quest.getQuestPath().get(0);
            LatLng position = new LatLng(startingPoint.getLatitude(), startingPoint.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(quest.getTitle()));

            // Associate the marker with its quest
            markerQuestMap.put(marker, quest);
        }

        // Set up the marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            Quest quest = markerQuestMap.get(marker);
            if (quest != null) {
                showQuestDialog(quest);
            }
            return true;
        });
    }

    private void showQuestDialog(Quest quest) {
        new AlertDialog.Builder(this)
                .setTitle(quest.getTitle())
                .setMessage(quest.getLongDescription())
                .setPositiveButton("Accept", (dialogInterface, i) -> {
                    // Handle quest acceptance here
                })
                .setNegativeButton("Decline", null)
                .show();
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
