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
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) getSystemService(
                android.content.Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return;

        networkCallback = new android.net.ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull android.net.Network network) {
                runOnUiThread(() -> {
                    if (tvOfflineBanner != null)
                        tvOfflineBanner.setVisibility(android.view.View.GONE);
                });
            }

            @Override
            public void onLost(@NonNull android.net.Network network) {
                runOnUiThread(() -> {
                    if (tvOfflineBanner != null)
                        tvOfflineBanner.setVisibility(android.view.View.VISIBLE);
                });
            }
        };

        android.net.NetworkRequest request = new android.net.NetworkRequest.Builder()
                .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(request, networkCallback);

        // Check initial state
        boolean isConnected = false;
        android.net.Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork != null) {
            android.net.NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
            isConnected = caps != null && caps.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }

        if (!isConnected && tvOfflineBanner != null) {
            tvOfflineBanner.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.logFragment("MainActivity", "onDestroy");
        if (networkCallback != null) {
            android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) getSystemService(
                    android.content.Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }
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
                AppLogger.logNav("BottomNav -> homeFragment (popBackStack)");
                return true;
            }

            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        AppLogger.d("MainActivity: BottomNavigation custom listener ready");
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

                    AppLogger.logNav(destination.getLabel() != null
                            ? destination.getLabel().toString()
                            : String.valueOf(destination.getId()));

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