package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.presentation.auth.views.RegisterView;

public interface RegisterPresenter {
    void attachView(RegisterView view);
    void detachView();
    void registerWithEmail(String username, String email, String password);
}
