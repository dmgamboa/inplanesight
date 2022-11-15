package com.inplanesight.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.data.LocationViewModel;

public class MainActivity extends AppCompatActivity {
    LocationViewModel locationService;
    ActivityResultLauncher<String> permissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNav();

        locationService = new LocationViewModel(this);
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
            for (String permission : LocationViewModel.REQUIRED_PERMISSIONS) {
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