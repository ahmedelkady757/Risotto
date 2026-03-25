package com.example.risotto.data.network.services;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseService {
    Completable signInWithEmail(String email, String password);
    Completable signUpWithEmail(String email, String password, String username);
    Completable signInWithCredential(AuthCredential credential);
    Completable signInAnonymously();
    void signOut();
    Single<FirebaseUser> getCurrentUser();
    boolean isUserLoggedIn();
}
