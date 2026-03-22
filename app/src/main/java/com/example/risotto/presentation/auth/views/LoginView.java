package com.example.risotto.presentation.auth.views;

public interface LoginView {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void navigateToHome();
    void clearErrors();
    void showEmailError(String message);
    void showPasswordError(String message);
}
