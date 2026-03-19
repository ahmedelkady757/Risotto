package com.example.risotto.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppLogger.logFragment("MainActivity", "onCreate");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupNavController();
        setupBottomNavigation();
        observeDestinationChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.logFragment("MainActivity", "onDestroy");
    }

    private void setupNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            AppLogger.e("MainActivity: NavHostFragment not found — check activity_main.xml id");
            return;
        }

        navController = navHostFragment.getNavController();
        AppLogger.d("MainActivity: NavController ready");
    }

    private void setupBottomNavigation() {
        if (navController == null) return;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AppLogger.d("MainActivity: BottomNavigation wired to NavController");
    }


    private void observeDestinationChanges() {
        if (navController == null) return;

        navController.addOnDestinationChangedListener(
                (@NonNull NavController controller,
                 @NonNull NavDestination destination,
                 Bundle arguments) -> {

                    AppLogger.logNav(destination.getLabel() != null
                            ? destination.getLabel().toString()
                            : String.valueOf(destination.getId()));

                    if (destination.getId() == R.id.mealDetailFragment) {
                        hideBottomNavigation();
                    } else {
                        showBottomNavigation();
                    }
                });
    }


    private void showBottomNavigation() {
        bottomNavigationView.animate()
                .translationY(0f)
                .setDuration(200)
                .start();
    }

    private void hideBottomNavigation() {
        bottomNavigationView.animate()
                .translationY(bottomNavigationView.getHeight())
                .setDuration(200)
                .start();
    }
}