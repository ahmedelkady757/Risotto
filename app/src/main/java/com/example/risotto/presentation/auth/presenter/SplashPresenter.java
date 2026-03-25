package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.presentation.auth.views.SplashView;

public interface SplashPresenter {
    void attachView(SplashView view);
    void detachView();
    void decideNextScreen();
}
