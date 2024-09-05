package com.example.historyhike.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.historyhike.R;
import com.example.historyhike.controller.ApiController;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFName;
    private EditText etSName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFName = findViewById(R.id.etfName);
        etSName = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFName.getText().toString().trim();
                String sName = etFName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || fName.isEmpty() || sName.isEmpty() || passwordConfirm.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                ApiController apiController = new ApiController();
                apiController.register(email, password, fName, sName, new ApiController.RegisterCallback() {
                    @Override
                    public void onSuccess(String jwt) {
                        apiController.login(email, password, new ApiController.LoginCallback() {
                            @Override
                            public void onSuccess(String jwt) {
                                // Save JWT token in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("HistoryHikePrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("JWT_TOKEN", jwt);
                                editor.apply();

                                // Navigate to MapsActivity
                                Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                                startActivity(intent);
                                finish(); // Prevent user from going back to login screen
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}