package com.example.risotto.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.risotto.core.utils.NetworkHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.risotto.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(android.view.View.GONE); // hidden until home loads

        setupNavController();
        setupBottomNavigation();
        observeDestinationChanges();
        setupNetworkListener();
    }

    private android.net.ConnectivityManager.NetworkCallback networkCallback;
    private android.widget.TextView tvOfflineBanner;

    private void setupNetworkListener() {
        tvOfflineBanner = findViewById(R.id.tv_offline_banner);
        if (tvOfflineBanner != null) {
            tvOfflineBanner.setText(R.string.error_offline_banner);
        }

        networkCallback = NetworkHelper.registerNetworkCallback(this, isConnected -> {
            runOnUiThread(() -> {
                if (tvOfflineBanner != null) {
                    tvOfflineBanner.setVisibility(isConnected ? android.view.View.GONE : android.view.View.VISIBLE);
                }
            });
        });

        // Initial check
        boolean currentlyConnected = NetworkHelper.isConnected(this);
        if (tvOfflineBanner != null) {
            tvOfflineBanner.setVisibility(currentlyConnected ? android.view.View.GONE : android.view.View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       NetworkHelper.unregisterNetworkCallback(this, networkCallback);
    }

    private void setupNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            return;
        }

        navController = navHostFragment.getNavController();
    }

    private void setupBottomNavigation() {
        if (navController == null)
            return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int destId = item.getItemId();
            NavDestination current = navController.getCurrentDestination();

            if (current != null && current.getId() == destId) {
                return true;
            }

            if (destId == R.id.homeFragment) {
                boolean popped = navController.popBackStack(R.id.homeFragment, false);
                if (!popped) {
                    navController.navigate(R.id.homeFragment);
                }
                return true;
            }

            return NavigationUI.onNavDestinationSelected(item, navController);
        });
    }

    private void observeDestinationChanges() {
        if (navController == null)
            return;

        java.util.Set<Integer> noNavDestinations = new java.util.HashSet<>();
        noNavDestinations.add(R.id.splashFragment);
        noNavDestinations.add(R.id.loginFragment);
        noNavDestinations.add(R.id.registerFragment);
        noNavDestinations.add(R.id.mealDetailFragment);

        navController.addOnDestinationChangedListener(
                (@NonNull NavController controller,
                        @NonNull NavDestination destination,
                        Bundle arguments) -> {

                    if (noNavDestinations.contains(destination.getId())) {
                        hideBottomNavigation();
                    } else {
                        showBottomNavigation();
                        if (bottomNavigationView.getSelectedItemId() != destination.getId()) {
                            if (bottomNavigationView.getMenu().findItem(destination.getId()) != null) {
                                bottomNavigationView.setSelectedItemId(destination.getId());
                            }
                        }
                    }
                });
    }

    private void showBottomNavigation() {
        if (bottomNavigationView.getVisibility() == android.view.View.VISIBLE)
            return;
        bottomNavigationView.setVisibility(android.view.View.VISIBLE);
        bottomNavigationView.animate()
                .translationY(0f)
                .setDuration(220)
                .start();
    }

    private void hideBottomNavigation() {
        if (bottomNavigationView.getVisibility() == android.view.View.GONE)
            return;
        bottomNavigationView.animate()
                .translationY(bottomNavigationView.getHeight())
                .setDuration(200)
                .withEndAction(() -> bottomNavigationView.setVisibility(android.view.View.GONE))
                .start();
    }
}