package com.example.historyhike.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.historyhike.R;
import com.example.historyhike.controller.ApiController;

public class ResetActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);

        // Get the email from the intent extras (optional)
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            etEmail.setText(email);
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = etEmail.getText().toString().trim();
                if (emailInput.isEmpty()) {
                    Toast.makeText(ResetActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make the API call to reset the password
                ApiController apiController = new ApiController();
                apiController.resetPassword(emailInput, new ApiController.ResetPasswordCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(ResetActivity.this, "Password reset email sent! Check your spam.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ResetActivity.this, "Failed to reset password: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
