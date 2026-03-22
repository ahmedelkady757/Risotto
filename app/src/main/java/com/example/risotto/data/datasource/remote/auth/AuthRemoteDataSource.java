package com.example.risotto.data.datasource.remote.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRemoteDataSource {
    Completable loginWithEmail(String email, String password);
    Completable registerWithEmail(String email, String password, String username);
    Completable loginWithCredential(AuthCredential credential);
    Completable loginAsGuest();
    void logout();
    Single<FirebaseUser> getCurrentUser();
    boolean isUserLoggedIn();
}
