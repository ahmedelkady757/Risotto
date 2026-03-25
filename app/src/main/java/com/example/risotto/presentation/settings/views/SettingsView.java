package com.example.risotto.presentation.settings.views;

public interface SettingsView {
    void showLocked();
    void showContent();
    void showUserProfile(String name, String email, String photoUrl);
    void navigateToSplash();
    void showLogoutSuccess();
}

