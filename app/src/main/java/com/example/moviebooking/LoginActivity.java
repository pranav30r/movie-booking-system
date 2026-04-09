package com.example.moviebooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // ========== SPINNER: City list ==========
    private String[] cities = {"Select City", "Mumbai", "Delhi", "Bangalore", "Pune", "Chennai", "Hyderabad", "Kolkata", "Ahmedabad"};
    private String selectedCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ========== SHARED PREFERENCES: Auto-login check (Moved from SplashActivity) ==========
        SharedPreferences prefs = getSharedPreferences("CineBookPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        if (isLoggedIn) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return; // Stop execution of onCreate
        }

        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvSkip = findViewById(R.id.tvSkip);
        EditText etEmail = findViewById(R.id.etEmailPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        CheckBox cbTerms = findViewById(R.id.cbTerms);

        // ========== SPINNER: Setup city dropdown ==========
        Spinner spinnerCity = findViewById(R.id.spinnerCity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCity = cities[position];
                } else {
                    selectedCity = "Mumbai"; // default city
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCity = "Mumbai";
            }
        });

        // ========== EVENT HANDLING & VALIDATION ==========
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // 1. Basic Input Validation
            if (email.isEmpty()) {
                etEmail.setError("Email cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Password cannot be empty");
                return;
            }
            if (password.length() < 4) {
                etPassword.setError("Password too short");
                return;
            }
            
            // 2. CheckBox Validation
            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Please agree to Terms & Conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            navigateToMain();
        });

        tvSkip.setOnClickListener(v -> navigateToMain());
    }

    private void navigateToMain() {
        SharedPreferences prefs = getSharedPreferences("CineBookPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("selected_city", selectedCity.isEmpty() ? "Mumbai" : selectedCity);
        editor.apply();

        // ========== EXPLICIT INTENT ==========
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
