package com.example.historyhike.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.historyhike.R;
import com.example.historyhike.controller.ApiController;
import com.example.historyhike.model.User;

public class AccountActivity extends AppCompatActivity {

    private EditText etfName;
    private EditText etSurname;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnChangeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize UI components
        etfName = findViewById(R.id.etfName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnChangeAccount = findViewById(R.id.btnChangeAccount);

        ImageView buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Fetch JWT from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("HistoryHikePrefs", MODE_PRIVATE);
        String jwt = sharedPreferences.getString("JWT_TOKEN", null);

        if (jwt != null) {
            ApiController apiController = new ApiController();
            apiController.fetchUserDetails(jwt, new ApiController.FetchUserCallback() {
                @Override
                public void onSuccess(User user) {
                    // Populate the fields with the returned user data
                    etfName.setText(user.getFirstName());
                    etSurname.setText(user.getSurname());
                    etEmail.setText(user.getEmail());
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            btnChangeAccount.setOnClickListener(v -> {
                // Retrieve updated user info
                String firstName = etfName.getText().toString().trim();
                String surname = etSurname.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();

                // Basic validation
                if (firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    Toast.makeText(AccountActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(AccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make the update request
                apiController.updateUserDetails(jwt, firstName, surname, email, password, new ApiController.UpdateUserCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AccountActivity.this, "Account details updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // exit this screen once updated
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } else {
            Toast.makeText(this, "JWT token not found. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }
}