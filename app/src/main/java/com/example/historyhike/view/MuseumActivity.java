package com.example.historyhike.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historyhike.R;
import com.example.historyhike.controller.ApiController;
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

        LinearLayout buttonBack = findViewById(R.id.header_layout);
        buttonBack.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerViewArtefacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Artefact> artefacts = (List<Artefact>) getIntent().getSerializableExtra("artefacts");
        if (artefacts == null || artefacts.isEmpty()) {
            Toast.makeText(this, "No artefacts available", Toast.LENGTH_SHORT).show();
            artefacts = new ArrayList<>();
        }

        adapter = new ArtefactAdapter(artefacts, this);
        recyclerView.setAdapter(adapter);

        LinearLayout buttonAccount = findViewById(R.id.account_header);
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MuseumActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        }); // Open account details change page
    }
}
