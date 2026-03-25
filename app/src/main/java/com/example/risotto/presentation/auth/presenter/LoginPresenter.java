package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.presentation.auth.views.LoginView;
import com.google.firebase.auth.AuthCredential;

public interface LoginPresenter {
    void attachView(LoginView view);
    void detachView();
    void loginWithEmail(String email, String password);
    void onGoogleSignInClicked();
    void loginWithGoogle(AuthCredential credential);
    void loginAsGuest();
}
