package com.example.historyhike.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historyhike.R;
import com.example.historyhike.model.Artefact;

import java.util.ArrayList;
import java.util.List;

public class MuseumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArtefactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        recyclerView = findViewById(R.id.recyclerViewArtefacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve artefacts list from the Intent
        List<Artefact> artefacts = (List<Artefact>) getIntent().getSerializableExtra("artefacts");
        if (artefacts == null || artefacts.isEmpty()) {
            Toast.makeText(this, "No artefacts available", Toast.LENGTH_SHORT).show();
            artefacts = new ArrayList<>();
        }

        adapter = new ArtefactAdapter(artefacts, this);
        recyclerView.setAdapter(adapter);
    }
}
