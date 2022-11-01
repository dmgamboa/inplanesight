package com.inplanesight.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.mainNavHost);
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
            NavigationUI.setupWithNavController(bottomNav, navController);
        } catch (Exception e) {
            Log.d("test", e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}