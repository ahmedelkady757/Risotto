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
        } else {
            attachedView.showLocked();
        }
    }
}

