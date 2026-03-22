package com.example.risotto.presentation.auth.views;

public interface RegisterView {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void navigateToHome();
    void clearErrors();
    void showUsernameError(String message);
    void showEmailError(String message);
    void showPasswordError(String message);
}
