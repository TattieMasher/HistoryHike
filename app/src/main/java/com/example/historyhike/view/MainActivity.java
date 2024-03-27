package com.example.historyhike.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.historyhike.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Changed to start maps activity upon starting application, for troubleshooting
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
}