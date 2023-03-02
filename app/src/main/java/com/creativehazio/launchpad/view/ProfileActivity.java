package com.creativehazio.launchpad.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.creativehazio.launchpad.R;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button darkModeBtn;
    private Button getPremiumBtn;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViewsAndConfigs();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        darkModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDarkMode();
            }
        });

        getPremiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,GetPremiumActivity.class));
            }
        });
    }

    private void enableDarkMode() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", true);
        }
        editor.apply();
    }

    private void initViewsAndConfigs() {
        toolbar = findViewById(R.id.profile_toolbar);
        darkModeBtn = findViewById(R.id.dark_mode_btn);
        getPremiumBtn = findViewById(R.id.get_premium_btn);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isNightMode = sharedPreferences.getBoolean("night",false);
    }
}