package com.example.risotto.presentation.settings.presenter;

import com.example.risotto.RisottoApp;
import com.example.risotto.presentation.settings.views.SettingsView;

public class SettingsPresenterImpl implements SettingsPresenter {
    private SettingsView attachedView;

    public SettingsPresenterImpl() {
    }

    @Override
    public void attachView(SettingsView view) {
        this.attachedView = view;
    }

    @Override
    public void detachView() {
        this.attachedView = null;
    }

    @Override
    public void checkAuthAndUpdate() {
        if (attachedView == null) return;
        if (RisottoApp.isRealUser()) {
            attachedView.showContent();
            com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String name = user.getDisplayName();
                String email = user.getEmail();
                String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
                attachedView.showUserProfile(name, email, photoUrl);
            }
        } else {
            attachedView.showLocked();
        }
    }

    @Override
    public void logout() {
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
        if (attachedView != null) {
            attachedView.showLogoutSuccess();
            attachedView.navigateToSplash();
        }
    }
}

