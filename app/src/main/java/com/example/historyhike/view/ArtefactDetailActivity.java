package com.example.historyhike.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.historyhike.R;

public class ArtefactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefact_detail);

        TextView textViewDescription = findViewById(R.id.textViewDescription);
        ImageView imageViewArtefact = findViewById(R.id.imageViewArtefact);

        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        textViewDescription.setText(description);
        Glide.with(this).load(imageUrl).into(imageViewArtefact);
    }
}
