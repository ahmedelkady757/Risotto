package com.example.risotto.data.repository.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Completable login(String email, String password);
    Completable register(String email, String password, String username);
    Completable loginWithGoogle(AuthCredential credential);
    Completable loginAsGuest();
    void logout();
    Single<FirebaseUser> getCurrentUser();
    boolean isUserLoggedIn();
}
