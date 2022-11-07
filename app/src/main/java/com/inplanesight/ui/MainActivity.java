package com.inplanesight.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.data.LocationService;

public class MainActivity extends AppCompatActivity {
    LocationService locationService;
    ActivityResultLauncher<String> permissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNav();

        locationService = new LocationService(this);
        permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                Toast.makeText(this, "Why you gotta be like this :(", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocation();
    }

    void requestLocation() {
        if (!locationService.hasLocationPermission()) {
            for (String permission : LocationService.REQUIRED_PERMISSIONS) {
                permissionsLauncher.launch(permission);
            }
        }

        if (!locationService.isLocationEnabled()) {
            locationService.requestEnableLocation();
        }
    }

    private void setupNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.mainNavHost);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}