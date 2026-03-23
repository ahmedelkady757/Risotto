package com.example.risotto.presentation.settings.presenter;

import com.example.risotto.presentation.settings.views.SettingsView;

public interface SettingsPresenter {
    void attachView(SettingsView view);
    void detachView();
    void checkAuthAndUpdate();
    void logout();
}

